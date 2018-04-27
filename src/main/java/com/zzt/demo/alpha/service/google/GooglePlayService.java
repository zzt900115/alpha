package com.zzt.demo.alpha.service.google;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.FileContent;
import com.google.api.client.repackaged.com.google.common.base.Preconditions;
import com.google.api.client.repackaged.com.google.common.base.Strings;
import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.AndroidPublisher.Edits.Images;
import com.google.api.services.androidpublisher.model.*;
import com.google.common.collect.Lists;
import com.zzt.demo.alpha.common.GoogleAppListingDO;
import com.zzt.demo.alpha.common.ImageAndroid;
import com.zzt.demo.alpha.common.ReleaseGoogleLang;
import com.zzt.demo.alpha.common.Result;
import com.zzt.demo.alpha.utils.ConcurrentUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Service
public class GooglePlayService {


    //保存在当前线程中
    public  ThreadLocal<AndroidPublisher.Edits> editsThreadLocal = new ThreadLocal<>() ;
    public  ThreadLocal<String> editIdThreadLocal = new ThreadLocal<>() ;

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Track for uploading the apk, can be 'alpha', beta', 'production' or
     * 'rollout'.
     */
    //应用的Alpha和Beta版本部署到您分配给Alpha和Beta测试组的用户F
    public static final String TRACK_ALPHA = "alpha";
    public static final String TRACK_BETA = "beta";
    public static final String TRACK_PROD = "production";
    public static final String TRACK_ROLLOUT = "rollout";//“部署”版本（“分阶段发布”的简称）


    public static final String IMAGE_JPG = "image/jpeg";
    public static final String IMAGE_PNG = "image/png";

    public static final String IMAGETYPE_FETUREGRAPIC = "featureGraphic";//置顶大图
    public static final String IMAGETYPE_ICON = "icon";//图标
    public static final String IMAGETYPE_SCREENSHOTS = "phoneScreenshots";//手机展示截图



    /**
     * 上传apk到google play
     * 并增加what's new到google play
     * @return
     */
    public Result updateApk2GooglePlay(String applicatinName , String packageName, Integer versionCode,
                                       String apkFilePath, String trackName, double userFraction ,
                                       List<ReleaseGoogleLang> langsWithNew){

        Result result = new Result();
        File localApk= new File(apkFilePath);
        if(!localApk.exists()){
            result.setSuccess(false);
            result.setMsg("apk 服务器文件不存在");
            return result;
        }
        List<Future> futureList = new ArrayList<>();
        try {
            //生成当前线程的 edits  editId  提交commit信息必需参数
            //放入当前线程中
            initEditsThreadLocal(applicatinName,packageName);

            //从当前线程threadlocal中获取edits  editId
            AndroidPublisher.Edits  edits = editsThreadLocal.get();
            String editId = editIdThreadLocal.get();

            final AbstractInputStreamContent apkFile =
                    new FileContent(AndroidPublisherHelper.MIME_TYPE_APK, localApk);

            //异步提交到google play 上传apk
            AndroidPublisher.Edits.Apks.Upload uploadRequest = edits
                    .apks()
                    .upload(packageName,editId,apkFile);
            Apk apk = uploadRequest.execute();
            log.info("***上传后  apk信息：{}",JSON.toJSONString(apk));

            if(apk.getVersionCode() != versionCode){
                log.error("versionCode 错误,本地versionCode :{},上传apk 信息:{}",
                        versionCode,JSON.toJSONString(apk));
                result.setSuccess(false);
                result.setMsg("上传apk失败");
                result.setData(langsWithNew);
                return result;
            }

            log.info("Version code %d has been uploaded",apk.getVersionCode());

            // Assign apk to alpha track.

           // String trackStr = trackName;
            List<Integer> apkVersionCodes = new ArrayList<>();
            apkVersionCodes.add(versionCode);

            final Track track = new Track();
            track.setTrack(trackName);
            track.setVersionCodes(apkVersionCodes);
            //如果是rollout通道 需要设置
            if(trackName.equals(TRACK_ROLLOUT)){
                track.setUserFraction(userFraction);
            }


            //异步提交 指定 上传的apk到到特定渠道
            AndroidPublisher.Edits.Tracks.Update updateTrackRequest = edits
                    .tracks()
                    .update(packageName,editId,track.getTrack(),track);

            futureList.add(ConcurrentUtil.executorService.submit(() ->{
                try {
                    Track updatedTrack = updateTrackRequest.execute();
                    log.info(String.format("Track %s has been updated.Track:{}, Track 结构体:{}",
                            updatedTrack.getTrack(), JSON.toJSONString(updatedTrack)));
                } catch (IOException e) {
                    log.error("执行更新updateTrackRequest失败， packagename:{}",
                            packageName);
                    throw new IllegalArgumentException(String.valueOf(e.getMessage()));
                }
            }
            ));

            if( !CollectionUtils.isEmpty(langsWithNew)) {
                //异步  每个语言版本更新各自语言版本
                for (ReleaseGoogleLang langWhatNew : langsWithNew) {
                    // Update recent changes field in apk listing.
                    final ApkListing newApkListing = new ApkListing();
                    newApkListing.setRecentChanges(langWhatNew.getRecentChanges());

                    AndroidPublisher.Edits.Apklistings.Update
                            updateRecentChangesRequest = edits
                            .apklistings()
                            .update(packageName,
                                    editId,
                                    versionCode,
                                    langWhatNew.getLanguage(),
                                    newApkListing);
                    futureList.add(ConcurrentUtil.executorService.submit(() -> {
                                try {
                                    updateRecentChangesRequest.execute();
                                } catch (IOException e) {
                                    log.error("执行更新updateRecentChangesRequest 失败，" +
                                                    "packagename:{},language:{},what's new:{}",
                                            packageName, langWhatNew.getLanguage(), langWhatNew.getRecentChanges());
                                    throw new IllegalArgumentException(String.valueOf(e.getMessage()));
                                }
                            }
                    ));

                }
            }

            //遍历结果  如果有异常 跳出操作
            for(Future future :futureList){
                try {
                    future.get();
                } catch (Exception e) {
                    log.error("线程池执行task  中间出错{}",e.getMessage());
                    result.setSuccess(false);
                    result.setMsg("上传apk 相关信息出错");
                   return result;
                }
            }
            log.info("Recent changes has been updated.");

            commitEdits(packageName);
        } catch (IOException  | GeneralSecurityException ex) {
            log.error("Excpetion was thrown while uploading apk to alpha track", ex);
            result.setSuccess(false);
            result.setMsg("上传apk 相关信息出错");
        }
        return result;
    }


    /**
     * 更新已经发布过的App   不同语言下的 相关描述+截图信息
     * @param applicatinName 应用名
     * @param packageName 应用包名  对应google play上的包名
     * @param listingDOList
     * @return
     */
    public String  updateAppListingAndImages(
            String applicatinName , String packageName,List<GoogleAppListingDO> listingDOList){

        if(CollectionUtils.isEmpty(listingDOList)){
            return "";
        }

        try {
            initEditsThreadLocal(applicatinName,packageName);

            List<Future> futureList = new ArrayList<>();

          //  for()



            commitEdits(packageName);
        } catch (IOException  | GeneralSecurityException ex) {
            log.error("Excpetion was thrown while uploading apk to alpha track", ex);
        }
        return "";
    }

    /**
     * 更新图标截图等图片+title+相关描述到  google play
     * @return
     */
    public Result  updateApkInfos(String applicatinName ,String packageName,
                                List<GoogleAppListingDO> appListingDOList ){

        Result result = new Result();
        if(CollectionUtils.isEmpty(appListingDOList)){
            result.setSuccess(false);
            result.setMsg("文本上传信息为空");
            return result;
        }

        List<Future> futureList = new ArrayList<>();
        try {
            initEditsThreadLocal(applicatinName,packageName);

            AndroidPublisher.Edits edits =editsThreadLocal.get();
            String editId = editIdThreadLocal.get();

            for(GoogleAppListingDO appListingDO :appListingDOList){

            }
            for(Future future :futureList){
                try {
                    future.get(60,TimeUnit.SECONDS);
                    //log.info("上传成功的Image:{}",JSON.toJSONString(image));
                }catch (Exception e){
                    log.error("***更新google play 文案文本失败:{}",e.getMessage());
                }
            }
            commitEdits(packageName);

        } catch (IOException  | GeneralSecurityException ex) {
            log.error("Excpetion was thrown while uploading apk to alpha track", ex);
        }
        result.setSuccess(true);
        return result;
    }

    /**
     * 更新图标截图等图片+title+相关描述到  google play
     * @return
     */
    public Result  updateApkAllInfos(String applicatinName ,String packageName,
                               List<GoogleAppListingDO> appListingDOList ){

        Result result = new Result();
        if(CollectionUtils.isEmpty(appListingDOList)){
            result.setSuccess(false);
            result.setMsg("文本上传信息为空");
            return result;
        }

        List<Future> futureList = new ArrayList<>();
        boolean textSuccess =false;
        boolean imageSuccess =false;
        try {
            initEditsThreadLocal(applicatinName,packageName);

            AndroidPublisher.Edits edits =editsThreadLocal.get();
            String editId = editIdThreadLocal.get();

            for(GoogleAppListingDO appListingDO :appListingDOList){

                //提交文案更新 异步
              String textResult=  updateApkInfo(edits,editId,packageName,appListingDO.getLanguage(),appListingDO,futureList);

                //提交图片更新  异步
              String imageResult = updateImages(edits,editId,packageName,appListingDO.getLanguage(),appListingDO.getImageDOList(),futureList);

              textSuccess = StringUtils.isBlank(textResult);
              imageSuccess = StringUtils.isBlank(imageResult);
            }

           // List<ImageAndroid> imageReturnList = new ArrayList<>();
            //获取图片更新成功后的处理方案
            for(Future<ImageAndroid> future :futureList){
                try {
                    ImageAndroid image =  future.get(60,TimeUnit.SECONDS);
                    if(image == null){
                        continue;
                    }
                    //imageReturnList.add(image);
                    log.info("上传成功的Image:{}",JSON.toJSONString(image));
                }catch (Exception e){
                    log.error("上传失败的图片:{}",e.getMessage());
                }
            }

            commitEdits(packageName);
        } catch (IOException  | GeneralSecurityException ex) {
            log.error("Excpetion was thrown while uploading apk to alpha track", ex);
        }

        result.setSuccess(textSuccess & imageSuccess);
        return result;
    }

    /**
     * 更新单个语言的图片信息 需要遍历异步future List结果集
     * @param edits
     * @param editId
     * @param language
     * @param imageDOList
     * @param futureList
     * @return
     */
    public String updateImages(AndroidPublisher.Edits edits, String editId ,String packageName,
                               String language, List<ImageAndroid> imageDOList, List<Future> futureList ){
        String errMsg ="";

        if(CollectionUtils.isEmpty(imageDOList)){
            String errmsg =
                    String.format("packageName:%s,language：%s,更新google play 图片信息为空",packageName,language);
            log.info(errmsg);
            return errmsg;
        }
        if(futureList == null){
            futureList = new ArrayList<>();
        }
        if(edits == null ||editId == null){
            return "";
        }
        try {
            //执行所有线上图片删除操作
            ImagesDeleteAllResponse delete1 =
                    edits.images().deleteall(packageName,editId,language,IMAGETYPE_FETUREGRAPIC).execute();
            ImagesDeleteAllResponse  delete2 =
                    edits.images().deleteall(packageName,editId,language,IMAGETYPE_ICON).execute();
            ImagesDeleteAllResponse delete3 =
                    edits.images().deleteall(packageName,editId,language,IMAGETYPE_SCREENSHOTS).execute();

            log.info("delete googleplay feture:{}",JSON.toJSONString(delete1));
            log.info("delete googleplay icon:{}",JSON.toJSONString(delete2));
            log.info("delete googleplay screenShots:{}",JSON.toJSONString(delete3));

            for(ImageAndroid imageDO:imageDOList){
                final AbstractInputStreamContent imageFile =
                        new FileContent(imageDO.getImageFormat(), new File(imageDO.getImagePath()));
                Images.Upload uploadRequest = edits
                        .images()
                        .upload(packageName,
                                editId,
                                language,
                                imageDO.getImageUse(),
                                imageFile);
//                futureList.add(ConcurrentUtil.executorService.submit(()->{
                   ImagesUploadResponse imagesUploadResponse = null;
                   try {
                       imagesUploadResponse = uploadRequest.execute();
                       Image image = imagesUploadResponse.getImage();
                       imageDO.setImageReturnUrl(JSON.toJSONString(image));
                       log.info("上传的image信息:{}",imageDO.getImageReturnUrl()  );
                      // return imageDO;
                   } catch (IOException e) {
                       errMsg =
                               String.format("!!!上传的image信息:%s 上传失败：%s",JSON.toJSONString(imageDO),e.getMessage());
                       log.error(errMsg);
                       return errMsg;
                   }
                  //  return null;
//               }));
            }

        } catch (IOException ex) {
            errMsg = String.format("上传图片信息出错,error:%s",ex.getMessage());
            log.error("Excpetion was thrown while uploading apk to alpha track", ex);
        }
        return errMsg;
    }

    /**
     *
     * @param edits
     * @param editId
     * @param language
     * @param futureList
     * @return
     */
    public String updateApkInfo(AndroidPublisher.Edits edits, String editId ,String packageName,
                               String language, GoogleAppListingDO appListingDO, List<Future> futureList ){
            if(edits == null ||editId == null){
                return "";
            }

            if(futureList == null){
                futureList = new ArrayList<>();
            }

            // Update listing for US version of the application.
           final  Listing newUsListing = new Listing();

            newUsListing.setTitle(appListingDO.getTitle())
                    .setFullDescription(appListingDO.getFullDescription())
                    .setShortDescription(appListingDO.getShortDescription())
                    .setVideo(appListingDO.getVideoUrl());

           // futureList.add(ConcurrentUtil.executorService.submit(()-> {
            try {
                AndroidPublisher.Edits.Listings.Update updateUSListingsRequest = edits
                        .listings()
                        .update(packageName, editId, language, newUsListing);
                Listing updatedUsListing = updateUSListingsRequest.execute();
                log.info("Created new US app listing with title: {}", JSON.toJSONString(updatedUsListing));
                return "";
            } catch (IOException ex) {
                log.error("Excpetion was thrown while uploading apk to alpha track", ex);
                return  ex.getMessage();
            }
            //return null;
       // }));
       // return "";
    }



    /**
     * 修改rollout通道 小流量比例
     * @param userFraction  小流量比例(google play用户 使用该版本的百分比)
     * @return
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public JSONObject editApkUserFraction(String applicatinName , String packageName, String trackType,
                                          double userFraction) throws IOException, GeneralSecurityException {
        JSONObject resultJson = JSON.parseObject("{}");

        if(userFraction <0|| userFraction > 0.5d){
            resultJson.put("message","userFraction 要介于0到0.5");
            return resultJson;
        }
        Preconditions.checkArgument(!Strings.isNullOrEmpty(packageName),
                "packageName cannot be null or empty!");

        // Create the API service.
        AndroidPublisher service = AndroidPublisherHelper.init(
                ApplicationConfig.APPLICATION_NAME, ApplicationConfig.SERVICE_ACCOUNT_EMAIL);
        final AndroidPublisher.Edits edits = service.edits();

        // Create a new edit to make changes to your listing.
        AndroidPublisher.Edits.Insert editRequest = edits
                .insert(packageName,
                        null /** no content */);
        AppEdit edit = editRequest.execute();
        final  String editId = edit.getId();


        final Track track = new Track();
        track.setUserFraction(userFraction);
        AndroidPublisher.Edits.Tracks.Patch updateUserFractionRequest =
                edits.tracks().patch(packageName,editId,TRACK_ROLLOUT,new Track().setUserFraction(userFraction));
        updateUserFractionRequest.execute();
        log.info("通道 {},apk 小流量比例被修改为:{}.",trackType,userFraction);

        // Commit changes for edit.
        AndroidPublisher.Edits.Commit commitRequest = edits.commit(packageName, editId);
        AppEdit appEdit = commitRequest.execute();
        log.info(String.format("App edit with id %s has been comitted", appEdit.getId()));

        resultJson = JSON.parseObject(JSON.toJSONString(appEdit));
        return resultJson;
    }


    public Result  updateUserFraction(
            String applicationName,String packageName,Integer versionCode,
            String trackType,double userFraction)  {

        Result result = new Result();
        log.info("***google play更新小流量方法已经执行。。。");
        try {
            initEditsThreadLocal(applicationName,packageName);

            result= updateUserFraction(editsThreadLocal.get(),editIdThreadLocal.get(),applicationName,packageName,
                    versionCode,trackType,userFraction);

            commitEdits(packageName);
            result.setSuccess(true);
        } catch (IOException | GeneralSecurityException e) {
            log.error("***更新出错:{}",e.getMessage());
            result.setSuccess(false);
            result.setMsg("oogle play更新小流量方法错误:"+e.getMessage());
        }
        return result;

    }


    public Result updateUserFraction(AndroidPublisher.Edits edits, String editId ,
                          String applicationName,String packageName,
                          Integer versionCode, String trackType,double userFraction) throws IOException {
        Result result = new Result();
        log.info("***更新小流量方法已经执行。。。");
        final Track track = new Track();
        track.setTrack(trackType);
        track.setVersionCodes(Lists.newArrayList(versionCode));
        //如果是rollout通道 需要设置
        if(trackType.equals(TRACK_ROLLOUT)){
            track.setUserFraction(userFraction);
        }
        //异步 指定到特定渠道
        AndroidPublisher.Edits.Tracks.Update updateTrackRequest = edits
                .tracks()
                .update(packageName,editId,track.getTrack(),track);

//        ConcurrentUtil.executorService.submit(() ->{
            try {
                Track updatedTrack = updateTrackRequest.execute();
                log.info("Track {} has been updated.{}",updatedTrack.getTrack(),JSON.toJSONString(updatedTrack));

                //更新db状态操作

            } catch (IOException e) {
                log.error("***执行更新updateTrackRequest失败， packagename:{},versionCode:{},track:{},userFraction:{}",
                        packageName,versionCode,track,userFraction);
                throw new IllegalArgumentException(String.valueOf(e.getMessage()));
            }
//        }
//        );

        return result;
    }


    public  Map getApkBackupLists(Integer releaseId,String applicatinName ,String packageName ){
        {
            Map resultMap = new HashMap();
            List<ReleaseGoogleLang> langList = new ArrayList<>();
            List<ImageAndroid> imageList = new ArrayList<>();
            resultMap.put("langList",langList);
            resultMap.put("imageList",imageList);
            try {
                initEditsThreadLocal(applicatinName,packageName);

                AndroidPublisher.Edits edits = editsThreadLocal.get();
                String editId = editIdThreadLocal.get();

                log.info(String.format("Created edit with id: %s", editId));

                // Get a list of apks.获取
                  ApksListResponse apksResponse = edits.apks()
                        .list(packageName, editId)
                        .execute();
                // Print the apk info.

                int apkOldVersion = 0;
              for (Apk apk : apksResponse.getApks()) {
                    log.info(JSON.toJSONString(apk));
                    //获取较大值versionCode
                  apkOldVersion = apk.getVersionCode()>apkOldVersion ?apk.getVersionCode():apkOldVersion;
                }
                //获取每种语言 对应的文案信息  拉取到本地
                ListingsListResponse listingsListResponse = edits.listings().list(packageName,editId).execute();
                HashMap<String,ReleaseGoogleLang> hashMap = new HashMap<>();
                for(Listing list: listingsListResponse.getListings()){
                    ReleaseGoogleLang lang =  new ReleaseGoogleLang();
                    lang.setReleaseId(0-releaseId);
                    lang.setLanguage(list.getLanguage());
                    lang.setTitle(list.getTitle());
                    lang.setShortDesc(list.getShortDescription());
                    lang.setFullDesc(list.getFullDescription());
                    lang.setVideoUrl(list.getVideo());
                    hashMap.put(list.getLanguage(),lang);
                    langList.add(lang);
                    log.info(JSON.toJSONString(list));
                }

                //获取apk  每种语言最近更新内容的远程备份
                ApkListingsListResponse apkListingsListResponse = edits.apklistings()
                        .list(packageName,editId,apkOldVersion ).execute();

                if (!CollectionUtils.isEmpty(apkListingsListResponse.getListings())) {
                    for(ApkListing apkListing:apkListingsListResponse.getListings()){
                        ReleaseGoogleLang lang = hashMap.get(apkListing.getLanguage());
                        if(lang == null){
                            lang= new ReleaseGoogleLang();
                            lang.setReleaseId(0-releaseId);
                            lang.setLanguage(apkListing.getLanguage());
                            langList.add(lang);
                        }
                        lang.setRecentChanges(apkListing.getRecentChanges());
                        log.info(JSON.toJSONString(apkListing));
                    }
                }

                for(Map.Entry<String,ReleaseGoogleLang> entry :hashMap.entrySet()) {
                    ImagesListResponse imagesListResponse =
                            edits.images().list(packageName,
                                    editId, entry.getKey(), IMAGETYPE_SCREENSHOTS)
                                    .execute();
                    int x= 0;
                    if(imagesListResponse.getImages() !=null) {
                        for (Image image : imagesListResponse.getImages()) {
                            ImageAndroid imageAndroid = new ImageAndroid();
                            imageAndroid.setReleaseId(0 - releaseId);
                            imageAndroid.setLanguage(entry.getKey());
                            //google  play保存的图片的json信息 包括图片id  图片url
                            imageAndroid.setImageReturnUrl(JSON.toJSONString(image));
                            imageAndroid.setImageUse(IMAGETYPE_SCREENSHOTS);
                            imageAndroid.setOrdinal(x + 1);
                            x++;
                            imageList.add(imageAndroid);
                        }
                    }
                    ImagesListResponse imagesListResponse2 =
                            edits.images().list(packageName,
                                    editId, entry.getKey(), IMAGETYPE_FETUREGRAPIC)
                                    .execute();
                    if(imagesListResponse2.getImages() !=null) {
                        for (Image image : imagesListResponse2.getImages()) {
                            ImageAndroid imageAndroid = new ImageAndroid();
                            imageAndroid.setReleaseId(0 - releaseId);
                            imageAndroid.setLanguage(entry.getKey());
                            imageAndroid.setImageReturnUrl(image.getUrl());
                            imageAndroid.setImageUse(IMAGETYPE_FETUREGRAPIC);
                            imageList.add(imageAndroid);
                        }
                    }

                    ImagesListResponse imagesListResponse3 =
                            edits.images().list(packageName,
                                    editId, entry.getKey(), IMAGETYPE_ICON)
                                    .execute();
                    if(imagesListResponse3.getImages() !=null) {
                        for (Image image : imagesListResponse3.getImages()) {
                            ImageAndroid imageAndroid = new ImageAndroid();
                            imageAndroid.setReleaseId(0 - releaseId);
                            imageAndroid.setLanguage(entry.getKey());
                            imageAndroid.setImageReturnUrl(image.getUrl());
                            imageAndroid.setImageUse(IMAGETYPE_ICON);
                            imageList.add(imageAndroid);
                        }
                    }
                }
            } catch (IOException  | GeneralSecurityException ex) {
                log.error("Excpetion was thrown while backUp  apkListings track", ex);
            }
            return resultMap;
        }
    }

    /**
     * 实际google play 的edit连接初始化 操作
     * @param applicatinName
     * @param packageName
     * @throws IOException
     * @throws GeneralSecurityException
     */
    private  void initEditsThreadLocal(String applicatinName ,String packageName)
            throws IOException, GeneralSecurityException {
        AndroidPublisher.Edits edits = editsThreadLocal.get();
        String editId = editIdThreadLocal.get();
        if(edits == null ||editId == null) {
            Preconditions.checkArgument(!Strings.isNullOrEmpty(packageName),
                    "packageName cannot be null or empty!");

            // Create the API service.
            AndroidPublisher service = AndroidPublisherHelper.init(
                    applicatinName, ApplicationConfig.SERVICE_ACCOUNT_EMAIL);

            edits = service.edits();
            editId = edits.insert(packageName, null /** no content */) .execute().getId();
            log.info(String.format("Created edit with id: %s", editId));
            editsThreadLocal.set(edits);
            editIdThreadLocal.set(editId);

        }
        log.info("***google play 创建Edits 完成！！！");

    }

    /**
     * 实际google play提交操作
     * @param packageName
     * @throws IOException
     */
    private void commitEdits(String packageName) throws IOException {
        AndroidPublisher.Edits edits = editsThreadLocal.get();
        String editId = editIdThreadLocal.get();
        // Commit changes for edit.
        AndroidPublisher.Edits.Commit commitRequest = edits.commit(packageName, editId);
        AppEdit appEdit = commitRequest.execute();
        log.info(String.format("App edit with id %s has been comitted", appEdit.getId()));
        //每次提交完毕  需要置空  防止commit后继续执行失败
        editsThreadLocal.set(null);
        editIdThreadLocal.set(null);
        log.info("***google play 提交Edits 完成！！！");
    }

}

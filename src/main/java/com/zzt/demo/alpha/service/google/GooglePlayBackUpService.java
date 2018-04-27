package com.zzt.demo.alpha.service.google;


import com.zzt.demo.alpha.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GooglePlayBackUpService {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    GooglePlayService googlePlayService;

    /**
     * 备份 Google play  上一个版本的文案
     * @param applicatingName
     * @param packageName
     * @return
     */
    public Result backUpGooglePlayInfos(Integer releaseId, String applicatingName, String packageName){

        Result result = new Result();
        Map map = googlePlayService.getApkBackupLists(releaseId,applicatingName,packageName);


        result.setData(map);
        log.info("***执行google play loadbei备份成功,releaseId:{},applicatingName:{},packageName:{}"
            ,releaseId,applicatingName,packageName);
        return result;
    }

}

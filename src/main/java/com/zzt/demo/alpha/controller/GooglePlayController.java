package com.zzt.demo.alpha.controller;


import com.alibaba.fastjson.JSONObject;
import com.zzt.demo.alpha.common.Result;
import com.zzt.demo.alpha.service.google.GooglePlayBackUpService;
import com.zzt.demo.alpha.service.google.GooglePlayService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class GooglePlayController {

    protected Logger log = LoggerFactory.getLogger(this.getClass());
    static String TIME_FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss";

    @Autowired
    GooglePlayService googlePlayService;


    @Autowired
    GooglePlayBackUpService googlePlayBackUpService;

    @GetMapping("time")
    public JSONObject getNowTime(){

        JSONObject jsonObject = new JSONObject();
        String time = DateFormatUtils.format(new Date(),TIME_FORMAT_DEFAULT);
        jsonObject.put("time",time);
        log.info("当前时间：{}",time);
        return jsonObject;
    }

    @GetMapping("copyWrite/backUp")
    public Result backUpGooglePlayInfos(
            @RequestParam(value = "releaseId",required = false,defaultValue = "1") Integer releaseId,
            @RequestParam(value = "applicationName",required = false,defaultValue = "1") String applicationName,
            @RequestParam(value = "packageName",required = false,defaultValue = "1") String packageName){

        Result result = new Result();
        if(StringUtils.isBlank(applicationName)||StringUtils.isBlank(packageName)){
            String msgf = String.format("传入信息错误，releaseId；%d，applicatingName:%s,packageName:%s"
                    ,releaseId,applicationName,packageName);
            log.info(msgf);
            result.setSuccess(false);
            result.setMsg(msgf);
            return result;
        }

       return googlePlayBackUpService.backUpGooglePlayInfos(releaseId,applicationName,packageName);
    }


}

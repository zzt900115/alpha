package com.zzt.demo.alpha.common;

import java.util.Date;

public class ReleaseGoogle {
    private Integer id;

    private Integer releaseId;

    private String packageName;

    private Integer versionCode;

    private String client;

    private String langUpdate;

    private String track;

    private Double userFraction;

    private Integer apkUploadStatus;

    private Integer textUploadStatus;

    private Integer imageUploadStatus;

    private Integer userFractionStatus;

    private Integer status;

    private String message;

    private Date createTime;

    private Date updateTime;

    public ReleaseGoogle(Integer id, Integer releaseId, String packageName, Integer versionCode, String client, String langUpdate, String track, Double userFraction, Integer apkUploadStatus, Integer textUploadStatus, Integer imageUploadStatus, Integer userFractionStatus, Integer status, String message, Date createTime, Date updateTime) {
        this.id = id;
        this.releaseId = releaseId;
        this.packageName = packageName;
        this.versionCode = versionCode;
        this.client = client;
        this.langUpdate = langUpdate;
        this.track = track;
        this.userFraction = userFraction;
        this.apkUploadStatus = apkUploadStatus;
        this.textUploadStatus = textUploadStatus;
        this.imageUploadStatus = imageUploadStatus;
        this.userFractionStatus = userFractionStatus;
        this.status = status;
        this.message = message;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public ReleaseGoogle() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getReleaseId() {
        return releaseId;
    }

    public void setReleaseId(Integer releaseId) {
        this.releaseId = releaseId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName == null ? null : packageName.trim();
    }

    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client == null ? null : client.trim();
    }

    public String getLangUpdate() {
        return langUpdate;
    }

    public void setLangUpdate(String langUpdate) {
        this.langUpdate = langUpdate == null ? null : langUpdate.trim();
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track == null ? null : track.trim();
    }

    public Double getUserFraction() {
        return userFraction;
    }

    public void setUserFraction(Double userFraction) {
        this.userFraction = userFraction;
    }

    public Integer getApkUploadStatus() {
        return apkUploadStatus;
    }

    public void setApkUploadStatus(Integer apkUploadStatus) {
        this.apkUploadStatus = apkUploadStatus;
    }

    public Integer getTextUploadStatus() {
        return textUploadStatus;
    }

    public void setTextUploadStatus(Integer textUploadStatus) {
        this.textUploadStatus = textUploadStatus;
    }

    public Integer getImageUploadStatus() {
        return imageUploadStatus;
    }

    public void setImageUploadStatus(Integer imageUploadStatus) {
        this.imageUploadStatus = imageUploadStatus;
    }

    public Integer getUserFractionStatus() {
        return userFractionStatus;
    }

    public void setUserFractionStatus(Integer userFractionStatus) {
        this.userFractionStatus = userFractionStatus;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message == null ? null : message.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
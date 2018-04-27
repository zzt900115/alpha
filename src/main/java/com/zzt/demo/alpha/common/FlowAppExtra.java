package com.zzt.demo.alpha.common;

import java.util.Date;

public class FlowAppExtra {
    private Integer id;

    private String clientName;

    private String languages;

    private String jenkinsJobs;

    private String bundleId;

    private String osType;

    private Integer appId;

    private String appPath;

    private Integer omegaAppId;

    private String omegaAppLabel;

    private Integer googlePlaySupport;

    private Date createTime;

    private Date updateTime;

    public FlowAppExtra(Integer id, String clientName, String languages, String jenkinsJobs, String bundleId, String osType, Integer appId, String appPath, Integer omegaAppId, String omegaAppLabel, Integer googlePlaySupport, Date createTime, Date updateTime) {
        this.id = id;
        this.clientName = clientName;
        this.languages = languages;
        this.jenkinsJobs = jenkinsJobs;
        this.bundleId = bundleId;
        this.osType = osType;
        this.appId = appId;
        this.appPath = appPath;
        this.omegaAppId = omegaAppId;
        this.omegaAppLabel = omegaAppLabel;
        this.googlePlaySupport = googlePlaySupport;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public FlowAppExtra() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName == null ? null : clientName.trim();
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages == null ? null : languages.trim();
    }

    public String getJenkinsJobs() {
        return jenkinsJobs;
    }

    public void setJenkinsJobs(String jenkinsJobs) {
        this.jenkinsJobs = jenkinsJobs == null ? null : jenkinsJobs.trim();
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId == null ? null : bundleId.trim();
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType == null ? null : osType.trim();
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getAppPath() {
        return appPath;
    }

    public void setAppPath(String appPath) {
        this.appPath = appPath == null ? null : appPath.trim();
    }

    public Integer getOmegaAppId() {
        return omegaAppId;
    }

    public void setOmegaAppId(Integer omegaAppId) {
        this.omegaAppId = omegaAppId;
    }

    public String getOmegaAppLabel() {
        return omegaAppLabel;
    }

    public void setOmegaAppLabel(String omegaAppLabel) {
        this.omegaAppLabel = omegaAppLabel == null ? null : omegaAppLabel.trim();
    }

    public Integer getGooglePlaySupport() {
        return googlePlaySupport;
    }

    public void setGooglePlaySupport(Integer googlePlaySupport) {
        this.googlePlaySupport = googlePlaySupport;
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
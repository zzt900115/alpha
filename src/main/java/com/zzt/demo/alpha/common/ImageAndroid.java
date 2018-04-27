package com.zzt.demo.alpha.common;

import java.util.Date;

public class ImageAndroid {
    private Integer id;

    private Integer releaseId;

    private String language;

    private Integer ordinal;

    private String imageName;

    private String imageUse;

    private String imageFormat;

    private String imagePath;

    private String imageReturnUrl;

    private Integer status;

    private Date updatetime;

    public ImageAndroid(Integer id, Integer releaseId, String language, Integer ordinal, String imageName, String imageUse, String imageFormat, String imagePath, String imageReturnUrl, Integer status, Date updatetime) {
        this.id = id;
        this.releaseId = releaseId;
        this.language = language;
        this.ordinal = ordinal;
        this.imageName = imageName;
        this.imageUse = imageUse;
        this.imageFormat = imageFormat;
        this.imagePath = imagePath;
        this.imageReturnUrl = imageReturnUrl;
        this.status = status;
        this.updatetime = updatetime;
    }

    public ImageAndroid() {
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language == null ? null : language.trim();
    }

    public Integer getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName == null ? null : imageName.trim();
    }

    public String getImageUse() {
        return imageUse;
    }

    public void setImageUse(String imageUse) {
        this.imageUse = imageUse == null ? null : imageUse.trim();
    }

    public String getImageFormat() {
        return imageFormat;
    }

    public void setImageFormat(String imageFormat) {
        this.imageFormat = imageFormat == null ? null : imageFormat.trim();
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath == null ? null : imagePath.trim();
    }

    public String getImageReturnUrl() {
        return imageReturnUrl;
    }

    public void setImageReturnUrl(String imageReturnUrl) {
        this.imageReturnUrl = imageReturnUrl == null ? null : imageReturnUrl.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}
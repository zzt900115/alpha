package com.zzt.demo.alpha.common;

import java.util.List;

/**
 * google play 上不同语言的描述信息
 * 包括标题 简述  详细描述  video地址
 * 以及所有的图片不同语言对应的图标 截图  置顶图等
 *
 */
public class GoogleAppListingDO {

    private Integer releaseId;

    private String language;

    private String title;

    private String shortDescription;

    private String fullDescription;

    private String videoUrl;

    private List<ImageAndroid> imageDOList;

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
        this.language = language;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public List<ImageAndroid> getImageDOList() {
        return imageDOList;
    }

    public void setImageDOList(List<ImageAndroid> imageDOList) {
        this.imageDOList = imageDOList;
    }
}

package com.zzt.demo.alpha.common;

public class ReleaseGoogleLang {
    private Integer id;

    private Integer releaseId;

    private String language;

    private String recentChanges;

    private String title;

    private String shortDesc;

    private String fullDesc;

    private String imageList;

    private String videoUrl;

    public ReleaseGoogleLang(Integer id, Integer releaseId, String language, String recentChanges,
                             String title, String shortDesc, String fullDesc, String imageList, String videoUrl) {
        this.id = id;
        this.releaseId = releaseId;
        this.language = language;
        this.recentChanges = recentChanges;
        this.title = title;
        this.shortDesc = shortDesc;
        this.fullDesc = fullDesc;
        this.imageList = imageList;
        this.videoUrl = videoUrl;
    }

    public ReleaseGoogleLang() {
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

    public String getRecentChanges() {
        return recentChanges;
    }

    public void setRecentChanges(String recentChanges) {
        this.recentChanges = recentChanges == null ? null : recentChanges.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc == null ? null : shortDesc.trim();
    }

    public String getFullDesc() {
        return fullDesc;
    }

    public void setFullDesc(String fullDesc) {
        this.fullDesc = fullDesc == null ? null : fullDesc.trim();
    }

    public String getImageList() {
        return imageList;
    }

    public void setImageList(String imageList) {
        this.imageList = imageList == null ? null : imageList.trim();
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl == null ? null : videoUrl.trim();
    }
}
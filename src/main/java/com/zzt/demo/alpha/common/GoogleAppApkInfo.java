package com.zzt.demo.alpha.common;

public class GoogleAppApkInfo {

    private Integer releaseId;

    private String packageName;

    private String client;

    private String track;

    private double userFraction;

    private GoogleAppChangesDO[] recentChangesArray;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public Integer getReleaseId() {
        return releaseId;
    }

    public void setReleaseId(Integer releaseId) {
        this.releaseId = releaseId;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public double getUserFraction() {
        return userFraction;
    }

    public void setUserFraction(double userFraction) {
        this.userFraction = userFraction;
    }

    public GoogleAppChangesDO[] getRecentChangesArray() {
        return recentChangesArray;
    }

    public void setRecentChangesArray(GoogleAppChangesDO[] recentChangesArray) {
        this.recentChangesArray = recentChangesArray;
    }
}

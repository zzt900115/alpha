package com.zzt.demo.alpha.common;
/**
 * google play 上不同语言的描述信息
 * 所有图片不同语言对应的图标 截图  置顶图等
 *
 */
public class GoogleAppImageDO {

    private Integer num;

    private String imageName;   //图片名

    private String imageFilePath;   //图片路径

    private String imageFormat;      //图片格式jpg  png

    private String imageUse;    //图片用途  突变  置顶图  截图等

    private String language;    //图片 对应的语言

    private String imageReturnUrl; //上传后的地址

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }

    public String getImageFormat() {
        return imageFormat;
    }

    public void setImageFormat(String imageFormat) {
        this.imageFormat = imageFormat;
    }

    public String getImageUse() {
        return imageUse;
    }

    public void setImageUse(String imageUse) {
        this.imageUse = imageUse;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getImageReturnUrl() {
        return imageReturnUrl;
    }

    public void setImageReturnUrl(String imageReturnUrl) {
        this.imageReturnUrl = imageReturnUrl;
    }
}

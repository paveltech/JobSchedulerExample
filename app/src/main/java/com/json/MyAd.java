package com.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by lolipop on 4/9/2018.
 */

public class MyAd {


    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("app_name")
    @Expose
    private String appName;
    @SerializedName("app_image_link")
    @Expose
    private String appImageLink;
    @SerializedName("app_doc")
    @Expose
    private String appDoc;
    @SerializedName("app_package_name")
    @Expose
    private String appPackageName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppImageLink() {
        return appImageLink;
    }

    public void setAppImageLink(String appImageLink) {
        this.appImageLink = appImageLink;
    }

    public String getAppDoc() {
        return appDoc;
    }

    public void setAppDoc(String appDoc) {
        this.appDoc = appDoc;
    }

    public String getAppPackageName() {
        return appPackageName;
    }

    public void setAppPackageName(String appPackageName) {
        this.appPackageName = appPackageName;
    }
}

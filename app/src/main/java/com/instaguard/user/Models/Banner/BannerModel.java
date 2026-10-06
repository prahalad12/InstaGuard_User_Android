package com.instaguard.user.Models.Banner;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BannerModel {

    @SerializedName("data")
    @Expose
    private List<BannerData> data = null;
    @SerializedName("message")
    @Expose
    private String message;

    public List<BannerData> getData() {
        return data;
    }

    public void setData(List<BannerData> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
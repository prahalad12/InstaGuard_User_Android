package com.instaguard.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AppSupportPagesModel {

    @SerializedName("data")
    @Expose
    private List<AppSupportPagesData> data = null;
    @SerializedName("message")
    @Expose
    private String message;

    public List<AppSupportPagesData> getData() {
        return data;
    }

    public void setData(List<AppSupportPagesData> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
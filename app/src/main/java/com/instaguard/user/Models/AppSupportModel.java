package com.instaguard.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AppSupportModel {

    @SerializedName("data")
    @Expose
    private List<AppSupportDataModel> AppSupportDataModel = null;
    @SerializedName("message")
    @Expose
    private String message;

    public List<AppSupportDataModel> getAppSupportDataModel() {
        return AppSupportDataModel;
    }

    public void setData(List<AppSupportDataModel> AppSupportDataModel) {
        this.AppSupportDataModel = AppSupportDataModel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
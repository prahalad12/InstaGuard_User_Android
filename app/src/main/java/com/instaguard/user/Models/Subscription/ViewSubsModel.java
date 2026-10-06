package com.instaguard.user.Models.Subscription;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ViewSubsModel {

    @SerializedName("result")
    @Expose
    private ViewSubsData result;
    @SerializedName("message")
    @Expose
    private String message;

    public ViewSubsData getResult() {
        return result;
    }

    public void setResult(ViewSubsData result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
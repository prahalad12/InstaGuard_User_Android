package com.instaguard.user.Models.Subscription;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class UpdateSubsModel {

    @SerializedName("result")
    @Expose
    private UpdateSubsData result;
    @SerializedName("message")
    @Expose
    private String message;

    public UpdateSubsData getResult() {
        return result;
    }

    public void setResult(UpdateSubsData result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
package com.instaguard.user.Models.Subscription;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CheckPlanModel {

    @SerializedName("result")
    @Expose
    private CheckPlanData result;
    @SerializedName("message")
    @Expose
    private String message;

    public CheckPlanData getResult() {
        return result;
    }

    public void setResult(CheckPlanData result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
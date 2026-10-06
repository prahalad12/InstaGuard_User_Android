package com.instaguard.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyOtpModel {

    @SerializedName("result")
    @Expose
    private VerifyOtpData result;
    @SerializedName("message")
    @Expose
    private String message;

    public VerifyOtpData getResult() {
        return result;
    }

    public void setResult(VerifyOtpData result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
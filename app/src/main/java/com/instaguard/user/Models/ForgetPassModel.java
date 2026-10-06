package com.instaguard.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgetPassModel {

    @SerializedName("result")
    @Expose
    private ForgetPassData result;
    @SerializedName("message")
    @Expose
    private String message;

    public ForgetPassData getResult() {
        return result;
    }

    public void setResult(ForgetPassData result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
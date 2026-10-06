package com.instaguard.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckPromoModel {

    @SerializedName("result")
    @Expose
    private CheckPromoData result;
    @SerializedName("message")
    @Expose
    private String message;

    public CheckPromoData getResult() {
        return result;
    }

    public void setResult(CheckPromoData result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
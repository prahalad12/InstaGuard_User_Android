package com.instaguard.user.Models.Subscription;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class BuyPlanWebHookModel {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("result")
    @Expose
    private BuyPlanWebHookData result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BuyPlanWebHookData getResult() {
        return result;
    }

    public void setResult(BuyPlanWebHookData result) {
        this.result = result;
    }

}
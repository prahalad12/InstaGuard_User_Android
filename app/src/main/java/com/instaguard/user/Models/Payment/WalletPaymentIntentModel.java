package com.instaguard.user.Models.Payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletPaymentIntentModel {

    @SerializedName("result")
    @Expose
    private WalletPaymentIntentResult result = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("client_secret")
    @Expose
    private String client_secret;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public WalletPaymentIntentResult getResult() {
        return result;
    }

    public void setResult(WalletPaymentIntentResult result) {
        this.result = result;
    }
}

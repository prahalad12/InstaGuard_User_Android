package com.instaguard.user.Models.Payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletPaymentIntentResult {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("client_secret")
    @Expose
    private String client_secret;


    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

package com.instaguard.user.Models.Payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SaveWalletPaymentIntentModel {

    /* "message": "success",
             "client_secret": "pi_3NVwYVAf6IJVLww31zOvq9Dt_secret_hFfryT6PcMgbpSufInIsBrSe8",
             "payment_intent_id": "pi_3NVwYVAf6IJVLww31zOvq9Dt",
             "result":

               "action_url": "https://hooks.stripe.com/redirect/authenticate/src_1NVwYVAf6IJVLww3w6AEylwo?client_secret=src_client_secret_nAfG0XCi41hZzeeFBJw8pwdm&source_redirect_slug=test_YWNjdF8xTHgxeEZBZjZJSlZMd3czLF9PSVhkR09OWDJJZE1PV2hUeWx0VjZxclpIS245dmJu0100hGX6rSPc",
    "action_url_status": 1
    */

    @SerializedName("result")
    @Expose
    private SaveWalletPaymentIntentResult result = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("client_secret")
    @Expose
    private String client_secret;
    @SerializedName("payment_intent_id")
    @Expose
    private String payment_intent_id;
    @SerializedName("action_url")
    @Expose
    private String action_url;
    @SerializedName("action_url_status")
    @Expose
    private String action_url_status;
    @SerializedName("wallet_status")
    @Expose
    private String wallet_status;

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

    public SaveWalletPaymentIntentResult getResult() {
        return result;
    }

    public void setResult(SaveWalletPaymentIntentResult result) {
        this.result = result;
    }

    public String getPayment_intent_id() {
        return payment_intent_id;
    }

    public void setPayment_intent_id(String payment_intent_id) {
        this.payment_intent_id = payment_intent_id;
    }

    public String getAction_url() {
        return action_url;
    }

    public void setAction_url(String action_url) {
        this.action_url = action_url;
    }

    public String getAction_url_status() {
        return action_url_status;
    }

    public void setAction_url_status(String action_url_status) {
        this.action_url_status = action_url_status;
    }

    public String getWallet_status() {
        return wallet_status;
    }

    public void setWallet_status(String wallet_status) {
        this.wallet_status = wallet_status;
    }
}

package com.instaguard.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by system7 on 10/11/2017.
 */

public class BalanceWalletAddMoneyModel {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("service_request")
    @Expose
    private BalanceWalletAddMonetDataModel BalanceWalletAddMonetDataModel;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public BalanceWalletAddMonetDataModel getBalanceWalletAddMonetDataModel() {
        return BalanceWalletAddMonetDataModel;
    }

    public void setBalanceWalletAddMonetDataModel(BalanceWalletAddMonetDataModel BalanceWalletAddMonetDataModel) {
        this.BalanceWalletAddMonetDataModel = BalanceWalletAddMonetDataModel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
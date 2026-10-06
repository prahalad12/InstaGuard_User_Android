package com.instaguard.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by system7 on 10/11/2017.
 */

public class WalletAddMoneyModel {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("service_request")
    @Expose
    private WalletAddMonetDataModel WalletAddMonetDataModel;
    @SerializedName("wallet_balance")
    @Expose
    private Double walletBalance;
    @SerializedName("balance_amount")
    @Expose
    private Double balanceAmount;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public WalletAddMonetDataModel getWalletAddMonetDataModel() {
        return WalletAddMonetDataModel;
    }

    public void setWalletAddMonetDataModel(WalletAddMonetDataModel WalletAddMonetDataModel) {
        this.WalletAddMonetDataModel = WalletAddMonetDataModel;
    }

    public Double getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(Double walletBalance) {
        this.walletBalance = walletBalance;
    }

    public Double getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(Double balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
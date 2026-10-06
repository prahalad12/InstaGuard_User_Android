package com.instaguard.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by system7 on 10/11/2017.
 */

public class WalletAllModel {

    @SerializedName("data")
    @Expose
    private List<WalletAllData> WalletAllData = null;
    @SerializedName("message")
    @Expose
    private String message;

    public List<WalletAllData> getWalletAllData() {
        return WalletAllData;
    }

    public void setWalletAllData(List<WalletAllData> WalletAllData) {
        this.WalletAllData = WalletAllData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
package com.instaguard.user.Models.Bank;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BankModel {

    @SerializedName("bank_details")
    @Expose
    private BankData bankDetails;
    @SerializedName("message")
    @Expose
    private String message;

    public BankData getBankDetails() {
        return bankDetails;
    }

    public void setBankDetails(BankData bankDetails) {
        this.bankDetails = bankDetails;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
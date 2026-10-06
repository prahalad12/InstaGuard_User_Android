package com.instaguard.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EmailMobileCheckingModel{

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("email_available")
    @Expose
    private Boolean emailAvailable;
    @SerializedName("phone_number_available")
    @Expose
    private Boolean phoneNumberAvailable;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getEmailAvailable() {
        return emailAvailable;
    }

    public void setEmailAvailable(Boolean emailAvailable) {
        this.emailAvailable = emailAvailable;
    }

    public Boolean getPhoneNumberAvailable() {
        return phoneNumberAvailable;
    }

    public void setPhoneNumberAvailable(Boolean phoneNumberAvailable) {
        this.phoneNumberAvailable = phoneNumberAvailable;
    }

}
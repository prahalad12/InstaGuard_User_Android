package com.instaguard.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by system7 on 10/10/2017.
 */

public class RegisterModel {

    @SerializedName("result")
    @Expose
    private RegisterData RegisterData;
    @SerializedName("message")
    @Expose
    private String message;

    public RegisterData getRegisterData() {
        return RegisterData;
    }

    public void setRegisterData(RegisterData RegisterData) {
        this.RegisterData = RegisterData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

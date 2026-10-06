package com.instaguard.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sikka on 01/12/2021.
 */

public class LoginNewModel {

    @SerializedName("result")
    @Expose
    private LoginModel loginModel;
    @SerializedName("message")
    @Expose
    private String message;

    public LoginModel getLoginModel() {
        return loginModel;
    }

    public void setRegisterData(LoginModel loginModel) {
        this.loginModel = loginModel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

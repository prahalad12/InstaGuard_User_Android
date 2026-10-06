package com.instaguard.user.Models.SocialLogModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by uplogic on 18/04/2018.
 */

public class SocialLoginModel {

    @SerializedName("msg")
    @Expose
    private String message;
    @SerializedName("result")
    @Expose
    private SocialLoginDataModel SocialLoginDataModel;
    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("token_type")
    @Expose
    private String tokenType;
    @SerializedName("expires_in")
    @Expose
    private Integer expiresIn;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SocialLoginDataModel getSocialLoginDataModel() {
        return SocialLoginDataModel;
    }

    public void setSocialLoginDataModel(SocialLoginDataModel SocialLoginDataModel) {
        this.SocialLoginDataModel = SocialLoginDataModel;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

}
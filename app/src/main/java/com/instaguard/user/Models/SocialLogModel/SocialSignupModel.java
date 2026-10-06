package com.instaguard.user.Models.SocialLogModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SocialSignupModel {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("result")
    @Expose
    private SocialSignupData result;
    @SerializedName("msg")
    @Expose
    private String msg;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SocialSignupData getResult() {
        return result;
    }

    public void setResult(SocialSignupData result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
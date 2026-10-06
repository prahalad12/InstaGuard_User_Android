package com.instaguard.user.Models.Subscription;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class SubsHistoryModel {

    @SerializedName("data")
    @Expose
    private List<SubsHistoryData> data = null;
    @SerializedName("message")
    @Expose
    private String message;

    public List<SubsHistoryData> getData() {
        return data;
    }

    public void setData(List<SubsHistoryData> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
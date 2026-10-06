package com.instaguard.user.Models.Subscription;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ListViewSubsModel {

    @SerializedName("result")
    @Expose
    private List<ListViewSubsData> result = null;
    @SerializedName("message")
    @Expose
    private String message;

    public List<ListViewSubsData> getResult() {
        return result;
    }

    public void setResult(List<ListViewSubsData> result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
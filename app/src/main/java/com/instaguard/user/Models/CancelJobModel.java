package com.instaguard.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by system7 on 10/11/2017.
 */

public class CancelJobModel {

    @SerializedName("data")
    @Expose
    private CancelJobDataModel CancelJobDataModel;
    @SerializedName("message")
    @Expose
    private String message;

    public CancelJobDataModel getCancelJobDataModel() {
        return CancelJobDataModel;
    }

    public void setData(CancelJobDataModel CancelJobDataModel) {
        this.CancelJobDataModel = CancelJobDataModel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
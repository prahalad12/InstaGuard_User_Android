package com.instaguard.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PanicAddAdminModel {

    @SerializedName("response")
    @Expose
    private PanicAddAdminDataModel PanicAddAdminDataModel;
    @SerializedName("message")
    @Expose
    private String message;

    public PanicAddAdminDataModel getPanicAddAdminDataModel() {
        return PanicAddAdminDataModel;
    }

    public void setPanicAddAdminDataModel(PanicAddAdminDataModel PanicAddAdminDataModel) {
        this.PanicAddAdminDataModel = PanicAddAdminDataModel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
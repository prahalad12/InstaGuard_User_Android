package com.instaguard.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by system7 on 10/11/2017.
 */

public class EmergencyViewModel{

    @SerializedName("data")
    @Expose
    private List<EmergencyViewDataModel> EmergencyViewDataModel = null;
    @SerializedName("message")
    @Expose
    private String message;

    public List<EmergencyViewDataModel> getEmergencyViewDataModel() {
        return EmergencyViewDataModel;
    }

    public void setEmergencyViewDataModel(List<EmergencyViewDataModel> EmergencyViewDataModel) {
        this.EmergencyViewDataModel = EmergencyViewDataModel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
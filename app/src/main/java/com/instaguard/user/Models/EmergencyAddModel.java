package com.instaguard.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by system7 on 10/11/2017.
 */

public class EmergencyAddModel {

    @SerializedName("result")
    @Expose
    private EmergencyAddDataModel EmergencyAddDataModel;
    @SerializedName("message")
    @Expose
    private String message;

    public EmergencyAddDataModel getEmergencyAddDataModel() {
        return EmergencyAddDataModel;
    }

    public void setEmergencyAddDataModel(EmergencyAddDataModel EmergencyAddDataModel) {
        this.EmergencyAddDataModel = EmergencyAddDataModel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
package com.instaguard.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VehicleCategoryModel {

    @SerializedName("data")
    @Expose
    private List<VehicleCategoryData> VehicleCategoryData = null;
    @SerializedName("message")
    @Expose
    private String message;

    public List<VehicleCategoryData> getVehicleCategoryData() {
        return VehicleCategoryData;
    }

    public void setVehicleCategoryData(List<VehicleCategoryData> VehicleCategoryData) {
        this.VehicleCategoryData = VehicleCategoryData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
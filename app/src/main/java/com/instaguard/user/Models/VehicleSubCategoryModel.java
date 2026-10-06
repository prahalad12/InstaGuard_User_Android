package com.instaguard.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VehicleSubCategoryModel {

    @SerializedName("data")
    @Expose
    private List<VehicleSubCategoryData> VehicleSubCategoryData = null;
    @SerializedName("message")
    @Expose
    private String message;

    public List<VehicleSubCategoryData> getVehicleSubCategoryData() {
        return VehicleSubCategoryData;
    }

    public void setVehicleSubCategoryData(List<VehicleSubCategoryData> VehicleCategoryData) {
        this.VehicleSubCategoryData = VehicleSubCategoryData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
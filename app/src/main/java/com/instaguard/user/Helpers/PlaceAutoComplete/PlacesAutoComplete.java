package com.instaguard.user.Helpers.PlaceAutoComplete;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlacesAutoComplete {

    @SerializedName("predictions")
    @Expose
    private List<Prediction> predictions = null;
    @SerializedName("status")
    @Expose
    private String status;

    public PlacesAutoComplete(String status) {
        this.status = status;
    }

    public List<Prediction> getPredictions() {
        return predictions;
    }

    public String getStatus() {
        return status;
    }

}
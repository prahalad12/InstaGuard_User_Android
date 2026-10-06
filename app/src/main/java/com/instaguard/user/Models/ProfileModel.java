package com.instaguard.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by system7 on 10/11/2017.
 */

public class ProfileModel {

    @SerializedName("data")
    @Expose
    private ProfileData ProfileData;
    @SerializedName("rating")
    @Expose
    private Double rating;
    @SerializedName("message")
    @Expose
    private String message;

    public ProfileData getProfileData() {
        return ProfileData;
    }

    public void setProfileData(ProfileData ProfileData) {
        this.ProfileData = ProfileData;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
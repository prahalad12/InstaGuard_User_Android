package com.instaguard.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by system7 on 10/11/2017.
 */

public class ProfileUpdateModel{

    @SerializedName("data")
    @Expose
    private ProfileUpdateData ProfileUpdateData;
    @SerializedName("message")
    @Expose
    private String message;

    public ProfileUpdateData getProfileUpdateData() {
        return ProfileUpdateData;
    }

    public void setProfileUpdateData(ProfileUpdateData ProfileUpdateData) {
        this.ProfileUpdateData = ProfileUpdateData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
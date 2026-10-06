package com.instaguard.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Uplogic on 27/04/2018.
 */

public class ProfilePicModel{

    @SerializedName("data")
    @Expose
    private ProfilePicData ProfilePicData;
    @SerializedName("message")
    @Expose
    private String message;

    public ProfilePicData getProfilePicData() {
        return ProfilePicData;
    }

    public void setData(ProfilePicData ProfilePicData) {
        this.ProfilePicData = ProfilePicData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
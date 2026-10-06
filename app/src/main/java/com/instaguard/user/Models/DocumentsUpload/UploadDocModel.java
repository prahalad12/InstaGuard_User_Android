package com.instaguard.user.Models.DocumentsUpload;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Uplogic on 27/04/2018.
 */

public class UploadDocModel {

    @SerializedName("result")
    @Expose
    private UploadDocData UploadDocData;
    @SerializedName("message")
    @Expose
    private String message;

    public UploadDocData getUploadDocData() {
        return UploadDocData;
    }

    public void setData(UploadDocData UploadDocData) {
        this.UploadDocData = UploadDocData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
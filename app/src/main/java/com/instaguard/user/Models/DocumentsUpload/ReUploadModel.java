package com.instaguard.user.Models.DocumentsUpload;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by system7 on 11/6/2017.
 */

public class ReUploadModel {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("result")
    @Expose
    private ReUploadData result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ReUploadData getResult() {
        return result;
    }

    public void setResult(ReUploadData result) {
        this.result = result;
    }

}
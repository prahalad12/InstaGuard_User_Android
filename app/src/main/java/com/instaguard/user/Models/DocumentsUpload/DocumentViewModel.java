package com.instaguard.user.Models.DocumentsUpload;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by system7 on 11/6/2017.
 */

public class DocumentViewModel {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("result")
    @Expose
   // private List<DocumentViewData> result = null;
    private List<DocumentList> result = null;
    @SerializedName("reject_status")
    @Expose
    private Integer rejectStatus;

    public Integer getRejectStatus() {
        return rejectStatus;
    }

    public void setRejectStatus(Integer rejectStatus) {
        this.rejectStatus = rejectStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DocumentList> getResult() {
        return result;
    }

    public void setResult(List<DocumentList> result) {
        this.result = result;
    }

}
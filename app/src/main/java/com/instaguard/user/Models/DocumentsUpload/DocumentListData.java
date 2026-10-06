package com.instaguard.user.Models.DocumentsUpload;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DocumentListData {

    @SerializedName("data")
    @Expose
    private List<DocumentList> data = null;
    @SerializedName("message")
    @Expose
    private String message;

    public List<DocumentList> getData() {
        return data;
    }

    public void setData(List<DocumentList> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

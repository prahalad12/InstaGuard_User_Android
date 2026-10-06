package com.instaguard.user.Models.DocumentsUpload;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by system7 on 11/6/2017.
 */

public class ReUploadData {

    @SerializedName("driver_id")
    @Expose
    private String driverId;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("document_id")
    @Expose
    private String documentId;
    @SerializedName("document")
    @Expose
    private String document;

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

}
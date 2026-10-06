package com.instaguard.user.Models.DocumentsUpload;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by system7 on 11/6/2017.
 */

public class DocumentViewData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("car_id")
    @Expose
    private Integer carId;
    @SerializedName("driver_id")
    @Expose
    private Integer driverId;
    @SerializedName("document")
    @Expose
    private String document;
    @SerializedName("document_name")
    @Expose
    private String documentName;
    @SerializedName("document_proof")
    @Expose
    private String documentProof;
    @SerializedName("approved_status")
    @Expose
    private Integer approvedStatus;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("reason")
    @Expose
    private String reason;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentProof() {
        return documentProof;
    }

    public void setDocumentProof(String documentProof) {
        this.documentProof = documentProof;
    }

    public Integer getApprovedStatus() {
        return approvedStatus;
    }

    public void setApprovedStatus(Integer approvedStatus) {
        this.approvedStatus = approvedStatus;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
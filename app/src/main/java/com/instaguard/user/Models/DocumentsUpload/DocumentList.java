package com.instaguard.user.Models.DocumentsUpload;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DocumentList {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("document_name")
    @Expose
    private String documentName;
    @SerializedName("status")
    @Expose
    private Integer status;

    @SerializedName("country_id")
    @Expose
    private Integer countryId;
    @SerializedName("expire_on")
    @Expose
    private Object expireOn;
    @SerializedName("document_name_ar")
    @Expose
    private String documentNameAr;

    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @SerializedName("car_id")
    @Expose
    private Integer carId;
    @SerializedName("driver_id")
    @Expose
    private Integer driverId;
    @SerializedName("document")
    @Expose
    private String document;

    @SerializedName("document_proof")
    @Expose
    private String documentProof;
    @SerializedName("approved_status")
    @Expose
    private Integer approvedStatus;
    @SerializedName("expiry_date")
    @Expose
    private String expiryDate;
    @SerializedName("expiry_status")
    @Expose
    private Integer expiryStatus;
    @SerializedName("reason")
    @Expose
    private String reason;


    @SerializedName("document_priority")
    @Expose
    private Object documentPriority;

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

    public Integer getExpiryStatus() {
        return expiryStatus;
    }

    public void setExpiryStatus(Integer expiryStatus) {
        this.expiryStatus = expiryStatus;
    }
    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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


    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public Object getExpireOn() {
        return expireOn;
    }

    public void setExpireOn(Object expireOn) {
        this.expireOn = expireOn;
    }

    public String getDocumentNameAr() {
        return documentNameAr;
    }

    public void setDocumentNameAr(String documentNameAr) {
        this.documentNameAr = documentNameAr;
    }

    public Object getDocumentPriority() {
        return documentPriority;
    }

    public void setDocumentPriority(Object documentPriority) {
        this.documentPriority = documentPriority;
    }
}

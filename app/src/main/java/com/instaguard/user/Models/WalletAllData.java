package com.instaguard.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by system7 on 10/11/2017.
 */

public class WalletAllData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("transaction_token")
    @Expose
    private Integer transactionToken;
    @SerializedName("customer_id")
    @Expose
    private Integer customerId;
    @SerializedName("driver_id")
    @Expose
    private Object driverId;
    @SerializedName("transaction_type")
    @Expose
    private String transactionType;
    @SerializedName("transaction_date")
    @Expose
    private String transactionDate;
    @SerializedName("transaction_amount")
    @Expose
    private Double transactionAmount;
    @SerializedName("transaction_mode")
    @Expose
    private String transactionMode;
    @SerializedName("card_number")
    @Expose
    private Object cardNumber;
    @SerializedName("transaction_notes")
    @Expose
    private String transactionNotes;
    @SerializedName("transaction_notes_ar")
    @Expose
    private String transactionNotesAr;
    @SerializedName("transaction_status")
    @Expose
    private Integer transactionStatus;
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

    public Integer getTransactionToken() {
        return transactionToken;
    }

    public void setTransactionToken(Integer transactionToken) {
        this.transactionToken = transactionToken;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Object getDriverId() {
        return driverId;
    }

    public void setDriverId(Object driverId) {
        this.driverId = driverId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(Double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getTransactionMode() {
        return transactionMode;
    }

    public void setTransactionMode(String transactionMode) {
        this.transactionMode = transactionMode;
    }

    public Object getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(Object cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getTransactionNotes() {
        return transactionNotes;
    }

    public void setTransactionNotes(String transactionNotes) {
        this.transactionNotes = transactionNotes;
    }

    public Integer getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(Integer transactionStatus) {
        this.transactionStatus = transactionStatus;
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

    public String getTransactionNotesAr() {
        return transactionNotesAr;
    }

    public void setTransactionNotesAr(String transactionNotesAr) {
        this.transactionNotesAr = transactionNotesAr;
    }

}
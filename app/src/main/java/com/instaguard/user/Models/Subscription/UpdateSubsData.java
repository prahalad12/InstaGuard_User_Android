package com.instaguard.user.Models.Subscription;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class UpdateSubsData {

    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("cus_id")
    @Expose
    private String cusId;
    @SerializedName("plan_id")
    @Expose
    private Integer planId;
    @SerializedName("purchase_date")
    @Expose
    private Object purchaseDate;
    @SerializedName("expiry_date")
    @Expose
    private Object expiryDate;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("subscription_name")
    @Expose
    private String subscriptionName;
    @SerializedName("subscription_amount")
    @Expose
    private Object subscriptionAmount;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public Object getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Object purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Object getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubscriptionName() {
        return subscriptionName;
    }

    public void setSubscriptionName(String subscriptionName) {
        this.subscriptionName = subscriptionName;
    }

    public Object getSubscriptionAmount() {
        return subscriptionAmount;
    }

    public void setSubscriptionAmount(Object subscriptionAmount) {
        this.subscriptionAmount = subscriptionAmount;
    }

}
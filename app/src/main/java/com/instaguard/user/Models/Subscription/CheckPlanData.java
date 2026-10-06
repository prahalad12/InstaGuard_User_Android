package com.instaguard.user.Models.Subscription;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CheckPlanData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("cus_id")
    @Expose
    private Integer cusId;
    @SerializedName("plan_id")
    @Expose
    private Integer planId;
    @SerializedName("plan_name")
    @Expose
    private String planName;
    @SerializedName("subscription_description")
    @Expose
    private String subscriptionDescription;
    @SerializedName("period_type")
    @Expose
    private Integer periodType;
    @SerializedName("period")
    @Expose
    private Integer period;
    @SerializedName("purchase_date")
    @Expose
    private String purchaseDate;
    @SerializedName("subscription_code")
    @Expose
    private String subscriptionCode;
    @SerializedName("expiry_date")
    @Expose
    private String expiryDate;
    @SerializedName("plan_period")
    @Expose
    private String planPeriod;
    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("maximum_discount_amount")
    @Expose
    private Integer maximumDiscountAmount;
    @SerializedName("tow_service_id")
    @Expose
    private String towServiceId;
    @SerializedName("appointment_service_id")
    @Expose
    private String appointmentServiceId;
    @SerializedName("status")
    @Expose
    private Integer status;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getCusId() {
        return cusId;
    }

    public void setCusId(Integer cusId) {
        this.cusId = cusId;
    }

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getSubscriptionDescription() {
        return subscriptionDescription;
    }

    public void setSubscriptionDescription(String subscriptionDescription) {
        this.subscriptionDescription = subscriptionDescription;
    }

    public Integer getPeriodType() {
        return periodType;
    }

    public void setPeriodType(Integer periodType) {
        this.periodType = periodType;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getSubscriptionCode() {
        return subscriptionCode;
    }

    public void setSubscriptionCode(String subscriptionCode) {
        this.subscriptionCode = subscriptionCode;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getPlanPeriod() {
        return planPeriod;
    }

    public void setPlanPeriod(String planPeriod) {
        this.planPeriod = planPeriod;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getMaximumDiscountAmount() {
        return maximumDiscountAmount;
    }

    public void setMaximumDiscountAmount(Integer maximumDiscountAmount) {
        this.maximumDiscountAmount = maximumDiscountAmount;
    }

    public String getTowServiceId() {
        return towServiceId;
    }

    public void setTowServiceId(String towServiceId) {
        this.towServiceId = towServiceId;
    }

    public String getAppointmentServiceId() {
        return appointmentServiceId;
    }

    public void setAppointmentServiceId(String appointmentServiceId) {
        this.appointmentServiceId = appointmentServiceId;
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

}
package com.instaguard.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckPromoData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("coupon_code")
    @Expose
    private String couponCode;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("description_french")
    @Expose
    private String descriptionFrench;
    @SerializedName("description_spanish")
    @Expose
    private String descriptionSpanish;
    @SerializedName("description_swati")
    @Expose
    private String descriptionSwati;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("discount_type")
    @Expose
    private Object discountType;
    @SerializedName("validity")
    @Expose
    private Integer validity;
    @SerializedName("activation_date")
    @Expose
    private String activationDate;
    @SerializedName("expiry_date")
    @Expose
    private String expiryDate;
    @SerializedName("usage_limit")
    @Expose
    private Integer usageLimit;
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

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionFrench() {
        return descriptionFrench;
    }

    public void setDescriptionFrench(String descriptionFrench) {
        this.descriptionFrench = descriptionFrench;
    }

    public String getDescriptionSpanish() {
        return descriptionSpanish;
    }

    public void setDescriptionSpanish(String descriptionSpanish) {
        this.descriptionSpanish = descriptionSpanish;
    }

    public String getDescriptionSwati() {
        return descriptionSwati;
    }

    public void setDescriptionSwati(String descriptionSwati) {
        this.descriptionSwati = descriptionSwati;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public Object getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Object discountType) {
        this.discountType = discountType;
    }

    public Integer getValidity() {
        return validity;
    }

    public void setValidity(Integer validity) {
        this.validity = validity;
    }

    public String getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(String activationDate) {
        this.activationDate = activationDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Integer getUsageLimit() {
        return usageLimit;
    }

    public void setUsageLimit(Integer usageLimit) {
        this.usageLimit = usageLimit;
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
package com.instaguard.user.Models.FareEstimate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by system7 on 10/11/2017.
 */

public class FareEstimateData {

    @SerializedName("promo_discount")
    @Expose
    private Object promoDiscount;
    @SerializedName("promo_status")
    @Expose
    private Object promoStatus;
    @SerializedName("promo_code")
    @Expose
    private Object promoCode;
    @SerializedName("base_fare")
    @Expose
    private Object baseFare;
    @SerializedName("total_hrs")
    @Expose
    private Object totalHrs;
    @SerializedName("price_per_hr")
    @Expose
    private Object pricePerHr;
    @SerializedName("price_per_guard")
    @Expose
    private Object pricePerGuard;
    @SerializedName("total_guard_amount")
    @Expose
    private Object totalGuardAmount;
    @SerializedName("before_vat_amount")
    @Expose
    private Object beforeVatAmount;
    @SerializedName("overall_total")
    @Expose
    private Object overallTotal;
    @SerializedName("wallet_status")
    @Expose
    private Boolean walletStatus;
    @SerializedName("wallet_amount")
    @Expose
    private Object walletAmount;
    @SerializedName("subscription_status")
    @Expose
    private Boolean subscriptionStatus;
    @SerializedName("subscription_percentage")
    @Expose
    private Object subscriptionPercentage;
    @SerializedName("subscription_amount")
    @Expose
    private Object subscriptionAmount;
    @SerializedName("sub_total")
    @Expose
    private Object subTotal;


    public Object getPromoDiscount() {
        return promoDiscount;
    }

    public void setPromoDiscount(Object promoDiscount) {
        this.promoDiscount = promoDiscount;
    }

    public Object getPromoStatus() {
        return promoStatus;
    }

    public void setPromoStatus(Object promoStatus) {
        this.promoStatus = promoStatus;
    }

    public Object getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(Object promoCode) {
        this.promoCode = promoCode;
    }

    public Object getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(Object baseFare) {
        this.baseFare = baseFare;
    }

    public Object getTotalHrs() {
        return totalHrs;
    }

    public void setTotalHrs(Object totalHrs) {
        this.totalHrs = totalHrs;
    }

    public Object getPricePerHr() {
        return pricePerHr;
    }

    public void setPricePerHr(Object pricePerHr) {
        this.pricePerHr = pricePerHr;
    }

    public Object getPricePerGuard() {
        return pricePerGuard;
    }

    public void setPricePerGuard(Object pricePerGuard) {
        this.pricePerGuard = pricePerGuard;
    }

    public Object getTotalGuardAmount() {
        return totalGuardAmount;
    }

    public void setTotalGuardAmount(Object totalGuardAmount) {
        this.totalGuardAmount = totalGuardAmount;
    }

    public Object getBeforeVatAmount() {
        return beforeVatAmount;
    }

    public void setBeforeVatAmount(Object beforeVatAmount) {
        this.beforeVatAmount = beforeVatAmount;
    }

    public Object getOverallTotal() {
        return overallTotal;
    }

    public void setOverallTotal(Object overallTotal) {
        this.overallTotal = overallTotal;
    }

    public Boolean getWalletStatus() {
        return walletStatus;
    }

    public void setWalletStatus(Boolean walletStatus) {
        this.walletStatus = walletStatus;
    }

    public Object getWalletAmount() {
        return walletAmount;
    }

    public void setWalletAmount(Object walletAmount) {
        this.walletAmount = walletAmount;
    }

    public Boolean getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public void setSubscriptionStatus(Boolean subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    public Object getSubscriptionPercentage() {
        return subscriptionPercentage;
    }

    public void setSubscriptionPercentage(Object subscriptionPercentage) {
        this.subscriptionPercentage = subscriptionPercentage;
    }

    public Object getSubscriptionAmount() {
        return subscriptionAmount;
    }

    public void setSubscriptionAmount(Object subscriptionAmount) {
        this.subscriptionAmount = subscriptionAmount;
    }

    public Object getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Object subTotal) {
        this.subTotal = subTotal;
    }
}
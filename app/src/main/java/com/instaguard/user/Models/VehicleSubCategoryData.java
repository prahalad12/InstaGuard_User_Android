package com.instaguard.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VehicleSubCategoryData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("service_name")
    @Expose
    private String serviceName;
    @SerializedName("service_name_french")
    @Expose
    private String serviceNameFrench;
    @SerializedName("service_name_spanish")
    @Expose
    private String serviceNameSpanish;
    @SerializedName("service_name_swati")
    @Expose
    private String serviceNameSwati;
    @SerializedName("service_image")
    @Expose
    private String serviceImage;
    @SerializedName("fixed_fare")
    @Expose
    private Double fixedFare;
    @SerializedName("base_fare")
    @Expose
    private Double baseFare;
    @SerializedName("visit_fare")
    @Expose
    private Double visitFare;
    @SerializedName("price_per_hour")
    @Expose
    private Double pricePerHour;
    @SerializedName("price_per_km")
    @Expose
    private Double pricePerKm;

    public Double getPricePerAgent() {
        return pricePerAgent;
    }

    public void setPricePerAgent(Double pricePerAgent) {
        this.pricePerAgent = pricePerAgent;
    }

    public Double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(Double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    @SerializedName("price_per_agent")
    @Expose
    private Double pricePerAgent;

    @SerializedName("price_per_day")
    @Expose
    private Double pricePerDay;

    @SerializedName("price_per_min")
    @Expose
    private Double pricePerMin;
    @SerializedName("user_cancellation_charge")
    @Expose
    private Integer userCancellationCharge;
    @SerializedName("user_cancellation_time")
    @Expose
    private Integer userCancellationTime;
    @SerializedName("admin_commission")
    @Expose
    private Integer adminCommission;
    @SerializedName("amount_type")
    @Expose
    private Integer amountType;

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("has_level")
    @Expose
    private Integer hasLevel;
    @SerializedName("Level_entry")
    @Expose
    private Integer levelEntry;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("book_guard_min_time")
    @Expose
    private Integer book_guard_min_time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceNameFrench() {
        return serviceNameFrench;
    }

    public void setServiceNameFrench(String serviceNameFrench) {
        this.serviceNameFrench = serviceNameFrench;
    }

    public String getServiceNameSpanish() {
        return serviceNameSpanish;
    }

    public void setServiceNameSpanish(String serviceNameSpanish) {
        this.serviceNameSpanish = serviceNameSpanish;
    }

    public String getServiceNameSwati() {
        return serviceNameSwati;
    }

    public void setServiceNameSwati(String serviceNameSwati) {
        this.serviceNameSwati = serviceNameSwati;
    }

    public String getServiceImage() {
        return serviceImage;
    }

    public void setServiceImage(String serviceImage) {
        this.serviceImage = serviceImage;
    }

    public Double getFixedFare() {
        return fixedFare;
    }

    public void setFixedFare(Double fixedFare) {
        this.fixedFare = fixedFare;
    }

    public Double getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(Double baseFare) {
        this.baseFare = baseFare;
    }

    public Double getVisitFare() {
        return visitFare;
    }

    public void setVisitFare(Double visitFare) {
        this.visitFare = visitFare;
    }

    public Double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(Double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public Double getPricePerKm() {
        return pricePerKm;
    }

    public void setPricePerKm(Double pricePerKm) {
        this.pricePerKm = pricePerKm;
    }

    public Double getPricePerMin() {
        return pricePerMin;
    }

    public void setPricePerMin(Double pricePerMin) {
        this.pricePerMin = pricePerMin;
    }

    public Integer getUserCancellationCharge() {
        return userCancellationCharge;
    }

    public void setUserCancellationCharge(Integer userCancellationCharge) {
        this.userCancellationCharge = userCancellationCharge;
    }

    public Integer getUserCancellationTime() {
        return userCancellationTime;
    }

    public void setUserCancellationTime(Integer userCancellationTime) {
        this.userCancellationTime = userCancellationTime;
    }

    public Integer getAdminCommission() {
        return adminCommission;
    }

    public void setAdminCommission(Integer adminCommission) {
        this.adminCommission = adminCommission;
    }

    public Integer getAmountType() {
        return amountType;
    }

    public void setAmountType(Integer amountType) {
        this.amountType = amountType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getHasLevel() {
        return hasLevel;
    }

    public void setHasLevel(Integer hasLevel) {
        this.hasLevel = hasLevel;
    }

    public Integer getLevelEntry() {
        return levelEntry;
    }

    public void setLevelEntry(Integer levelEntry) {
        this.levelEntry = levelEntry;
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

    public Integer getBook_guard_min_time() {
        return book_guard_min_time;
    }

    public void setBook_guard_min_time(Integer book_guard_min_time) {
        this.book_guard_min_time = book_guard_min_time;
    }
}
/*{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("service_name")
    @Expose
    private String serviceName;
    @SerializedName("service_image")
    @Expose
    private String serviceImage;
    @SerializedName("base_fare")
    @Expose
    private Integer baseFare;
    @SerializedName("admin_commission")
    @Expose
    private Integer adminCommission;
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

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceImage() {
        return serviceImage;
    }

    public void setServiceImage(String serviceImage) {
        this.serviceImage = serviceImage;
    }

    public Integer getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(Integer baseFare) {
        this.baseFare = baseFare;
    }

    public Integer getAdminCommission() {
        return adminCommission;
    }

    public void setAdminCommission(Integer adminCommission) {
        this.adminCommission = adminCommission;
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

}*/
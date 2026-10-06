package com.instaguard.user.Models.Subscription;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ViewSubsData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private Object description;
    @SerializedName("amount")
    @Expose
    private Object amount;
    @SerializedName("period")
    @Expose
    private Integer period;
    @SerializedName("period_type")
    @Expose
    private Object periodType;
    @SerializedName("free_km")
    @Expose
    private Integer freeKm;
    @SerializedName("free_job_request")
    @Expose
    private Integer freeJobRequest;
    @SerializedName("status")
    @Expose
    private Object status;
    @SerializedName("plan_type")
    @Expose
    private Object planType;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public Object getAmount() {
        return amount;
    }

    public void setAmount(Object amount) {
        this.amount = amount;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Object getPeriodType() {
        return periodType;
    }

    public void setPeriodType(Object periodType) {
        this.periodType = periodType;
    }

    public Integer getFreeKm() {
        return freeKm;
    }

    public void setFreeKm(Integer freeKm) {
        this.freeKm = freeKm;
    }

    public Integer getFreeJobRequest() {
        return freeJobRequest;
    }

    public void setFreeJobRequest(Integer freeJobRequest) {
        this.freeJobRequest = freeJobRequest;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
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

    public Object getPlanType() {
        return planType;
    }

    public void setPlanType(Object planType) {
        this.planType = planType;
    }
}
package com.instaguard.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sikka 20/06/2022.
 */

public class DeleteData {

    @SerializedName("driver_id")
    @Expose
    private String driverId;
    @SerializedName("user_type")
    @Expose
    private String userType;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("mobile_type")
    @Expose
    private String mobileType;
    @SerializedName("unique_code")
    @Expose
    private Integer uniqueCode;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("email")
    @Expose
    private String email;

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getMobileType() {
        return mobileType;
    }

    public void setMobileType(String mobileType) {
        this.mobileType = mobileType;
    }

    public Integer getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(Integer uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
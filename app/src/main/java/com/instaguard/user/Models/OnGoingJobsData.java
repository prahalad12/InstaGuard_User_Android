package com.instaguard.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OnGoingJobsData {
    @SerializedName("booking_id")
    @Expose
    private String bookingId;
    @SerializedName("job_number")
    @Expose
    private String jobNumber;
    @SerializedName("job_date")
    @Expose
    private String jobDate;
    @SerializedName("driver_name")
    @Expose
    private String driverName;
    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("customer_location")
    @Expose
    private String customerLocation;
    @SerializedName("driver_avatar")
    @Expose
    private String driverAvatar;
    @SerializedName("service_name")
    @Expose
    private String serviceName;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("payment_type")
    @Expose
    private String paymentType;
    @SerializedName("total_amount")
    @Expose
    private String totalAmount;
    @SerializedName("unit_price")
    @Expose
    private String unitPrice;
    @SerializedName("site_commission")
    @Expose
    private String siteCommission;
    @SerializedName("extra_charge")
    @Expose
    private String extraCharge;
    @SerializedName("misc_charge")
    @Expose
    private String miscCharge;
    @SerializedName("driver_discount")
    @Expose
    private String driverDiscount;
    @SerializedName("service_status")
    @Expose
    private Integer serviceStatus;
    @SerializedName("accept_time")
    @Expose
    private String acceptTime;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("driver_id")
    @Expose
    private String driverId;
    @SerializedName("category_ar")
    @Expose
    private String categoryAr;
    @SerializedName("service_ar")
    @Expose
    private String serviceAr;
    @SerializedName("company_name")
    @Expose
    private String CompanyName;
    public String getCategoryAr() {
        return categoryAr;
    }

    public void setCategoryAr(String categoryAr) {
        this.categoryAr = categoryAr;
    }

    public String getServiceAr() {
        return serviceAr;
    }

    public void setServiceAr(String serviceAr) {
        this.serviceAr = serviceAr;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getJobDate() {
        return jobDate;
    }

    public void setJobDate(String jobDate) {
        this.jobDate = jobDate;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerLocation() {
        return customerLocation;
    }

    public void setCustomerLocation(String customerLocation) {
        this.customerLocation = customerLocation;
    }

    public String getDriverAvatar() {
        return driverAvatar;
    }

    public void setDriverAvatar(String driverAvatar) {
        this.driverAvatar = driverAvatar;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getSiteCommission() {
        return siteCommission;
    }

    public void setSiteCommission(String siteCommission) {
        this.siteCommission = siteCommission;
    }

    public String getExtraCharge() {
        return extraCharge;
    }

    public void setExtraCharge(String extraCharge) {
        this.extraCharge = extraCharge;
    }

    public String getMiscCharge() {
        return miscCharge;
    }

    public void setMiscCharge(String miscCharge) {
        this.miscCharge = miscCharge;
    }

    public String getDriverDiscount() {
        return driverDiscount;
    }

    public void setDriverDiscount(String driverDiscount) {
        this.driverDiscount = driverDiscount;
    }

    public Integer getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(Integer serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public String getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(String acceptTime) {
        this.acceptTime = acceptTime;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }
    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String CompanyName) {
        this.CompanyName = CompanyName;
    }
}
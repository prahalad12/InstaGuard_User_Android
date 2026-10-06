package com.instaguard.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpcomingJobsData {


    @SerializedName("booking_id")
    @Expose
    private Integer bookingId;
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
    private Double totalAmount;
    @SerializedName("unit_price")
    @Expose
    private Double unitPrice;
    @SerializedName("site_commission")
    @Expose
    private Double siteCommission;
    @SerializedName("extra_charge")
    @Expose
    private Double extraCharge;
    @SerializedName("misc_charge")
    @Expose
    private Double miscCharge;
    @SerializedName("driver_discount")
    @Expose
    private Double driverDiscount;
    @SerializedName("service_status")
    @Expose
    private Integer serviceStatus;
    @SerializedName("accept_time")
    @Expose
    private String acceptTime;
    @SerializedName("rating")
    @Expose
    private Double rating;
    @SerializedName("category_ar")
    @Expose
    private String categoryAr;
    @SerializedName("service_ar")
    @Expose
    private String serviceAr;
    @SerializedName("driver_phone_number")
    @Expose
    private String driverPhoneNumber;
    @SerializedName("company_name")
    @Expose
    private String CompanyName;

    @SerializedName("total_agent")
    @Expose
    private String totalAgent;
    @SerializedName("total_days")
    @Expose
    private String totalDays;


    @SerializedName("start_date_time")
    @Expose
    private String startDateTime;
    @SerializedName("end_date_time")
    @Expose
    private String endDateTime;



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

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
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

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getSiteCommission() {
        return siteCommission;
    }

    public void setSiteCommission(Double siteCommission) {
        this.siteCommission = siteCommission;
    }

    public Double getExtraCharge() {
        return extraCharge;
    }

    public void setExtraCharge(Double extraCharge) {
        this.extraCharge = extraCharge;
    }

    public Double getMiscCharge() {
        return miscCharge;
    }

    public void setMiscCharge(Double miscCharge) {
        this.miscCharge = miscCharge;
    }

    public Double getDriverDiscount() {
        return driverDiscount;
    }

    public void setDriverDiscount(Double driverDiscount) {
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

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getDriverPhoneNumber() {
        return driverPhoneNumber;
    }

    public void setDriverPhoneNumber(String driverPhoneNumber) {
        this.driverPhoneNumber = driverPhoneNumber;
    }
    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String CompanyName) {
        this.CompanyName = CompanyName;
    }
    public String getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(String totalAgent) {
        this.totalDays = totalDays;
    }
    public String getTotalAgents() {
        return totalAgent;
    }

    public void setTotalAgents(String totalAgent) {
        this.totalAgent = totalAgent;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }
}
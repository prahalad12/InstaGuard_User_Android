package com.instaguard.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScheduleRequestData {

    @SerializedName("customer_id")
    @Expose
    private String customerId;
    @SerializedName("service_id")
    @Expose
    private String serviceId;
    @SerializedName("customer_location")
    @Expose
    private String customerLocation;
    @SerializedName("customer_lat")
    @Expose
    private String customerLat;
    @SerializedName("customer_lon")
    @Expose
    private String customerLon;
    @SerializedName("payment_type")
    @Expose
    private String paymentType;
    @SerializedName("driver_id")
    @Expose
    private String driverId;
    @SerializedName("request_time")
    @Expose
    private String requestTime;
    @SerializedName("request_date")
    @Expose
    private String requestDate;
    @SerializedName("job_date")
    @Expose
    private String jobDate;
    @SerializedName("mode")
    @Expose
    private String mode;
    @SerializedName("job_number")
    @Expose
    private String jobNumber;
    @SerializedName("customer_first_name")
    @Expose
    private String customerFirstName;
    @SerializedName("customer_last_name")
    @Expose
    private String customerLastName;
    @SerializedName("customer_email")
    @Expose
    private String customerEmail;
    @SerializedName("customer_phone_number")
    @Expose
    private String customerPhoneNumber;
    @SerializedName("service_status")
    @Expose
    private Integer serviceStatus;
    @SerializedName("cancel_service_status")
    @Expose
    private Integer cancelServiceStatus;
    @SerializedName("driver_first_name")
    @Expose
    private String driverFirstName;
    @SerializedName("driver_last_name")
    @Expose
    private String driverLastName;
    @SerializedName("driver_email")
    @Expose
    private String driverEmail;
    @SerializedName("driver_phone_number")
    @Expose
    private String driverPhoneNumber;
    @SerializedName("unit_price")
    @Expose
    private Double unitPrice;
    @SerializedName("site_commission")
    @Expose
    private Double siteCommission;
    @SerializedName("total_amount")
    @Expose
    private Double totalAmount;
    @SerializedName("strip_id")
    @Expose
    private String stripId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("booking_id")
    @Expose
    private Integer bookingId;
    @SerializedName("driver_avatar")
    @Expose
    private Object driverAvatar;
    @SerializedName("customer_avatar")
    @Expose
    private String customerAvatar;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getCustomerLocation() {
        return customerLocation;
    }

    public void setCustomerLocation(String customerLocation) {
        this.customerLocation = customerLocation;
    }

    public String getCustomerLat() {
        return customerLat;
    }

    public void setCustomerLat(String customerLat) {
        this.customerLat = customerLat;
    }

    public String getCustomerLon() {
        return customerLon;
    }

    public void setCustomerLon(String customerLon) {
        this.customerLon = customerLon;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getJobDate() {
        return jobDate;
    }

    public void setJobDate(String jobDate) {
        this.jobDate = jobDate;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    public void setCustomerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public Integer getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(Integer serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public Integer getCancelServiceStatus() {
        return cancelServiceStatus;
    }

    public void setCancelServiceStatus(Integer cancelServiceStatus) {
        this.cancelServiceStatus = cancelServiceStatus;
    }

    public String getDriverFirstName() {
        return driverFirstName;
    }

    public void setDriverFirstName(String driverFirstName) {
        this.driverFirstName = driverFirstName;
    }

    public String getDriverLastName() {
        return driverLastName;
    }

    public void setDriverLastName(String driverLastName) {
        this.driverLastName = driverLastName;
    }

    public String getDriverEmail() {
        return driverEmail;
    }

    public void setDriverEmail(String driverEmail) {
        this.driverEmail = driverEmail;
    }

    public String getDriverPhoneNumber() {
        return driverPhoneNumber;
    }

    public void setDriverPhoneNumber(String driverPhoneNumber) {
        this.driverPhoneNumber = driverPhoneNumber;
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

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStripId() {
        return stripId;
    }

    public void setStripId(String stripId) {
        this.stripId = stripId;
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

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public Object getDriverAvatar() {
        return driverAvatar;
    }

    public void setDriverAvatar(Object driverAvatar) {
        this.driverAvatar = driverAvatar;
    }

    public String getCustomerAvatar() {
        return customerAvatar;
    }

    public void setCustomerAvatar(String customerAvatar) {
        this.customerAvatar = customerAvatar;
    }

}
package com.instaguard.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by system7 on 10/11/2017.
 */

public class CancelJobDataModel {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("job_number")
    @Expose
    private String jobNumber;
    @SerializedName("job_date")
    @Expose
    private String jobDate;
    @SerializedName("customer_id")
    @Expose
    private Object customerId;
    @SerializedName("customer_first_name")
    @Expose
    private String customerFirstName;
    @SerializedName("customer_last_name")
    @Expose
    private Object customerLastName;
    @SerializedName("customer_email")
    @Expose
    private String customerEmail;
    @SerializedName("customer_phone_number")
    @Expose
    private Object customerPhoneNumber;
    @SerializedName("service_id")
    @Expose
    private String serviceId;
    @SerializedName("customer_location")
    @Expose
    private Object customerLocation;
    @SerializedName("customer_lat")
    @Expose
    private String customerLat;
    @SerializedName("customer_lon")
    @Expose
    private String customerLon;
    @SerializedName("request_date")
    @Expose
    private Object requestDate;
    @SerializedName("request_time")
    @Expose
    private Object requestTime;
    @SerializedName("accept_time")
    @Expose
    private Object acceptTime;
    @SerializedName("start_time")
    @Expose
    private Object startTime;
    @SerializedName("reach_time")
    @Expose
    private Object reachTime;
    @SerializedName("service_start_time")
    @Expose
    private Object serviceStartTime;
    @SerializedName("service_status")
    @Expose
    private Integer serviceStatus;
    @SerializedName("cancel_service_status")
    @Expose
    private Integer cancelServiceStatus;
    @SerializedName("driver_id")
    @Expose
    private Object driverId;
    @SerializedName("driver_first_name")
    @Expose
    private String driverFirstName;
    @SerializedName("driver_last_name")
    @Expose
    private Object driverLastName;
    @SerializedName("driver_email")
    @Expose
    private String driverEmail;
    @SerializedName("driver_phone_number")
    @Expose
    private Object driverPhoneNumber;
    @SerializedName("total_distance")
    @Expose
    private Double totalDistance;
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
    @SerializedName("coupon_id")
    @Expose
    private Object couponId;
    @SerializedName("cancel_reason")
    @Expose
    private String cancelReason;
    @SerializedName("current_satus")
    @Expose
    private Integer currentSatus;
    @SerializedName("cancel_job_satus")
    @Expose
    private Integer cancelJobSatus;
    @SerializedName("payment_satus")
    @Expose
    private Integer paymentSatus;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("payment_type")
    @Expose
    private String paymentType;
    @SerializedName("strip_name")
    @Expose
    private Object stripName;
    @SerializedName("strip_id")
    @Expose
    private Object stripId;
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

    public Object getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Object customerId) {
        this.customerId = customerId;
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    public Object getCustomerLastName() {
        return customerLastName;
    }

    public void setCustomerLastName(Object customerLastName) {
        this.customerLastName = customerLastName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Object getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(Object customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public Object getCustomerLocation() {
        return customerLocation;
    }

    public void setCustomerLocation(Object customerLocation) {
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

    public Object getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Object requestDate) {
        this.requestDate = requestDate;
    }

    public Object getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Object requestTime) {
        this.requestTime = requestTime;
    }

    public Object getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(Object acceptTime) {
        this.acceptTime = acceptTime;
    }

    public Object getStartTime() {
        return startTime;
    }

    public void setStartTime(Object startTime) {
        this.startTime = startTime;
    }

    public Object getReachTime() {
        return reachTime;
    }

    public void setReachTime(Object reachTime) {
        this.reachTime = reachTime;
    }

    public Object getServiceStartTime() {
        return serviceStartTime;
    }

    public void setServiceStartTime(Object serviceStartTime) {
        this.serviceStartTime = serviceStartTime;
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

    public Object getDriverId() {
        return driverId;
    }

    public void setDriverId(Object driverId) {
        this.driverId = driverId;
    }

    public String getDriverFirstName() {
        return driverFirstName;
    }

    public void setDriverFirstName(String driverFirstName) {
        this.driverFirstName = driverFirstName;
    }

    public Object getDriverLastName() {
        return driverLastName;
    }

    public void setDriverLastName(Object driverLastName) {
        this.driverLastName = driverLastName;
    }

    public String getDriverEmail() {
        return driverEmail;
    }

    public void setDriverEmail(String driverEmail) {
        this.driverEmail = driverEmail;
    }

    public Object getDriverPhoneNumber() {
        return driverPhoneNumber;
    }

    public void setDriverPhoneNumber(Object driverPhoneNumber) {
        this.driverPhoneNumber = driverPhoneNumber;
    }

    public Double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(Double totalDistance) {
        this.totalDistance = totalDistance;
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

    public Object getCouponId() {
        return couponId;
    }

    public void setCouponId(Object couponId) {
        this.couponId = couponId;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public Integer getCurrentSatus() {
        return currentSatus;
    }

    public void setCurrentSatus(Integer currentSatus) {
        this.currentSatus = currentSatus;
    }

    public Integer getCancelJobSatus() {
        return cancelJobSatus;
    }

    public void setCancelJobSatus(Integer cancelJobSatus) {
        this.cancelJobSatus = cancelJobSatus;
    }

    public Integer getPaymentSatus() {
        return paymentSatus;
    }

    public void setPaymentSatus(Integer paymentSatus) {
        this.paymentSatus = paymentSatus;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Object getStripName() {
        return stripName;
    }

    public void setStripName(Object stripName) {
        this.stripName = stripName;
    }

    public Object getStripId() {
        return stripId;
    }

    public void setStripId(Object stripId) {
        this.stripId = stripId;
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
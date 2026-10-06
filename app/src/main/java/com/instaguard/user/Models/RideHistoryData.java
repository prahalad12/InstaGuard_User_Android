package com.instaguard.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Uplogic on 21/04/2018.
 */

public class RideHistoryData implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("job_number")
    @Expose
    private Object jobNumber;
    @SerializedName("job_date")
    @Expose
    private Object jobDate;
    @SerializedName("customer_id")
    @Expose
    private Object customerId;
    @SerializedName("customer_first_name")
    @Expose
    private Object customerFirstName;
    @SerializedName("customer_last_name")
    @Expose
    private Object customerLastName;
    @SerializedName("customer_email")
    @Expose
    private Object customerEmail;
    @SerializedName("customer_phone_number")
    @Expose
    private Object customerPhoneNumber;
    @SerializedName("service_id")
    @Expose
    private Object serviceId;
    @SerializedName("customer_location")
    @Expose
    private Object customerLocation;
    @SerializedName("customer_lat")
    @Expose
    private Object customerLat;
    @SerializedName("customer_lon")
    @Expose
    private Object customerLon;
    @SerializedName("category_id")
    @Expose
    private Object categoryId;
    @SerializedName("drop_lat")
    @Expose
    private Object dropLat;
    @SerializedName("drop_lon")
    @Expose
    private Object dropLon;
    @SerializedName("drop_location")
    @Expose
    private Object dropLocation;
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
    private Object cancelServiceStatus;
    @SerializedName("driver_id")
    @Expose
    private Object driverId;
    @SerializedName("driver_first_name")
    @Expose
    private Object driverFirstName;
    @SerializedName("driver_last_name")
    @Expose
    private Object driverLastName;
    @SerializedName("driver_email")
    @Expose
    private Object driverEmail;
    @SerializedName("driver_phone_number")
    @Expose
    private Object driverPhoneNumber;
    @SerializedName("provider_type")
    @Expose
    private Object providerType;
    @SerializedName("company_name")
    @Expose
    private Object companyName;
    @SerializedName("total_distance")
    @Expose
    private Object totalDistance;
    @SerializedName("total_amount")
    @Expose
    private Object totalAmount;
    @SerializedName("unit_price")
    @Expose
    private Object unitPrice;
    @SerializedName("level_type")
    @Expose
    private Object levelType;
    @SerializedName("main_level_amount")
    @Expose
    private Object mainLevelAmount;
    @SerializedName("sub_level_amount")
    @Expose
    private Object subLevelAmount;
    @SerializedName("visit_fare")
    @Expose
    private Object visitFare;
    @SerializedName("price_per_agent")
    @Expose
    private Object pricePerAgent;
    @SerializedName("price_per_day")
    @Expose
    private Object pricePerDay;
    @SerializedName("price_per_km")
    @Expose
    private Object pricePerKm;
    @SerializedName("total_agent")
    @Expose
    private Object totalAgent;
    @SerializedName("total_days")
    @Expose
    private Object totalDays;
    @SerializedName("site_commission")
    @Expose
    private Object siteCommission;
    @SerializedName("extra_charge")
    @Expose
    private Object extraCharge;
    @SerializedName("misc_charge")
    @Expose
    private Object miscCharge;
    @SerializedName("driver_discount")
    @Expose
    private Object driverDiscount;
    @SerializedName("distance_rate")
    @Expose
    private Object distanceRate;
    @SerializedName("coupon_id")
    @Expose
    private Object couponId;
    @SerializedName("cancel_reason")
    @Expose
    private Object cancelReason;
    @SerializedName("current_satus")
    @Expose
    private Object currentSatus;
    @SerializedName("cancel_job_satus")
    @Expose
    private Object cancelJobSatus;
    @SerializedName("payment_satus")
    @Expose
    private Object paymentSatus;
    @SerializedName("currency")
    @Expose
    private Object currency;
    @SerializedName("card_number")
    @Expose
    private Object cardNumber;
    @SerializedName("payment_type")
    @Expose
    private Object paymentType;
    @SerializedName("balance_amount")
    @Expose
    private Object balanceAmount;
    @SerializedName("strip_name")
    @Expose
    private Object stripName;
    @SerializedName("strip_id")
    @Expose
    private Object stripId;
    @SerializedName("paypal_payment_id")
    @Expose
    private Object paypalPaymentId;
    @SerializedName("bank_state_id")
    @Expose
    private Object bankStateId;
    @SerializedName("status")
    @Expose
    private Object status;
    @SerializedName("vehicle_image")
    @Expose
    private Object vehicleImage;
    @SerializedName("vehicle_number")
    @Expose
    private Object vehicleNumber;
    @SerializedName("vehicle_details")
    @Expose
    private Object vehicleDetails;
    @SerializedName("promocode")
    @Expose
    private Object promocode;
    @SerializedName("promo_discount")
    @Expose
    private Object promoDiscount;
    @SerializedName("admin_cash")
    @Expose
    private Object adminCash;
    @SerializedName("special_instruction")
    @Expose
    private Object specialInstruction;
    @SerializedName("created_at")
    @Expose
    private Object createdAt;
    @SerializedName("updated_at")
    @Expose
    private Object updatedAt;
    @SerializedName("provider_ids")
    @Expose
    private Object providerIds;
    @SerializedName("total_hrs")
    @Expose
    private Object totalHrs;
    @SerializedName("price_per_hr")
    @Expose
    private Object pricePerHr;
    @SerializedName("vat_amount")
    @Expose
    private Object vatAmount;
    @SerializedName("main_trip_id")
    @Expose
    private Object mainTripId;
    @SerializedName("trip_type")
    @Expose
    private Object tripType;
    @SerializedName("start_date_time")
    @Expose
    private String  startDateTime;
    @SerializedName("end_date_time")
    @Expose
    private String endDateTime;
    @SerializedName("emergency_msg")
    @Expose
    private Object emergencyMsg;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(Object jobNumber) {
        this.jobNumber = jobNumber;
    }

    public Object getJobDate() {
        return jobDate;
    }

    public void setJobDate(Object jobDate) {
        this.jobDate = jobDate;
    }

    public Object getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Object customerId) {
        this.customerId = customerId;
    }

    public Object getCustomerFirstName() {
        return customerFirstName;
    }

    public void setCustomerFirstName(Object customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    public Object getCustomerLastName() {
        return customerLastName;
    }

    public void setCustomerLastName(Object customerLastName) {
        this.customerLastName = customerLastName;
    }

    public Object getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(Object customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Object getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(Object customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public Object getServiceId() {
        return serviceId;
    }

    public void setServiceId(Object serviceId) {
        this.serviceId = serviceId;
    }

    public Object getCustomerLocation() {
        return customerLocation;
    }

    public void setCustomerLocation(Object customerLocation) {
        this.customerLocation = customerLocation;
    }

    public Object getCustomerLat() {
        return customerLat;
    }

    public void setCustomerLat(Object customerLat) {
        this.customerLat = customerLat;
    }

    public Object getCustomerLon() {
        return customerLon;
    }

    public void setCustomerLon(Object customerLon) {
        this.customerLon = customerLon;
    }

    public Object getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Object categoryId) {
        this.categoryId = categoryId;
    }

    public Object getDropLat() {
        return dropLat;
    }

    public void setDropLat(Object dropLat) {
        this.dropLat = dropLat;
    }

    public Object getDropLon() {
        return dropLon;
    }

    public void setDropLon(Object dropLon) {
        this.dropLon = dropLon;
    }

    public Object getDropLocation() {
        return dropLocation;
    }

    public void setDropLocation(Object dropLocation) {
        this.dropLocation = dropLocation;
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

    public Object getCancelServiceStatus() {
        return cancelServiceStatus;
    }

    public void setCancelServiceStatus(Object cancelServiceStatus) {
        this.cancelServiceStatus = cancelServiceStatus;
    }

    public Object getDriverId() {
        return driverId;
    }

    public void setDriverId(Object driverId) {
        this.driverId = driverId;
    }

    public Object getDriverFirstName() {
        return driverFirstName;
    }

    public void setDriverFirstName(Object driverFirstName) {
        this.driverFirstName = driverFirstName;
    }

    public Object getDriverLastName() {
        return driverLastName;
    }

    public void setDriverLastName(Object driverLastName) {
        this.driverLastName = driverLastName;
    }

    public Object getDriverEmail() {
        return driverEmail;
    }

    public void setDriverEmail(Object driverEmail) {
        this.driverEmail = driverEmail;
    }

    public Object getDriverPhoneNumber() {
        return driverPhoneNumber;
    }

    public void setDriverPhoneNumber(Object driverPhoneNumber) {
        this.driverPhoneNumber = driverPhoneNumber;
    }

    public Object getProviderType() {
        return providerType;
    }

    public void setProviderType(Object providerType) {
        this.providerType = providerType;
    }

    public Object getCompanyName() {
        return companyName;
    }

    public void setCompanyName(Object companyName) {
        this.companyName = companyName;
    }

    public Object getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(Object totalDistance) {
        this.totalDistance = totalDistance;
    }

    public Object getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Object totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Object getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Object unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Object getLevelType() {
        return levelType;
    }

    public void setLevelType(Object levelType) {
        this.levelType = levelType;
    }

    public Object getMainLevelAmount() {
        return mainLevelAmount;
    }

    public void setMainLevelAmount(Object mainLevelAmount) {
        this.mainLevelAmount = mainLevelAmount;
    }

    public Object getSubLevelAmount() {
        return subLevelAmount;
    }

    public void setSubLevelAmount(Object subLevelAmount) {
        this.subLevelAmount = subLevelAmount;
    }

    public Object getVisitFare() {
        return visitFare;
    }

    public void setVisitFare(Object visitFare) {
        this.visitFare = visitFare;
    }

    public Object getPricePerAgent() {
        return pricePerAgent;
    }

    public void setPricePerAgent(Object pricePerAgent) {
        this.pricePerAgent = pricePerAgent;
    }

    public Object getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(Object pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public Object getPricePerKm() {
        return pricePerKm;
    }

    public void setPricePerKm(Object pricePerKm) {
        this.pricePerKm = pricePerKm;
    }

    public Object getTotalAgent() {
        return totalAgent;
    }

    public void setTotalAgent(Object totalAgent) {
        this.totalAgent = totalAgent;
    }

    public Object getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(Object totalDays) {
        this.totalDays = totalDays;
    }

    public Object getSiteCommission() {
        return siteCommission;
    }

    public void setSiteCommission(Object siteCommission) {
        this.siteCommission = siteCommission;
    }

    public Object getExtraCharge() {
        return extraCharge;
    }

    public void setExtraCharge(Object extraCharge) {
        this.extraCharge = extraCharge;
    }

    public Object getMiscCharge() {
        return miscCharge;
    }

    public void setMiscCharge(Object miscCharge) {
        this.miscCharge = miscCharge;
    }

    public Object getDriverDiscount() {
        return driverDiscount;
    }

    public void setDriverDiscount(Object driverDiscount) {
        this.driverDiscount = driverDiscount;
    }

    public Object getDistanceRate() {
        return distanceRate;
    }

    public void setDistanceRate(Object distanceRate) {
        this.distanceRate = distanceRate;
    }

    public Object getCouponId() {
        return couponId;
    }

    public void setCouponId(Object couponId) {
        this.couponId = couponId;
    }

    public Object getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(Object cancelReason) {
        this.cancelReason = cancelReason;
    }

    public Object getCurrentSatus() {
        return currentSatus;
    }

    public void setCurrentSatus(Object currentSatus) {
        this.currentSatus = currentSatus;
    }

    public Object getCancelJobSatus() {
        return cancelJobSatus;
    }

    public void setCancelJobSatus(Object cancelJobSatus) {
        this.cancelJobSatus = cancelJobSatus;
    }

    public Object getPaymentSatus() {
        return paymentSatus;
    }

    public void setPaymentSatus(Object paymentSatus) {
        this.paymentSatus = paymentSatus;
    }

    public Object getCurrency() {
        return currency;
    }

    public void setCurrency(Object currency) {
        this.currency = currency;
    }

    public Object getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(Object cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Object getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Object paymentType) {
        this.paymentType = paymentType;
    }

    public Object getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(Object balanceAmount) {
        this.balanceAmount = balanceAmount;
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

    public Object getPaypalPaymentId() {
        return paypalPaymentId;
    }

    public void setPaypalPaymentId(Object paypalPaymentId) {
        this.paypalPaymentId = paypalPaymentId;
    }

    public Object getBankStateId() {
        return bankStateId;
    }

    public void setBankStateId(Object bankStateId) {
        this.bankStateId = bankStateId;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public Object getVehicleImage() {
        return vehicleImage;
    }

    public void setVehicleImage(Object vehicleImage) {
        this.vehicleImage = vehicleImage;
    }

    public Object getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(Object vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public Object getVehicleDetails() {
        return vehicleDetails;
    }

    public void setVehicleDetails(Object vehicleDetails) {
        this.vehicleDetails = vehicleDetails;
    }

    public Object getPromocode() {
        return promocode;
    }

    public void setPromocode(Object promocode) {
        this.promocode = promocode;
    }

    public Object getPromoDiscount() {
        return promoDiscount;
    }

    public void setPromoDiscount(Object promoDiscount) {
        this.promoDiscount = promoDiscount;
    }

    public Object getAdminCash() {
        return adminCash;
    }

    public void setAdminCash(Object adminCash) {
        this.adminCash = adminCash;
    }

    public Object getSpecialInstruction() {
        return specialInstruction;
    }

    public void setSpecialInstruction(Object specialInstruction) {
        this.specialInstruction = specialInstruction;
    }

    public Object getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Object createdAt) {
        this.createdAt = createdAt;
    }

    public Object getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Object updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Object getProviderIds() {
        return providerIds;
    }

    public void setProviderIds(Object providerIds) {
        this.providerIds = providerIds;
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

    public Object getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(Object vatAmount) {
        this.vatAmount = vatAmount;
    }

    public Object getMainTripId() {
        return mainTripId;
    }

    public void setMainTripId(Object mainTripId) {
        this.mainTripId = mainTripId;
    }

    public Object getTripType() {
        return tripType;
    }

    public void setTripType(Object tripType) {
        this.tripType = tripType;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String  getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public Object getEmergencyMsg() {
        return emergencyMsg;
    }

    public void setEmergencyMsg(Object emergencyMsg) {
        this.emergencyMsg = emergencyMsg;
    }

}
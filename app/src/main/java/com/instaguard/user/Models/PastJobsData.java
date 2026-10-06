package com.instaguard.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PastJobsData {

    @SerializedName("rating_status")
    @Expose
    private String ratingStatus;
    @SerializedName("rating")
    @Expose
    private Float rating;
    @SerializedName("booking_id")
    @Expose
    private Integer bookingId;
    @SerializedName("job_number")
    @Expose
    private String jobNumber;
    @SerializedName("job_date")
    @Expose
    private String jobDate;
    @SerializedName("job_type")
    @Expose
    private String jobType;
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
    private Integer unitPrice;
    @SerializedName("site_commission")
    @Expose
    private Integer siteCommission;
    @SerializedName("extra_charge")
    @Expose
    private Double extraCharge;
    @SerializedName("misc_charge")
    @Expose
    private Double miscCharge;
    @SerializedName("driver_discount")
    @Expose
    private Double driverDiscount;
    @SerializedName("price_per_km")
    @Expose
    private Integer pricePerKm;
    @SerializedName("total_distance")
    @Expose
    private Double totalDistance;
    @SerializedName("level_type")
    @Expose
    private Integer levelType;
    @SerializedName("main_level_amount")
    @Expose
    private Double mainLevelAmount;
    @SerializedName("sub_level_amount")
    @Expose
    private Double subLevelAmount;
    @SerializedName("visit_fare")
    @Expose
    private Double visitFare;
    @SerializedName("service_status")
    @Expose
    private Integer serviceStatus;
    @SerializedName("accept_time")
    @Expose
    private String acceptTime;
    @SerializedName("customer_avatar")
    @Expose
    private String customerAvatar;
    @SerializedName("driver_avatar")
    @Expose
    private String driverAvatar;
    @SerializedName("customer_id")
    @Expose
    private Integer customerId;
    @SerializedName("driver_id")
    @Expose
    private Integer driverId;
    @SerializedName("promocode")
    @Expose
    private String promocode;
    @SerializedName("promo_discount")
    @Expose
    private Double promoDiscount;
    @SerializedName("category_ar")
    @Expose
    private String categoryAr;
    @SerializedName("service_ar")
    @Expose
    private String serviceAr;
    @SerializedName("company_name")
    @Expose
    private String CompanyName;

    @SerializedName("total_agent")
    @Expose
    private String totalAgent;
    @SerializedName("total_days")
    @Expose
    private String totalDays;
    @SerializedName("price_per_day")
    @Expose
    private String priceperDay;



    /* "emergency_msg": " ",
      "trip_type": 2,
      "start_date_time": "2024-01-28 01:06",
      "end_date_time": "2024-01-29 00:00",
      "extended_hrs": 0,
      "extended_amount": 0,
      "extended_vat": 0,
      "extended_status": 0,*/

    @SerializedName("emergency_msg")
    @Expose
    private Object emergencyMsg;
    @SerializedName("start_date_time")
    @Expose
    private Object startDateTime;
    @SerializedName("end_date_time")
    @Expose
    private Object endDateTime;
    @SerializedName("extended_amount")
    @Expose
    private Object extendedAmount;
    @SerializedName("extended_vat")
    @Expose
    private Object extendedVat;

    @SerializedName("vat_amount")
    @Expose
    private Object vatAmount;

    @SerializedName("group_driver_id")
    @Expose
    private Object groupDriverId;
    @SerializedName("group_driver_data")
    @Expose
    private List<GroupDriverDatum> groupDriverData;

    @SerializedName("total_hrs")
    @Expose
    private Object totalHrs;
    @SerializedName("price_per_hr")
    @Expose
    private Object pricePerHr;
    @SerializedName("extended_status")
    @Expose
    private Object extendedStatus;
    @SerializedName("extended_hrs")
    @Expose
    private Object extendedHrs;
    @SerializedName("trip_type")
    @Expose
    private Object tripType;

    @SerializedName("price_per_agent")
    @Expose
    private Object pricePerAgent;

    @SerializedName("subscription_status")
    @Expose
    private Object subscriptionStatus;
    @SerializedName("discount_amount")
    @Expose
    private Object discountAmount;
    @SerializedName("discount_percentage")
    @Expose
    private Object discountPercentage;

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

    public String getRatingStatus() {
        return ratingStatus;
    }

    public void setRatingStatus(String ratingStatus) {
        this.ratingStatus = ratingStatus;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
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

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
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

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getSiteCommission() {
        return siteCommission;
    }

    public void setSiteCommission(Integer siteCommission) {
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

    public Integer getPricePerKm() {
        return pricePerKm;
    }

    public void setPricePerKm(Integer pricePerKm) {
        this.pricePerKm = pricePerKm;
    }

    public Double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(Double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public Integer getLevelType() {
        return levelType;
    }

    public void setLevelType(Integer levelType) {
        this.levelType = levelType;
    }

    public Double getMainLevelAmount() {
        return mainLevelAmount;
    }

    public void setMainLevelAmount(Double mainLevelAmount) {
        this.mainLevelAmount = mainLevelAmount;
    }

    public Double getSubLevelAmount() {
        return subLevelAmount;
    }

    public void setSubLevelAmount(Double subLevelAmount) {
        this.subLevelAmount = subLevelAmount;
    }

    public Double getVisitFare() {
        return visitFare;
    }

    public void setVisitFare(Double visitFare) {
        this.visitFare = visitFare;
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

    public String getCustomerAvatar() {
        return customerAvatar;
    }

    public void setCustomerAvatar(String customerAvatar) {
        this.customerAvatar = customerAvatar;
    }

    public String getDriverAvatar() {
        return driverAvatar;
    }

    public void setDriverAvatar(String driverAvatar) {
        this.driverAvatar = driverAvatar;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public String getPromocode() {
        return promocode;
    }

    public void setPromocode(String promocode) {
        this.promocode = promocode;
    }

    public Double getPromoDiscount() {
        return promoDiscount;
    }

    public void setPromoDiscount(Double promoDiscount) {
        this.promoDiscount = promoDiscount;
    }
    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String CompanyName) {
        this.CompanyName = CompanyName;
    }public String getTotalDays() {
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

    public String getPriceperDay() {
        return priceperDay;
    }

    public void setPriceperDay(String priceperDay) {
        this.priceperDay = priceperDay;
    }


    public Object getEmergencyMsg() {
        return emergencyMsg;
    }

    public void setEmergencyMsg(Object emergencyMsg) {
        this.emergencyMsg = emergencyMsg;
    }

    public Object getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Object startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Object getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Object endDateTime) {
        this.endDateTime = endDateTime;
    }

    public Object getExtendedAmount() {
        return extendedAmount;
    }

    public void setExtendedAmount(Object extendedAmount) {
        this.extendedAmount = extendedAmount;
    }

    public Object getExtendedVat() {
        return extendedVat;
    }

    public void setExtendedVat(Object extendedVat) {
        this.extendedVat = extendedVat;
    }

    public Object getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(Object vatAmount) {
        this.vatAmount = vatAmount;
    }

    public Object getGroupDriverId() {
        return groupDriverId;
    }

    public void setGroupDriverId(Object groupDriverId) {
        this.groupDriverId = groupDriverId;
    }

    public List<GroupDriverDatum> getGroupDriverData() {
        return groupDriverData;
    }

    public void setGroupDriverData(List<GroupDriverDatum> groupDriverData) {
        this.groupDriverData = groupDriverData;
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

    public Object getExtendedStatus() {
        return extendedStatus;
    }

    public void setExtendedStatus(Integer extendedStatus) {
        this.extendedStatus = extendedStatus;
    }

    public Object getExtendedHrs() {
        return extendedHrs;
    }

    public void setExtendedHrs(Object extendedHrs) {
        this.extendedHrs = extendedHrs;
    }

    public Object getTripType() {
        return tripType;
    }

    public void setTripType(Object tripType) {
        this.tripType = tripType;
    }

    public Object getPricePerAgent() {
        return pricePerAgent;
    }

    public void setPricePerAgent(Object pricePerAgent) {
        this.pricePerAgent = pricePerAgent;
    }

    public Object getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public void setSubscriptionStatus(Object subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    public Object getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Object discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Object getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Object discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
}
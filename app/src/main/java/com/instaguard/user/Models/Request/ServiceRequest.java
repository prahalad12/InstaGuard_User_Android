package com.instaguard.user.Models.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ServiceRequest {

    @SerializedName("card_number")
    @Expose
    private String cardNumber;
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("customer_lon")
    @Expose
    private String customerLon;
    @SerializedName("stripe_token")
    @Expose
    private String stripeToken;
    @SerializedName("drop_lon")
    @Expose
    private String dropLon;
    @SerializedName("category_type")
    @Expose
    private String categoryType;
    @SerializedName("vehicle_type")
    @Expose
    private String vehicleType;
    @SerializedName("promocode")
    @Expose
    private String promocode;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("drop_location")
    @Expose
    private String dropLocation;
    @SerializedName("location_type")
    @Expose
    private String locationType;
    @SerializedName("mode")
    @Expose
    private String mode;
    @SerializedName("customer_lat")
    @Expose
    private String customerLat;
    @SerializedName("payment_type")
    @Expose
    private String paymentType;
    @SerializedName("drop_lat")
    @Expose
    private String dropLat;
    @SerializedName("location_details")
    @Expose
    private String locationDetails;
    @SerializedName("customer_location")
    @Expose
    private String customerLocation;
    @SerializedName("vehicle_information")
    @Expose
    private String vehicleInformation;
    @SerializedName("special_instruction")
    @Expose
    private String specialInstruction;
    @SerializedName("service_id")
    @Expose
    private String serviceId;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("customer_id")
    @Expose
    private String customerId;
    @SerializedName("question_answers")
    @Expose
    private String questionAnswers;
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
    @SerializedName("request_status")
    @Expose
    private String requestStatus;
    @SerializedName("strip_id")
    @Expose
    private String stripId;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("provider_ids")
    @Expose
    private String providerIds;
    @SerializedName("booking_id")
    @Expose
    private Integer bookingId;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCustomerLon() {
        return customerLon;
    }

    public void setCustomerLon(String customerLon) {
        this.customerLon = customerLon;
    }

    public String getStripeToken() {
        return stripeToken;
    }

    public void setStripeToken(String stripeToken) {
        this.stripeToken = stripeToken;
    }

    public String getDropLon() {
        return dropLon;
    }

    public void setDropLon(String dropLon) {
        this.dropLon = dropLon;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getPromocode() {
        return promocode;
    }

    public void setPromocode(String promocode) {
        this.promocode = promocode;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDropLocation() {
        return dropLocation;
    }

    public void setDropLocation(String dropLocation) {
        this.dropLocation = dropLocation;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getCustomerLat() {
        return customerLat;
    }

    public void setCustomerLat(String customerLat) {
        this.customerLat = customerLat;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getDropLat() {
        return dropLat;
    }

    public void setDropLat(String dropLat) {
        this.dropLat = dropLat;
    }

    public String getLocationDetails() {
        return locationDetails;
    }

    public void setLocationDetails(String locationDetails) {
        this.locationDetails = locationDetails;
    }

    public String getCustomerLocation() {
        return customerLocation;
    }

    public void setCustomerLocation(String customerLocation) {
        this.customerLocation = customerLocation;
    }

    public String getVehicleInformation() {
        return vehicleInformation;
    }

    public void setVehicleInformation(String vehicleInformation) {
        this.vehicleInformation = vehicleInformation;
    }

    public String getSpecialInstruction() {
        return specialInstruction;
    }

    public void setSpecialInstruction(String specialInstruction) {
        this.specialInstruction = specialInstruction;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getQuestionAnswers() {
        return questionAnswers;
    }

    public void setQuestionAnswers(String questionAnswers) {
        this.questionAnswers = questionAnswers;
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

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }


    public String getStripId() {
        return stripId;
    }

    public void setStripId(String stripId) {
        this.stripId = stripId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getProviderIds() {
        return providerIds;
    }

    public void setProviderIds(String providerIds) {
        this.providerIds = providerIds;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

}

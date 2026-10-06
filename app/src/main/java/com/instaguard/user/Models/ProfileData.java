package com.instaguard.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by system7 on 10/11/2017.
 */

public class ProfileData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("social_id")
    @Expose
    private Object socialId;
    @SerializedName("social_type")
    @Expose
    private Object socialType;
    @SerializedName("avatar")
    @Expose
    private Object avatar;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("country_code")
    @Expose
    private String countryCode;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("birth_date")
    @Expose
    private Object birthDate;
    @SerializedName("gender")
    @Expose
    private Object gender;
    @SerializedName("phone_verified")
    @Expose
    private Integer phoneVerified;
    @SerializedName("email_verified")
    @Expose
    private Integer emailVerified;
    @SerializedName("address")
    @Expose
    private Object address;
    @SerializedName("city")
    @Expose
    private Object city;
    @SerializedName("state_id")
    @Expose
    private Object stateId;
    @SerializedName("country_id")
    @Expose
    private Object countryId;
    @SerializedName("postal_code")
    @Expose
    private Object postalCode;
    @SerializedName("device_type")
    @Expose
    private String deviceType;
    @SerializedName("device_token")
    @Expose
    private Object deviceToken;
    @SerializedName("login_token")
    @Expose
    private Object loginToken;
    @SerializedName("reset_pass_token")
    @Expose
    private Object resetPassToken;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("wallet_balance")
    @Expose
    private Double walletBalance;
    @SerializedName("last_seen")
    @Expose
    private String lastSeen;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("referral_code")
    @Expose
    private Object referralCode;
    @SerializedName("invite_code")
    @Expose
    private String inviteCode;
    @SerializedName("salt")
    @Expose
    private Object salt;


    @SerializedName("subscription_id")
    @Expose
    private Integer subscriptionId;
    @SerializedName("subscription_status")
    @Expose
    private Object subscriptionStatus;
    @SerializedName("subscription_name")
    @Expose
    private Object subscriptionName;
    @SerializedName("subscription_amount")
    @Expose
    private Object subscriptionAmount;
    @SerializedName("subscription_purchase")
    @Expose
    private Object subscriptionPurchase;
    @SerializedName("subscription_expiry")
    @Expose
    private Object subscriptionExpiry;
    @SerializedName("plan_type")
    @Expose
    private Object planType;
    @SerializedName("free_km")
    @Expose
    private Integer freeKm;
    @SerializedName("free_job")
    @Expose
    private Integer freeJobRequest;
    @SerializedName("subscription_description")
    @Expose
    private Object subscriptionDescription;
    @SerializedName("period_type")
    @Expose
    private Integer periodType;
    @SerializedName("period")
    @Expose
    private Integer period;
    @SerializedName("remeaning_job")
    @Expose
    private Integer remeaningJob;

    @SerializedName("subscription_code")
    @Expose
    private Object subscriptionCode;
    @SerializedName("maximum_discount_amount")
    @Expose
    private Object maximumDiscountAmount;
    @SerializedName("tow_service_id")
    @Expose
    private Object towServiceId;
    @SerializedName("appointment_service_id")
    @Expose
    private Object appointmentServiceId;
    @SerializedName("subscription_history_id")
    @Expose
    private Integer subscriptionHistoryId;
    @SerializedName("auto_renew")
    @Expose
    private Integer autoRenew;
    @SerializedName("discount_percentage")
    @Expose
    private Integer discountPercentage;
    @SerializedName("free_appointment_services")
    @Expose
    private Object freeAppointmentService;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Object getSocialId() {
        return socialId;
    }

    public void setSocialId(Object socialId) {
        this.socialId = socialId;
    }

    public Object getSocialType() {
        return socialType;
    }

    public void setSocialType(Object socialType) {
        this.socialType = socialType;
    }

    public Object getAvatar() {
        return avatar;
    }

    public void setAvatar(Object avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Object getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Object birthDate) {
        this.birthDate = birthDate;
    }

    public Object getGender() {
        return gender;
    }

    public void setGender(Object gender) {
        this.gender = gender;
    }

    public Integer getPhoneVerified() {
        return phoneVerified;
    }

    public void setPhoneVerified(Integer phoneVerified) {
        this.phoneVerified = phoneVerified;
    }

    public Integer getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Integer emailVerified) {
        this.emailVerified = emailVerified;
    }

    public Object getAddress() {
        return address;
    }

    public void setAddress(Object address) {
        this.address = address;
    }

    public Object getCity() {
        return city;
    }

    public void setCity(Object city) {
        this.city = city;
    }

    public Object getStateId() {
        return stateId;
    }

    public void setStateId(Object stateId) {
        this.stateId = stateId;
    }

    public Object getCountryId() {
        return countryId;
    }

    public void setCountryId(Object countryId) {
        this.countryId = countryId;
    }

    public Object getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Object postalCode) {
        this.postalCode = postalCode;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public Object getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(Object deviceToken) {
        this.deviceToken = deviceToken;
    }

    public Object getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(Object loginToken) {
        this.loginToken = loginToken;
    }

    public Object getResetPassToken() {
        return resetPassToken;
    }

    public void setResetPassToken(Object resetPassToken) {
        this.resetPassToken = resetPassToken;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(Double walletBalance) {
        this.walletBalance = walletBalance;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
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

    public Object getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(Object referralCode) {
        this.referralCode = referralCode;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public Object getSalt() {
        return salt;
    }

    public void setSalt(Object salt) {
        this.salt = salt;
    }

    public Integer getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Integer subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public Object getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public void setSubscriptionStatus(Object subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    public Object getSubscriptionName() {
        return subscriptionName;
    }

    public void setSubscriptionName(Object subscriptionName) {
        this.subscriptionName = subscriptionName;
    }

    public Object getSubscriptionAmount() {
        return subscriptionAmount;
    }

    public void setSubscriptionAmount(Object subscriptionAmount) {
        this.subscriptionAmount = subscriptionAmount;
    }

    public Object getSubscriptionPurchase() {
        return subscriptionPurchase;
    }

    public void setSubscriptionPurchase(Object subscriptionPurchase) {
        this.subscriptionPurchase = subscriptionPurchase;
    }

    public Object getSubscriptionExpiry() {
        return subscriptionExpiry;
    }

    public void setSubscriptionExpiry(Object subscriptionExpiry) {
        this.subscriptionExpiry = subscriptionExpiry;
    }

    public Object getPlanType() {
        return planType;
    }

    public void setPlanType(Object planType) {
        this.planType = planType;
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

    public Object getSubscriptionDescription() {
        return subscriptionDescription;
    }

    public void setSubscriptionDescription(Object subscriptionDescription) {
        this.subscriptionDescription = subscriptionDescription;
    }

    public Integer getPeriodType() {
        return periodType;
    }

    public void setPeriodType(Integer periodType) {
        this.periodType = periodType;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Integer getRemeaningJob() {
        return remeaningJob;
    }

    public void setRemeaningJob(Integer remeaningJob) {
        this.remeaningJob = remeaningJob;
    }

    public Object getSubscriptionCode() {
        return subscriptionCode;
    }

    public void setSubscriptionCode(Object subscriptionCode) {
        this.subscriptionCode = subscriptionCode;
    }

    public Object getMaximumDiscountAmount() {
        return maximumDiscountAmount;
    }

    public void setMaximumDiscountAmount(Object maximumDiscountAmount) {
        this.maximumDiscountAmount = maximumDiscountAmount;
    }

    public Object getTowServiceId() {
        return towServiceId;
    }

    public void setTowServiceId(Object towServiceId) {
        this.towServiceId = towServiceId;
    }

    public Object getAppointmentServiceId() {
        return appointmentServiceId;
    }

    public void setAppointmentServiceId(Object appointmentServiceId) {
        this.appointmentServiceId = appointmentServiceId;
    }

    public Integer getSubscriptionHistoryId() {
        return subscriptionHistoryId;
    }

    public void setSubscriptionHistoryId(Integer subscriptionHistoryId) {
        this.subscriptionHistoryId = subscriptionHistoryId;
    }

    public Integer getAutoRenew() {
        return autoRenew;
    }

    public void setAutoRenew(Integer autoRenew) {
        this.autoRenew = autoRenew;
    }

    public Integer getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Integer discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Object getFreeAppointmentService() {
        return freeAppointmentService;
    }

    public void setFreeAppointmentService(Object freeAppointmentService) {
        this.freeAppointmentService = freeAppointmentService;
    }
}
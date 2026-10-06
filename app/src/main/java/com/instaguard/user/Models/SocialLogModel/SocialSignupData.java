package com.instaguard.user.Models.SocialLogModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SocialSignupData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("social_id")
    @Expose
    private String socialId;
    @SerializedName("social_type")
    @Expose
    private String socialType;
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
    private String deviceToken;
    @SerializedName("login_token")
    @Expose
    private Object loginToken;
    @SerializedName("reset_pass_token")
    @Expose
    private Object resetPassToken;
    @SerializedName("language")
    @Expose
    private Object language;
    @SerializedName("currency")
    @Expose
    private Object currency;
    @SerializedName("wallet_balance")
    @Expose
    private Integer walletBalance;
    @SerializedName("last_seen")
    @Expose
    private Object lastSeen;
    @SerializedName("login_status")
    @Expose
    private Object loginStatus;
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
    private String referralCode;
    @SerializedName("invite_code")
    @Expose
    private Object inviteCode;
    @SerializedName("salt")
    @Expose
    private Object salt;
    @SerializedName("otp")
    @Expose
    private Object otp;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSocialId() {
        return socialId;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    public String getSocialType() {
        return socialType;
    }

    public void setSocialType(String socialType) {
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

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
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

    public Object getLanguage() {
        return language;
    }

    public void setLanguage(Object language) {
        this.language = language;
    }

    public Object getCurrency() {
        return currency;
    }

    public void setCurrency(Object currency) {
        this.currency = currency;
    }

    public Integer getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(Integer walletBalance) {
        this.walletBalance = walletBalance;
    }

    public Object getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Object lastSeen) {
        this.lastSeen = lastSeen;
    }

    public Object getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(Object loginStatus) {
        this.loginStatus = loginStatus;
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

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public Object getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(Object inviteCode) {
        this.inviteCode = inviteCode;
    }

    public Object getSalt() {
        return salt;
    }

    public void setSalt(Object salt) {
        this.salt = salt;
    }

    public Object getOtp() {
        return otp;
    }

    public void setOtp(Object otp) {
        this.otp = otp;
    }

}

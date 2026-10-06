package com.instaguard.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppSetting {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("site_name")
    @Expose
    private String siteName;
    @SerializedName("phone_verification")
    @Expose
    private Integer phoneVerification;
    @SerializedName("email_verification")
    @Expose
    private Integer emailVerification;
    @SerializedName("phone_verification_driver")
    @Expose
    private Integer phoneVerificationDriver;
    @SerializedName("email_verification_driver")
    @Expose
    private Integer emailVerificationDriver;
    @SerializedName("country_code")
    @Expose
    private String countryCode;
    @SerializedName("distance_unit")
    @Expose
    private String distanceUnit;
    @SerializedName("wallet_minimum")
    @Expose
    private Double walletMinimum;
    @SerializedName("referral_amount")
    @Expose
    private Double referralAmount;
    @SerializedName("approximate_time")
    @Expose
    private Integer approximateTime;
    @SerializedName("timedelay_accept")
    @Expose
    private Integer timedelayAccept;
    @SerializedName("service_change_mail")
    @Expose
    private Integer serviceChangeMail;
    @SerializedName("email_from_name")
    @Expose
    private String emailFromName;
    @SerializedName("noreply_email")
    @Expose
    private String noreplyEmail;
    @SerializedName("admin_email")
    @Expose
    private String adminEmail;
    @SerializedName("mail_footer")
    @Expose
    private String mailFooter;
    @SerializedName("smtp_user_id")
    @Expose
    private Object smtpUserId;
    @SerializedName("smtp_user_key")
    @Expose
    private Object smtpUserKey;
    @SerializedName("smtp_host")
    @Expose
    private String smtpHost;
    @SerializedName("sms_account_id")
    @Expose
    private Object smsAccountId;
    @SerializedName("sms_account_token")
    @Expose
    private Object smsAccountToken;
    @SerializedName("sms_phone_number")
    @Expose
    private Object smsPhoneNumber;
    @SerializedName("payment_publisher_key")
    @Expose
    private String paymentPublisherKey;
    @SerializedName("payment_secret_key")
    @Expose
    private String paymentSecretKey;
    @SerializedName("payment_mode")
    @Expose
    private String paymentMode;
    @SerializedName("enable_commission")
    @Expose
    private String enableCommission;
    @SerializedName("fb_app_id")
    @Expose
    private String fbAppId;
    @SerializedName("fb_link")
    @Expose
    private String fbLink;
    @SerializedName("fb_app_secret")
    @Expose
    private String fbAppSecret;
    @SerializedName("twitter_link")
    @Expose
    private String twitterLink;
    @SerializedName("twitter_secret")
    @Expose
    private String twitterSecret;
    @SerializedName("twitter_key")
    @Expose
    private String twitterKey;
    @SerializedName("twitter_oauth_token")
    @Expose
    private String twitterOauthToken;
    @SerializedName("twitter_oauth_secret")
    @Expose
    private String twitterOauthSecret;
    @SerializedName("linked_in_link")
    @Expose
    private Object linkedInLink;
    @SerializedName("instagram_link")
    @Expose
    private Object instagramLink;
    @SerializedName("gplus_app_name")
    @Expose
    private Object gplusAppName;
    @SerializedName("gplus_oauth_client_id")
    @Expose
    private Object gplusOauthClientId;
    @SerializedName("gplus_oauth_secret")
    @Expose
    private Object gplusOauthSecret;
    @SerializedName("gplus_oauth_redirect_url")
    @Expose
    private Object gplusOauthRedirectUrl;
    @SerializedName("gplus_login_site_url")
    @Expose
    private Object gplusLoginSiteUrl;
    @SerializedName("twitter_login_user")
    @Expose
    private String twitterLoginUser;
    @SerializedName("fb_login_user")
    @Expose
    private String fbLoginUser;
    @SerializedName("gplus_login_user")
    @Expose
    private String gplusLoginUser;
    @SerializedName("twitter_login_driver")
    @Expose
    private String twitterLoginDriver;
    @SerializedName("fb_login_driver")
    @Expose
    private String fbLoginDriver;
    @SerializedName("gplus_login_driver")
    @Expose
    private String gplusLoginDriver;
    @SerializedName("driver_search_radius")
    @Expose
    private Integer driverSearchRadius;
    @SerializedName("google_project_id")
    @Expose
    private Object googleProjectId;
    @SerializedName("google_push_api_key")
    @Expose
    private Object googlePushApiKey;
    @SerializedName("pass_phrase")
    @Expose
    private String passPhrase;
    @SerializedName("distance_limit")
    @Expose
    private Integer distanceLimit;
    @SerializedName("android_app_link")
    @Expose
    private Object androidAppLink;
    @SerializedName("iphone_app_link")
    @Expose
    private Object iphoneAppLink;
    @SerializedName("police_number")
    @Expose
    private Object policeNumber;
    @SerializedName("created_at")
    @Expose
    private Object createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @SerializedName("tawk_url")
    @Expose
    private String tawkUrl;
    @SerializedName("property_url")
    @Expose
    private String propertyUrl;
    @SerializedName("property_id")
    @Expose
    private String propertyId;

    @SerializedName("document_norms")
    @Expose
    private Object documentNorms;


    @SerializedName("paypal_client_id")
    @Expose
    private Object paypalClientId;
    @SerializedName("paypal_client_secret")
    @Expose
    private Object paypalClientSecret;
    @SerializedName("paypal_type")
    @Expose
    private Object paypalType;
    @SerializedName("paypal_privacy_url")
    @Expose
    private Object paypalPrivacyUrl;
    @SerializedName("paypal_legal_url")
    @Expose
    private Object paypalLegalUrl;
    @SerializedName("paypal_status")
    @Expose
    private Integer paypalStatus;

    @SerializedName("vat_percentage")
    @Expose
    private String vatPercentage;


    @SerializedName("guard_subscription")
    @Expose
    private Integer guardSubscription;
    @SerializedName("customer_manage_document")
    @Expose
    private Integer customerManageDocument;
    @SerializedName("timeout_delay")
    @Expose
    private Integer timeOutDelay;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public Integer getPhoneVerification() {
        return phoneVerification;
    }

    public void setPhoneVerification(Integer phoneVerification) {
        this.phoneVerification = phoneVerification;
    }

    public Integer getEmailVerification() {
        return emailVerification;
    }

    public void setEmailVerification(Integer emailVerification) {
        this.emailVerification = emailVerification;
    }

    public Integer getPhoneVerificationDriver() {
        return phoneVerificationDriver;
    }

    public void setPhoneVerificationDriver(Integer phoneVerificationDriver) {
        this.phoneVerificationDriver = phoneVerificationDriver;
    }

    public Integer getEmailVerificationDriver() {
        return emailVerificationDriver;
    }

    public void setEmailVerificationDriver(Integer emailVerificationDriver) {
        this.emailVerificationDriver = emailVerificationDriver;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getDistanceUnit() {
        return distanceUnit;
    }

    public void setDistanceUnit(String distanceUnit) {
        this.distanceUnit = distanceUnit;
    }

    public Double getWalletMinimum() {
        return walletMinimum;
    }

    public void setWalletMinimum(Double walletMinimum) {
        this.walletMinimum = walletMinimum;
    }

    public Double getReferralAmount() {
        return referralAmount;
    }

    public void setReferralAmount(Double referralAmount) {
        this.referralAmount = referralAmount;
    }

    public Integer getApproximateTime() {
        return approximateTime;
    }

    public void setApproximateTime(Integer approximateTime) {
        this.approximateTime = approximateTime;
    }

    public Integer getTimedelayAccept() {
        return timedelayAccept;
    }

    public void setTimedelayAccept(Integer timedelayAccept) {
        this.timedelayAccept = timedelayAccept;
    }

    public Integer getServiceChangeMail() {
        return serviceChangeMail;
    }

    public void setServiceChangeMail(Integer serviceChangeMail) {
        this.serviceChangeMail = serviceChangeMail;
    }

    public String getEmailFromName() {
        return emailFromName;
    }

    public void setEmailFromName(String emailFromName) {
        this.emailFromName = emailFromName;
    }

    public String getNoreplyEmail() {
        return noreplyEmail;
    }

    public void setNoreplyEmail(String noreplyEmail) {
        this.noreplyEmail = noreplyEmail;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getMailFooter() {
        return mailFooter;
    }

    public void setMailFooter(String mailFooter) {
        this.mailFooter = mailFooter;
    }

    public Object getSmtpUserId() {
        return smtpUserId;
    }

    public void setSmtpUserId(Object smtpUserId) {
        this.smtpUserId = smtpUserId;
    }

    public Object getSmtpUserKey() {
        return smtpUserKey;
    }

    public void setSmtpUserKey(Object smtpUserKey) {
        this.smtpUserKey = smtpUserKey;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public Object getSmsAccountId() {
        return smsAccountId;
    }

    public void setSmsAccountId(Object smsAccountId) {
        this.smsAccountId = smsAccountId;
    }

    public Object getSmsAccountToken() {
        return smsAccountToken;
    }

    public void setSmsAccountToken(Object smsAccountToken) {
        this.smsAccountToken = smsAccountToken;
    }

    public Object getSmsPhoneNumber() {
        return smsPhoneNumber;
    }

    public void setSmsPhoneNumber(Object smsPhoneNumber) {
        this.smsPhoneNumber = smsPhoneNumber;
    }

    public String getPaymentPublisherKey() {
        return paymentPublisherKey;
    }

    public void setPaymentPublisherKey(String paymentPublisherKey) {
        this.paymentPublisherKey = paymentPublisherKey;
    }

    public String getPaymentSecretKey() {
        return paymentSecretKey;
    }

    public void setPaymentSecretKey(String paymentSecretKey) {
        this.paymentSecretKey = paymentSecretKey;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getEnableCommission() {
        return enableCommission;
    }

    public void setEnableCommission(String enableCommission) {
        this.enableCommission = enableCommission;
    }

    public String getFbAppId() {
        return fbAppId;
    }

    public void setFbAppId(String fbAppId) {
        this.fbAppId = fbAppId;
    }

    public String getFbLink() {
        return fbLink;
    }

    public void setFbLink(String fbLink) {
        this.fbLink = fbLink;
    }

    public String getFbAppSecret() {
        return fbAppSecret;
    }

    public void setFbAppSecret(String fbAppSecret) {
        this.fbAppSecret = fbAppSecret;
    }

    public String getTwitterLink() {
        return twitterLink;
    }

    public void setTwitterLink(String twitterLink) {
        this.twitterLink = twitterLink;
    }

    public String getTwitterSecret() {
        return twitterSecret;
    }

    public void setTwitterSecret(String twitterSecret) {
        this.twitterSecret = twitterSecret;
    }

    public String getTwitterKey() {
        return twitterKey;
    }

    public void setTwitterKey(String twitterKey) {
        this.twitterKey = twitterKey;
    }

    public String getTwitterOauthToken() {
        return twitterOauthToken;
    }

    public void setTwitterOauthToken(String twitterOauthToken) {
        this.twitterOauthToken = twitterOauthToken;
    }

    public String getTwitterOauthSecret() {
        return twitterOauthSecret;
    }

    public void setTwitterOauthSecret(String twitterOauthSecret) {
        this.twitterOauthSecret = twitterOauthSecret;
    }

    public Object getLinkedInLink() {
        return linkedInLink;
    }

    public void setLinkedInLink(Object linkedInLink) {
        this.linkedInLink = linkedInLink;
    }

    public Object getInstagramLink() {
        return instagramLink;
    }

    public void setInstagramLink(Object instagramLink) {
        this.instagramLink = instagramLink;
    }

    public Object getGplusAppName() {
        return gplusAppName;
    }

    public void setGplusAppName(Object gplusAppName) {
        this.gplusAppName = gplusAppName;
    }

    public Object getGplusOauthClientId() {
        return gplusOauthClientId;
    }

    public void setGplusOauthClientId(Object gplusOauthClientId) {
        this.gplusOauthClientId = gplusOauthClientId;
    }

    public Object getGplusOauthSecret() {
        return gplusOauthSecret;
    }

    public void setGplusOauthSecret(Object gplusOauthSecret) {
        this.gplusOauthSecret = gplusOauthSecret;
    }

    public Object getGplusOauthRedirectUrl() {
        return gplusOauthRedirectUrl;
    }

    public void setGplusOauthRedirectUrl(Object gplusOauthRedirectUrl) {
        this.gplusOauthRedirectUrl = gplusOauthRedirectUrl;
    }

    public Object getGplusLoginSiteUrl() {
        return gplusLoginSiteUrl;
    }

    public void setGplusLoginSiteUrl(Object gplusLoginSiteUrl) {
        this.gplusLoginSiteUrl = gplusLoginSiteUrl;
    }

    public String getTwitterLoginUser() {
        return twitterLoginUser;
    }

    public void setTwitterLoginUser(String twitterLoginUser) {
        this.twitterLoginUser = twitterLoginUser;
    }

    public String getFbLoginUser() {
        return fbLoginUser;
    }

    public void setFbLoginUser(String fbLoginUser) {
        this.fbLoginUser = fbLoginUser;
    }

    public String getGplusLoginUser() {
        return gplusLoginUser;
    }

    public void setGplusLoginUser(String gplusLoginUser) {
        this.gplusLoginUser = gplusLoginUser;
    }

    public String getTwitterLoginDriver() {
        return twitterLoginDriver;
    }

    public void setTwitterLoginDriver(String twitterLoginDriver) {
        this.twitterLoginDriver = twitterLoginDriver;
    }

    public String getFbLoginDriver() {
        return fbLoginDriver;
    }

    public void setFbLoginDriver(String fbLoginDriver) {
        this.fbLoginDriver = fbLoginDriver;
    }

    public String getGplusLoginDriver() {
        return gplusLoginDriver;
    }

    public void setGplusLoginDriver(String gplusLoginDriver) {
        this.gplusLoginDriver = gplusLoginDriver;
    }

    public Integer getDriverSearchRadius() {
        return driverSearchRadius;
    }

    public void setDriverSearchRadius(Integer driverSearchRadius) {
        this.driverSearchRadius = driverSearchRadius;
    }

    public Object getGoogleProjectId() {
        return googleProjectId;
    }

    public void setGoogleProjectId(Object googleProjectId) {
        this.googleProjectId = googleProjectId;
    }

    public Object getGooglePushApiKey() {
        return googlePushApiKey;
    }

    public void setGooglePushApiKey(Object googlePushApiKey) {
        this.googlePushApiKey = googlePushApiKey;
    }

    public String getPassPhrase() {
        return passPhrase;
    }

    public void setPassPhrase(String passPhrase) {
        this.passPhrase = passPhrase;
    }

    public Integer getDistanceLimit() {
        return distanceLimit;
    }

    public void setDistanceLimit(Integer distanceLimit) {
        this.distanceLimit = distanceLimit;
    }

    public Object getAndroidAppLink() {
        return androidAppLink;
    }

    public void setAndroidAppLink(Object androidAppLink) {
        this.androidAppLink = androidAppLink;
    }

    public Object getIphoneAppLink() {
        return iphoneAppLink;
    }

    public void setIphoneAppLink(Object iphoneAppLink) {
        this.iphoneAppLink = iphoneAppLink;
    }

    public Object getPoliceNumber() {
        return policeNumber;
    }

    public void setPoliceNumber(Object policeNumber) {
        this.policeNumber = policeNumber;
    }

    public Object getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Object createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getTawkUrl() {
        return tawkUrl;
    }

    public void setTawkUrl(String tawkUrl) {
        this.tawkUrl = tawkUrl;
    }

    public String getPropertyUrl() {
        return propertyUrl;
    }

    public void setPropertyUrl(String propertyUrl) {
        this.propertyUrl = propertyUrl;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public Object getDocumentNorms() {
        return documentNorms;
    }

    public void setDocumentNorms(Object documentNorms) {
        this.documentNorms = documentNorms;
    }

    public Object getPaypalClientId() {
        return paypalClientId;
    }

    public void setPaypalClientId(Object paypalClientId) {
        this.paypalClientId = paypalClientId;
    }

    public Object getPaypalClientSecret() {
        return paypalClientSecret;
    }

    public void setPaypalClientSecret(Object paypalClientSecret) {
        this.paypalClientSecret = paypalClientSecret;
    }

    public Object getPaypalType() {
        return paypalType;
    }

    public void setPaypalType(Object paypalType) {
        this.paypalType = paypalType;
    }

    public Object getPaypalPrivacyUrl() {
        return paypalPrivacyUrl;
    }

    public void setPaypalPrivacyUrl(Object paypalPrivacyUrl) {
        this.paypalPrivacyUrl = paypalPrivacyUrl;
    }

    public Object getPaypalLegalUrl() {
        return paypalLegalUrl;
    }

    public void setPaypalLegalUrl(Object paypalLegalUrl) {
        this.paypalLegalUrl = paypalLegalUrl;
    }

    public Integer getPaypalStatus() {
        return paypalStatus;
    }

    public void setPaypalStatus(Integer paypalStatus) {
        this.paypalStatus = paypalStatus;
    }

    public String getVatPercentage() {
        return vatPercentage;
    }

    public void setVatPercentage(String vatPercentage) {
        this.vatPercentage = vatPercentage;
    }

    public Integer getGuardSubscription() {
        return guardSubscription;
    }

    public void setGuardSubscription(Integer guardSubscription) {
        this.guardSubscription = guardSubscription;
    }

    public Integer getCustomerManageDocument() {
        return customerManageDocument;
    }

    public void setCustomerManageDocument(Integer customerManageDocument) {
        this.customerManageDocument = customerManageDocument;
    }

    public Integer getTimeOutDelay() {
        return timeOutDelay;
    }

    public void setTimeOutDelay(Integer timeOutDelay) {
        this.timeOutDelay = timeOutDelay;
    }
}
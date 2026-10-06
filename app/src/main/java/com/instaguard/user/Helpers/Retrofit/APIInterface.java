package com.instaguard.user.Helpers.Retrofit;



import com.instaguard.user.Helpers.PlaceAutoComplete.PlacesAutoComplete;
import com.instaguard.user.Models.AppSettingWebModel;
import com.instaguard.user.Models.AppSupportModel;
import com.instaguard.user.Models.AppSupportPagesModel;
import com.instaguard.user.Models.BalanceWalletAddMoneyModel;
import com.instaguard.user.Models.Bank.BankModel;
import com.instaguard.user.Models.Banner.BannerModel;
import com.instaguard.user.Models.CancelJobModel;
import com.instaguard.user.Models.ChangePasswordModel;
import com.instaguard.user.Models.ChatAccessModel;
import com.instaguard.user.Models.CheckAvailablityModel;
import com.instaguard.user.Models.CheckPromoModel;
import com.instaguard.user.Models.DeleteModel;
import com.instaguard.user.Models.DocumentsUpload.DocumentListData;
import com.instaguard.user.Models.DocumentsUpload.DocumentViewModel;
import com.instaguard.user.Models.DocumentsUpload.ReUploadModel;
import com.instaguard.user.Models.DocumentsUpload.UploadDocModel;
import com.instaguard.user.Models.DocumentsUpload.UploadModel;
import com.instaguard.user.Models.EmailMobileCheckingModel;
import com.instaguard.user.Models.EmergencyAddModel;
import com.instaguard.user.Models.EmergencyDeleteModel;
import com.instaguard.user.Models.EmergencyViewModel;
import com.instaguard.user.Models.FareEstimate.FareEstimateModel;
import com.instaguard.user.Models.ForgetPassModel;
import com.instaguard.user.Models.ForgotPasswordModel;
import com.instaguard.user.Models.LinkedInUserDetailsModel;
import com.instaguard.user.Models.LoginModel;
import com.instaguard.user.Models.LoginNewModel;
import com.instaguard.user.Models.LogoutModel;
import com.instaguard.user.Models.GoogleDistance.ModelRoutes;
import com.instaguard.user.Models.OnGoingJobsModel;
import com.instaguard.user.Models.PanicAddAdminModel;
import com.instaguard.user.Models.PastJobsModel;
import com.instaguard.user.Models.PayPalCreateModel;
import com.instaguard.user.Models.Payment.PaymentIntentModel;
import com.instaguard.user.Models.Payment.SaveWalletPaymentIntentModel;
import com.instaguard.user.Models.Payment.WalletPaymentIntentModel;
import com.instaguard.user.Models.ProfileModel;
import com.instaguard.user.Models.ProfilePicModel;
import com.instaguard.user.Models.ProfileUpdateModel;
import com.instaguard.user.Models.PushModel;
import com.instaguard.user.Models.RatingDriverModel;
import com.instaguard.user.Models.RefreshTokenModel;
import com.instaguard.user.Models.RegisterModel;
import com.instaguard.user.Models.Request.SendRequestModel;
import com.instaguard.user.Models.ResetPasswordModel;
import com.instaguard.user.Models.RideHistoryModel;
import com.instaguard.user.Models.ScheduleRequestModel;
import com.instaguard.user.Models.SocialLogModel.SocialLoginModel;
import com.instaguard.user.Models.SocialLogModel.SocialSignupModel;
import com.instaguard.user.Models.Subscription.BuyPlanModel;
import com.instaguard.user.Models.Subscription.BuyPlanWebHookModel;
import com.instaguard.user.Models.Subscription.CancelAutoRenewPlanModel;
import com.instaguard.user.Models.Subscription.CheckPlanModel;
import com.instaguard.user.Models.Subscription.ListViewSubsModel;
import com.instaguard.user.Models.Subscription.SubsHistoryModel;
import com.instaguard.user.Models.Subscription.UpdateSubsModel;
import com.instaguard.user.Models.Subscription.ViewSubsModel;
import com.instaguard.user.Models.UniqeIdResModel;
import com.instaguard.user.Models.UpcomingCancelJobsModel;
import com.instaguard.user.Models.UpcomingJobsModel;
import com.instaguard.user.Models.VehicleCategoryModel;
import com.instaguard.user.Models.VehicleSubCategoryModel;
import com.instaguard.user.Models.VerifyMobileModel;
import com.instaguard.user.Models.VerifyOtpModel;
import com.instaguard.user.Models.WalletAddMoneyModel;
import com.instaguard.user.Models.WalletAllModel;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by uplogic on 18/04/18.
 */


public interface APIInterface {

    @GET("json")
    Call<PlacesAutoComplete> getPlaces(
            @Query("input") String input, @Query("location") String location,
            @Query("key") String key, @Query("radius") String radius);


    @GET("/maps/api/directions/json")
    Call<ModelRoutes> getResponseDistance(
            @Query("origin") String str,
            @Query("destination") String str1,
            @Query("key") String str2);

    //Vehicle Category Response
    @FormUrlEncoded
    @POST("Categories?")
    Call<VehicleCategoryModel> doGetVehiclecategoryUpdateDetails(@Header("Authorization")String token,@Header("Accept")String accept,@Header("Content-Type")String content_type,@FieldMap HashMap<String, Object> map1);

    //Vehicle Category Response
    @FormUrlEncoded
    @POST("Service?")
    Call<VehicleSubCategoryModel> doGetVehicleSubcategoryDetails(@Header("Authorization")String token,@Header("Accept")String accept,@FieldMap HashMap<String, Object> map1);

    @FormUrlEncoded
    @POST("auth/login?")
    Call<LoginNewModel> doGetLoginDetails(@FieldMap HashMap<String, Object> map1);

    //SocialLog
    @FormUrlEncoded
    @POST("auth/SocialLog?")
    Call<SocialLoginModel> doGetSocialLoginDetailsdd(@FieldMap HashMap<String, Object> map1);


    //Social
    @FormUrlEncoded
    @POST("customer/socialAdd")
    Call<SocialLoginModel> doGetSocialLoginDetails(@FieldMap HashMap<String, Object> map1);

    //Social Register Response
    @FormUrlEncoded
    @POST("customer/socialUpdate")
    Call<SocialSignupModel> doSocialLoginUpdate(@FieldMap HashMap<String, Object> map1);


    //Register Response
    @FormUrlEncoded
    @POST("auth/register?")
    Call<RegisterModel> doGetRegisterDetails(@FieldMap HashMap<String, Object> map1);

    //Mobile email checking Response
    @FormUrlEncoded
    @POST("emailAvailable?")
    Call<EmailMobileCheckingModel> doGetEmailMobileChecking(@FieldMap HashMap<String, Object> map1);

    //Logout  Response
    @FormUrlEncoded
    @POST("auth/logout?")
    Call<LogoutModel> doGetLogoutDetails(@FieldMap HashMap<String, Object> map1);

    @FormUrlEncoded
    @POST("forgot_password?")
    Call<ForgotPasswordModel> doUpdateForgotPassword(@FieldMap HashMap<String, Object> map1);

    @FormUrlEncoded
    @POST("verifyMobile?")
    Call<VerifyMobileModel> doUpdateverifyMobile(@FieldMap HashMap<String, Object> map1);

    @FormUrlEncoded
    @POST("updatePassword?")
    Call<ResetPasswordModel> doUpdateResetPassword(@FieldMap HashMap<String, Object> map1);

    @FormUrlEncoded
    @POST("forgetpass?")
    Call<ForgetPassModel> doForgotPassword(@FieldMap HashMap<String, Object> map1);


    @FormUrlEncoded
    @POST("verifyotp?")
    Call<VerifyOtpModel> doVerifyOtp(@FieldMap HashMap<String, Object> map1);


    //Refresh Token  Response
    @FormUrlEncoded
    @POST("auth/refresh?")
    Call<RefreshTokenModel> doGetRefreshToken(@Header("Accept")String accept,@Header("Content-Type")String content_type,@FieldMap HashMap<String, Object> map1);

    //Profile View  Response
    @FormUrlEncoded
    @POST("profileView?")
    Call<ProfileModel> doGetprofileDetails(@Header("Authorization")String token, @Header("Accept")String accept,@FieldMap HashMap<String, Object> map1);

    //Profile Update  Response
    @FormUrlEncoded
    @POST("updateprofile?")
    Call<ProfileUpdateModel> doGetprofileUpdateDetails(@Header("Authorization")String token,@FieldMap HashMap<String, Object> map1);

    //Profile Photo Update  Response
    @FormUrlEncoded
    @POST("picupload?")
    Call<ProfilePicModel> doGetprofilePhotoDetails(@Header("Authorization")String token, @FieldMap HashMap<String, Object> map1);

    //Logout  Response
    @FormUrlEncoded
    @POST("savepanic?")
    Call<PanicAddAdminModel> doUpdateAddPanic(@FieldMap HashMap<String, Object> map1);


    //ADD Emergency Response
    @FormUrlEncoded
    @POST("emergencyContactCustomer?")
    Call<EmergencyAddModel> doAddemergencyContactCustomer(@Header("Authorization")String token, @FieldMap HashMap<String, Object> map1);

    //Vuew Emergency Response
    @FormUrlEncoded
    @POST("emergencyContactGET?")
    Call<EmergencyViewModel> doGetViewEmergencyDetails(@Header("Authorization")String token, @FieldMap HashMap<String, Object> map1);

    //Vuew Emergency Response
    @FormUrlEncoded
    @POST("emergencyContactDelete?")
    Call<EmergencyDeleteModel> doGetDeleteEmergencyDetails(@Header("Authorization")String token, @Header("Accept")String accept,@FieldMap HashMap<String, Object> map1);

    //Vuew Emergency Response
    @FormUrlEncoded
    @POST("customerongoing?")
    Call<OnGoingJobsModel> doCustomerOnGoingJobs(@Header("Authorization")String token, @FieldMap HashMap<String, Object> map1);

    //Upcoming Jobs Response
    @FormUrlEncoded
    @POST("upcomingJobs?")
    Call<UpcomingJobsModel> doCustomerUpcomingJobs(@Header("Authorization")String token,@Header("Accept")String accept, @FieldMap HashMap<String, Object> map1);

    //upcoming Cancel Jobs Response
    @FormUrlEncoded
    @POST("scheduledcancelJobs?")
    Call<UpcomingCancelJobsModel> doCustomerUpcomingCancelJobs(@Header("Authorization")String token,@Header("Accept")String accept, @FieldMap HashMap<String, Object> map1);

    //Past Jobs Response
    @FormUrlEncoded
    @POST("pastjobs?")
    Call<PastJobsModel> doCustomerPastJobs(@Header("Authorization")String token,@Header("Accept")String accept, @FieldMap HashMap<String, Object> map1);
  //  Call<PastJobsModel> doCustomerPastJobs(@Header("Authorization")String token, @FieldMap HashMap<String, Object> map1);

    //Vuew Emergency Response
    @FormUrlEncoded
    @POST("customerAddMoney?")
    Call<WalletAddMoneyModel> doUpdateWalletAmount(@Header("Authorization")String token, @FieldMap HashMap<String, Object> map1);


    //CancelJobs Update  Response
    @FormUrlEncoded
    @POST("cancelJobs?")
    Call<CancelJobModel> doCancelJobDetails(@Header("Authorization")String token, @FieldMap HashMap<String, Object> map1);

    //ScheduleRequesting Response
    @FormUrlEncoded
    @POST("scheduledBooking?")
    Call<ScheduleRequestModel> doScheduleRequesting(@Header("Authorization")String token,@Header("Accept")String accept, @FieldMap HashMap<String, Object> map1);

    //ScheduleRequesting Response
    @FormUrlEncoded
    @POST("checkPromoCode?")
    Call<CheckPromoModel> doCheckPromoCode(@Header("Authorization")String token, @Header("Accept")String accept, @FieldMap HashMap<String, Object> map1);

    //CancelJobs Update  Response
    @POST("appsetting")
    Call<AppSettingWebModel> doGetAppSettings();
    //Vehicle Category Response id(send) & token
    @FormUrlEncoded
    @POST("Support?")
    Call<AppSupportModel> doGetAppSupportDetails(@Header("Authorization")String token , @FieldMap HashMap<String, Object> map1);

    //Change password Photo Update  Response
    @FormUrlEncoded
    @POST("auth/passwordChange?")
    Call<ChangePasswordModel> doUpdateChangePassword(@Header("Authorization")String token, @FieldMap HashMap<String, Object> map1);

    //Change password Photo Update  Response
    @FormUrlEncoded
    @POST("availabilityCheckService?")
    Call<CheckAvailablityModel> doAvailabilityCheck(@Header("Authorization")String token, @Header("Accept")String accept, @FieldMap HashMap<String, Object> map1);

    //CancelJobs Update  Response
    @FormUrlEncoded
    @POST("CustomerFeedbackStore?")
    Call<RatingDriverModel> doUpdateRatingUser(@Header("Authorization")String token, @Header("Accept")String accept, @FieldMap HashMap<String, Object> map1);

    //Transaction Jobs Response
    @FormUrlEncoded
    @POST("Transaction?")
    Call<WalletAllModel> doWalletTransactionAll(@Header("Authorization")String token, @Header("Accept")String accept, @FieldMap HashMap<String, Object> map1);

    //Vuew Emergency Response
    @FormUrlEncoded
    @POST("customerTripPayment?")
    Call<BalanceWalletAddMoneyModel> doUpdateBalanceWalletAmount(@Header("Authorization")String token, @FieldMap HashMap<String, Object> map1);


    //Call Slide Banner Images
    @FormUrlEncoded
    @POST("getbanners?")
    Call<BannerModel> doSlideBannerImages(@Header("Authorization")String token, @FieldMap HashMap<String, Object> map1);

    //UpdateCardPayment Response
    @FormUrlEncoded
    @POST("Paycard?")
    Call<BalanceWalletAddMoneyModel> doUpdateCardPayment(@Header("Authorization")String token, @FieldMap HashMap<String, Object> map1);

    @FormUrlEncoded
    @POST("notifysend?")
    Call<PushModel> doNotifySend(@Header ("Authorization") String token, @FieldMap HashMap<String, Object> map1);

    //Support Response id(send) & token
    @FormUrlEncoded
    @POST("pages?")
    Call<AppSupportPagesModel> doGetSupportDetails(@Header("Authorization")String token , @FieldMap HashMap<String, Object> map1);


    //Linked In Response
    @GET("userinfo")
    Call<LinkedInUserDetailsModel> doGetLinkedInUserInfo(@Header("Authorization")String token);



    //Document Upload Path Response
    @FormUrlEncoded
    @POST("customerImageUpload")
    Call<UploadModel> doImageUploadPathDetails(@FieldMap HashMap<String, Object> map1);

    //Document Upload Path Response
    @FormUrlEncoded
    @POST("customerDocumentUpdate")
    Call<UploadDocModel> doDocumentUploadDetails(@FieldMap HashMap<String, Object> map1);

    //Document Upload Path Response
    @FormUrlEncoded
    @POST("customerProofReupload")
    Call<ReUploadModel> doDocumentReUploadDetails(@FieldMap HashMap<String, Object> map1);

    //Document Upload Path Response
    @FormUrlEncoded
    @POST("customerViewDocument")
    Call<DocumentViewModel> doDocumentViewDetails(@FieldMap HashMap<String, Object> map1);

    //Document Upload Path Response
    @FormUrlEncoded
    @POST("manageCustomerDocument")
    Call<DocumentListData> doCallViewManageDocument(@FieldMap HashMap<String, Object> map1);



    //Delete Account Response
    @FormUrlEncoded
    @POST("delete_account_request")
    Call<DeleteModel> doGetDeleteDetails(@FieldMap HashMap<String, Object> map1);


    //Delete Account Confirm  Response
    @FormUrlEncoded
    @POST("delete_account_confirm")
    Call<DeleteModel> doGetDeleteConfirmDetails(@FieldMap HashMap<String, Object> map1);


    //Create PayPal Payment Response
    @FormUrlEncoded
    @POST("createOrder")
    Call<PayPalCreateModel> doCreatePayPalPayment(@Header("Authorization")String token, @FieldMap HashMap<String, Object> map1);

    //Update PayPal Payment Response
    @FormUrlEncoded
    @POST("Paypaypal")
    Call<BalanceWalletAddMoneyModel> doUpdatePayPalPayment(@Header("Authorization")String token, @FieldMap HashMap<String, Object> map1);


    //Fare Estimate
    @FormUrlEncoded
    @POST("FareEstimate")
    Call<FareEstimateModel> doGetFareEstimate(@Header ("Authorization") String token, @FieldMap HashMap<String, Object> map1);

    @FormUrlEncoded
    @POST("customerTimeout")
    Call<BankModel> docallTimeout(@FieldMap HashMap<String, Object> map1);

    //Request send Response
    @FormUrlEncoded
    @POST("requestService?")
    Call<SendRequestModel> doRequestSendDetails(@FieldMap HashMap<String, Object> map1);

    //UpdateCardPayment Response
    @FormUrlEncoded
    @POST("requestService?")
    Call<BalanceWalletAddMoneyModel> doPaymentFinish_(@Header("Authorization")String token, @FieldMap HashMap<String, Object> map1);



    @FormUrlEncoded
    @POST("StripePayment3DApp")
    Call<PaymentIntentModel> doCreatePaymentIntentModel(@Header("Authorization") String key, @FieldMap HashMap<String, Object> map1);

    @FormUrlEncoded
    @POST("CreateSaveCardSetupIntent")
    Call<PaymentIntentModel> doCreateSavePaymentIntentModel(@Header("Authorization") String key, @FieldMap HashMap<String, Object> map1);

    @FormUrlEncoded
    @POST("CaptureWalletRecharge")
    Call<WalletPaymentIntentModel> doCreateWalletPaymentIntentModel(@Header("Authorization") String key, @FieldMap HashMap<String, Object> map1);

    @FormUrlEncoded
    @POST("StripeSaveCardPayment3DApp")
    Call<SaveWalletPaymentIntentModel> doSaveWalletPaymentIntentModel(@Header("Authorization") String key, @FieldMap HashMap<String, Object> map1);



    //ExtendedHours send Response
    @FormUrlEncoded
    @POST("UpdateJob?")
    Call<SendRequestModel> doExtendedHoursUpdateDetails(@FieldMap HashMap<String, Object> map1);


    //Fare Estimate
    @FormUrlEncoded
    @POST("ExtraCalFare")
    Call<FareEstimateModel> doGetExtendFareEstimate(@Header ("Authorization") String token, @FieldMap HashMap<String, Object> map1);


    //ChatAccessToken
    @GET("chatAccessToken")
    Call<ChatAccessModel> doChatAccessToken(@Query("device") String device,
                                            @Query("identity") String identity);

    //CallAccessToken
    @GET("accesstoken")
    Call<ChatAccessModel> doCallAccessToken(@Query("device") String device,
                                            @Query("identity") String identity);

    @FormUrlEncoded
    @POST("message_notify")
    Call<BankModel> addSendMessageNotify(@Header("Authorization") String key, @FieldMap HashMap<String, Object> map1);

    //Upcoming Jobs Response
    @FormUrlEncoded
    @POST("CustomerUpcomingJob?")
    Call<RideHistoryModel> doCustomerUpcomingJobs_offset(@Header("Authorization")String token, @Header("Accept")String accept, @FieldMap HashMap<String, Object> map1);


    @FormUrlEncoded
    @POST("subscriptionGetList")
    Call<ViewSubsModel> doGetsubscriptionGetList(@Header("Authorization") String token,
                                                 @Header("Accept") String accept, @FieldMap HashMap<String, Object> map1);

    @FormUrlEncoded
    @POST("subscriptionList")
    Call<ListViewSubsModel> doGetsubscriptionList(@FieldMap HashMap<String, Object> map1);

    @FormUrlEncoded
    @POST("buySubscription")
    Call<UpdateSubsModel> doUpdatesubscriptionList(@FieldMap HashMap<String, Object> map1);

    @FormUrlEncoded
    @POST("subscription_history")
    Call<SubsHistoryModel> doSubscriptionHistory(@FieldMap HashMap<String, Object> map1);

    //Disable Auto renew BuyPlan Response
    @FormUrlEncoded
    @POST("updateAutoRenew?")
    Call<CancelAutoRenewPlanModel> doUpdateAutoRenew(@FieldMap HashMap<String, Object> map1);

    //Check Subscription Sub category  Response
    @FormUrlEncoded
    @POST("checkService?")
    Call<CheckPlanModel> doCheckSubscriptionSubCategory(@FieldMap HashMap<String, Object> map1);


    //CheckSubscription BuyPlan Response
    @FormUrlEncoded
    @POST("createCheckoutSession?")
    Call<BuyPlanWebHookModel> doCreateCheckoutSessionSubscription(@FieldMap HashMap<String, Object> map1);

    //CheckSubscription BuyPlan Response
    @FormUrlEncoded
    @POST("checkSubscription?")
    Call<BuyPlanModel> doCheckSubscription(@FieldMap HashMap<String, Object> map1);


    //Request booking Cancel Jobs Response
    @FormUrlEncoded
    @POST("requestService?")
    Call<UpcomingCancelJobsModel> doCustomerRequestCancelJobs(@Header("Authorization")String token,@Header("Accept")String accept, @FieldMap HashMap<String, Object> map1);

    @FormUrlEncoded
    @POST("driverReferenceIdValidate")
    Call<UniqeIdResModel> verifyUniqueId(@FieldMap HashMap<String, String> map);


}
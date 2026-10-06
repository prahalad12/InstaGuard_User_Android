package com.instaguard.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by system7 on 10/11/2017.
 */

public class AppSettingWebModel {

    @SerializedName("data")
    @Expose
    private List<AppSupportDataModel> AppSupportDataModel = null;

    @SerializedName("app_setting")
    @Expose
    private AppSetting appSetting;
    @SerializedName("sms_templates")
    @Expose
    private List<AppSettingSmsTemplate> smsTemplates = null;
    @SerializedName("currency")
    @Expose
    private List<AppSettingCurrencyModel> currency = null;
    @SerializedName("language")
    @Expose
    private List<AppSettingLanguageModel> language = null;
    @SerializedName("message")
    @Expose
    private String message;

    public List<AppSupportDataModel> getAppSupportDataModel() {
        return AppSupportDataModel;
    }

    public void setData(List<AppSupportDataModel> AppSupportDataModel) {
        this.AppSupportDataModel = AppSupportDataModel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public AppSetting getAppSetting() {
        return appSetting;
    }

    public void setAppSetting(AppSetting appSetting) {
        this.appSetting = appSetting;
    }

    public List<AppSettingSmsTemplate> getSmsTemplates() {
        return smsTemplates;
    }

    public void setSmsTemplates(List<AppSettingSmsTemplate> smsTemplates) {
        this.smsTemplates = smsTemplates;
    }

    public List<AppSettingCurrencyModel> getCurrency() {
        return currency;
    }

    public void setCurrency(List<AppSettingCurrencyModel> currency) {
        this.currency = currency;
    }

    public List<AppSettingLanguageModel> getLanguage() {
        return language;
    }

    public void setLanguage(List<AppSettingLanguageModel> language) {
        this.language = language;
    }
}
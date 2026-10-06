package com.instaguard.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppSettingSmsTemplate{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("sms_subject")
    @Expose
    private String smsSubject;
    @SerializedName("sms_subject_french")
    @Expose
    private String smsSubjectFrench;
    @SerializedName("sms_subject_spanish")
    @Expose
    private String smsSubjectSpanish;
    @SerializedName("sms_subject_swati")
    @Expose
    private String smsSubjectSwati;
    @SerializedName("sms_content")
    @Expose
    private String smsContent;
    @SerializedName("sms_content_french")
    @Expose
    private String smsContentFrench;
    @SerializedName("sms_content_spanish")
    @Expose
    private String smsContentSpanish;
    @SerializedName("sms_content_swati")
    @Expose
    private String smsContentSwati;
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

    public String getSmsSubject() {
        return smsSubject;
    }

    public void setSmsSubject(String smsSubject) {
        this.smsSubject = smsSubject;
    }

    public String getSmsSubjectFrench() {
        return smsSubjectFrench;
    }

    public void setSmsSubjectFrench(String smsSubjectFrench) {
        this.smsSubjectFrench = smsSubjectFrench;
    }

    public String getSmsSubjectSpanish() {
        return smsSubjectSpanish;
    }

    public void setSmsSubjectSpanish(String smsSubjectSpanish) {
        this.smsSubjectSpanish = smsSubjectSpanish;
    }

    public String getSmsSubjectSwati() {
        return smsSubjectSwati;
    }

    public void setSmsSubjectSwati(String smsSubjectSwati) {
        this.smsSubjectSwati = smsSubjectSwati;
    }

    public String getSmsContent() {
        return smsContent;
    }

    public void setSmsContent(String smsContent) {
        this.smsContent = smsContent;
    }

    public String getSmsContentFrench() {
        return smsContentFrench;
    }

    public void setSmsContentFrench(String smsContentFrench) {
        this.smsContentFrench = smsContentFrench;
    }

    public String getSmsContentSpanish() {
        return smsContentSpanish;
    }

    public void setSmsContentSpanish(String smsContentSpanish) {
        this.smsContentSpanish = smsContentSpanish;
    }

    public String getSmsContentSwati() {
        return smsContentSwati;
    }

    public void setSmsContentSwati(String smsContentSwati) {
        this.smsContentSwati = smsContentSwati;
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
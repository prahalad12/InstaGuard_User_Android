package com.instaguard.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScheduleRequestModel {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("service_request")
    @Expose
    private ScheduleRequestData serviceRequest;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public ScheduleRequestData getServiceRequest() {
        return serviceRequest;
    }

    public void setServiceRequest(ScheduleRequestData serviceRequest) {
        this.serviceRequest = serviceRequest;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
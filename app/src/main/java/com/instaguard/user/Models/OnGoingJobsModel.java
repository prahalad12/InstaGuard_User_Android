package com.instaguard.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by system7 on 10/11/2017.
 */

public class OnGoingJobsModel {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("service_request")
    @Expose
    private List<OnGoingJobsData> serviceRequest = null;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<OnGoingJobsData> getServiceRequest() {
        return serviceRequest;
    }

    public void setServiceRequest(List<OnGoingJobsData> serviceRequest) {
        this.serviceRequest = serviceRequest;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
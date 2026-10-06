package com.instaguard.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Uplogic on 21/04/2018.
 */

public class RideHistoryModel {

    @SerializedName("result")
    @Expose
    private List<RideHistoryData> result = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("page")
    @Expose
    private String page;
    @SerializedName("next_page")
    @Expose
    private Integer nextPage;

    public List<RideHistoryData> getResult() {
        return result;
    }

    public void setResult(List<RideHistoryData> result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public Integer getNextPage() {
        return nextPage;
    }

    public void setNextPage(Integer nextPage) {
        this.nextPage = nextPage;
    }

}

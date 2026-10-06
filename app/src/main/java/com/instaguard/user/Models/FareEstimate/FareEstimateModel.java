package com.instaguard.user.Models.FareEstimate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.instaguard.user.Models.AppSetting;
import com.instaguard.user.Models.AppSettingCurrencyModel;
import com.instaguard.user.Models.AppSettingLanguageModel;
import com.instaguard.user.Models.AppSettingSmsTemplate;
import com.instaguard.user.Models.AppSupportDataModel;

import java.util.List;

/**
 * Created by system7 on 10/11/2017.
 */

public class FareEstimateModel  {

    @SerializedName("result")
    @Expose
    private FareEstimateData result;
    @SerializedName("message")
    @Expose
    private String message;

    public FareEstimateData getResult() {
        return result;
    }

    public void setResult(FareEstimateData result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
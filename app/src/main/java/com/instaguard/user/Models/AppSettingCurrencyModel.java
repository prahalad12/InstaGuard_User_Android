package com.instaguard.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class AppSettingCurrencyModel {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("symbol")
    @Expose
    private String symbol;
    @SerializedName("short_name")
    @Expose
    private String shortName;
    @SerializedName("default")
    @Expose
    private Integer _default;
    @SerializedName("converions_ratio")
    @Expose
    private Double converionsRatio;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Integer getDefault() {
        return _default;
    }

    public void setDefault(Integer _default) {
        this._default = _default;
    }

    public Double getConverionsRatio() {
        return converionsRatio;
    }

    public void setConverionsRatio(Double converionsRatio) {
        this.converionsRatio = converionsRatio;
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

package com.instaguard.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VehicleCategoryData{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("category_name_spanish")
    @Expose
    private String categoryNameSpanish;
    @SerializedName("category_image")
    @Expose
    private String categoryImage;
    @SerializedName("category_type")
    @Expose
    private Integer categoryType;
    @SerializedName("level_type")
    @Expose
    private Integer levelType;
    @SerializedName("level_amount")
    @Expose
    private Integer levelAmount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryNameSpanish() {
        return categoryNameSpanish;
    }

    public void setCategoryNameSpanish(String categoryNameSpanish) {
        this.categoryNameSpanish = categoryNameSpanish;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public Integer getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Integer categoryType) {
        this.categoryType = categoryType;
    }

    public Integer getLevelType() {
        return levelType;
    }

    public void setLevelType(Integer levelType) {
        this.levelType = levelType;
    }

    public Integer getLevelAmount() {
        return levelAmount;
    }

    public void setLevelAmount(Integer levelAmount) {
        this.levelAmount = levelAmount;
    }

}
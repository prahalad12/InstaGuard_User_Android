package com.instaguard.user.Models;

import java.io.Serializable;

public class CategorySubModel implements Serializable {

    private String id;
    public String getId(){
        return id;
    }
    public void setId(String cabId){
        this.id = cabId;
    }

    private boolean is_selected;
    public boolean getiIsSelected(){
        return is_selected;
    }
    public void setIsSelected(boolean is_selected){
        this.is_selected = is_selected;
    }

    private String SubCategoryId;
    public String getSubCategoryId(){
        return SubCategoryId;
    }
    public void setSubCategoryId(String SubCategoryId){
        this.SubCategoryId = SubCategoryId;
    }

    private String SubCategoryName;
    public String getSubCategoryName(){
        return SubCategoryName;
    }
    public void setSubCategoryName(String SubCategoryName){
        this.SubCategoryName = SubCategoryName;
    }

    private String SubCategoryImage;
    public String getSubCategoryImage(){
        return SubCategoryImage;
    }
    public void setSubCategoryImage(String SubCategoryImage){
        this.SubCategoryImage = SubCategoryImage;
    }

    private String SubMainCategoryId;
    public String getSubMainCategoryId(){
        return SubMainCategoryId;
    }
    public void setSubMainCategoryId(String SubMainCategoryId){
        this.SubMainCategoryId = SubMainCategoryId;
    }



    private String SubCategoryBasefare;
    public String getSubCategoryBasefare(){
        return SubCategoryBasefare;
    }
    public void setSubCategoryBasefare(String SubCategoryBasefare){
        this.SubCategoryBasefare = SubCategoryBasefare;
    }

    private String SubCategoryAdminComm;
    public String getSubCategoryAdminComm(){
        return SubCategoryAdminComm;
    }
    public void setSubCategoryAdminComm(String SubCategoryAdminComm){
        this.SubCategoryAdminComm = SubCategoryAdminComm;
    }

    private String SubCategoryPricePerKm;
    public String getSubCategoryPricePerKm(){
        return SubCategoryPricePerKm;
    }
    public void setSubCategoryPricePerKm(String SubCategoryPricePerKm) {
        this.SubCategoryPricePerKm = SubCategoryPricePerKm;
    }

    private String SubCategoryVistiPrice;
    public String getSubCategoryVistiPrice(){
        return SubCategoryVistiPrice;
    }
    public void setSubCategoryVistiPrice(String SubCategoryVistiPrice) {
        this.SubCategoryVistiPrice = SubCategoryVistiPrice;
    }

    private String SubCategoryEnName;
    public String getSubCategoryEnName(){
        return SubCategoryEnName;
    }
    public void setSubCategoryEnName(String SubCategoryEnName){
        this.SubCategoryEnName = SubCategoryEnName;
    }

    private String SubCategoryArName;
    public String getSubCategoryArName(){
        return SubCategoryArName;
    }
    public void setSubCategoryArName(String SubCategoryArName){
        this.SubCategoryArName = SubCategoryArName;
    }

    private Double SubCategoryAgentPrice;
    public Double getSubCategoryAgentPrice(){
        return SubCategoryAgentPrice;
    }
    public void setSubCategoryAgentPrice(Double SubCategoryAgentPrice) {
        this.SubCategoryAgentPrice = SubCategoryAgentPrice;
    }

    private Double SubCategoryDayPrice;
    public Double getSubCategoryDayPrice(){
        return SubCategoryDayPrice;
    }
    public void setSubCategoryDayPrice(Double SubCategoryDayPrice) {
        this.SubCategoryDayPrice = SubCategoryDayPrice;
    }

    private Integer bookGuardMinTime;

    public Integer getBookGuardMinTime() {
        return bookGuardMinTime;
    }

    public void setBookGuardMinTime(Integer bookGuardMinTime) {
        this.bookGuardMinTime = bookGuardMinTime;
    }
}

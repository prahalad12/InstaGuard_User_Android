package com.instaguard.user.Models;

import java.io.Serializable;

public class AppSettingModel implements Serializable {

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

    private String categoryId;
    public String getCategoryId(){
        return categoryId;
    }
    public void setCategoryId(String categoryId){
        this.categoryId = categoryId;
    }

    private String smscontent;
    public String getsmscontent(){
        return smscontent;
    }
    public void setsmscontent(String smscontent){
        this.smscontent = smscontent;
    }

    private String smssubject;
    public String getsmssubject(){
        return smssubject;
    }
    public void setsmssubject(String smssubject){
        this.smssubject = smssubject;
    }

    private String catId;
    public String getCatId(){
        return catId;
    }
    public void setCatId(String catId){
        this.catId = catId;
    }



    private String priceKm;
    public String getPriceKm(){
        return priceKm;
    }
    public void setPriceKm(String priceKm){
        this.priceKm = priceKm;
    }

    private String minFare;
    public String getMinFare(){
        return minFare;
    }
    public void setMinFare(String minFare){
        this.minFare = minFare;
    }

}

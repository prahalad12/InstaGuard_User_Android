package com.instaguard.user.Models;

import java.io.Serializable;

public class EmergencyModel implements Serializable {

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

    private String Contact_id;
    public String getContact_id(){
        return Contact_id;
    }
    public void setContact_id(String Contact_id){
        this.Contact_id = Contact_id;
    }

    private String Contact_name;
    public String getContact_name(){
        return Contact_name;
    }
    public void setContact_name(String Contact_name){
        this.Contact_name = Contact_name;
    }

    private String Contact_num;
    public String getContact_num(){
        return Contact_num;
    }
    public void setContact_num(String Contact_num){
        this.Contact_num = Contact_num;
    }

    private String Contact_cust_id;
    public String getContact_cust_id(){
        return Contact_cust_id;
    }
    public void setContact_cust_id(String Contact_cust_id){
        this.Contact_cust_id = Contact_cust_id;
    }

}

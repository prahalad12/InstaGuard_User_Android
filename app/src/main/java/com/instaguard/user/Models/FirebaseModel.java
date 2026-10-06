package com.instaguard.user.Models;

public class FirebaseModel {
    private Long id;
    private String message;
    private String status;
    private String date;
    private String title;

    private String DropLocation;
    private String PickupLatitude;
    private String PickupLongitude;
    private String DropLatitude;
    private String DropLongitude;
    private String CreatedTime;
    private String CustomerName;
    private String CustomerLastName;
    private String PhoneNumber;
    private String Profile;
    private String PickupDate;
    private String PickupTime;
    private String remarks_ru;
    private String active_status;
    private String timeline_status;

    public FirebaseModel() {
    }

    public FirebaseModel(Long id, String message, String status, String date, String dropLocation,
                         String pickupLatitude, String pickupLongitude, String dropLatitude, String dropLongitude,
                         String createdTime, String customerName, String customerLastName, String phoneNumber,
                         String profile, String title, String PickupDate, String PickupTime) {
        this.id = id;
        this.message = message;
        this.status = status;
        this.date = date;
        this.DropLocation = dropLocation;

        this.PickupLatitude = pickupLatitude;
        this.PickupLongitude = pickupLongitude;
        this.DropLatitude = dropLatitude;
        this.DropLongitude = dropLongitude;

        this.CreatedTime = createdTime;
        this.CustomerName = customerName;
        this.CustomerLastName = customerLastName;
        this.PhoneNumber = phoneNumber;
        this.Profile = profile;
        this.title = title;

        this.PickupDate = PickupDate;
        this.PickupTime = PickupTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

   /* public void setDropLocation(String dropLocation) {
        this.DropLocation = dropLocation;
    }

    public String getDropLocation() {
        return DropLocation;
    }

    public void setPickupLatitude(String pickupLatitude) {
        this.PickupLatitude = pickupLatitude;
    }

    public String getPickupLatitude() {
        return PickupLatitude;
    }

    public void setPickupLongitude(String pickupLongitude) {
        this.PickupLongitude = pickupLongitude;
    }

    public String getPickupLongitude() {
        return PickupLongitude;
    }

    public void setDropLatitude(String dropLatitude) {
        this.DropLatitude = dropLatitude;
    }

    public String getDropLatitude() {
        return DropLatitude;
    }

    public void setDropLongitude(String dropLongitude) {
        this.DropLongitude = dropLongitude;
    }

    public String getDropLongitude() {
        return DropLongitude;
    }

    public void setCreatedTime(String createdTime) {
        this.CreatedTime = createdTime;
    }

    public String getCreatedTime() {
        return CreatedTime;
    }

    public void setCustomerName(String customerName) {
        this.CustomerName = customerName;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerLastName(String customerLastName) {
        this.CustomerLastName = customerLastName;
    }

    public String getCustomerLastName() {
        return CustomerLastName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.PhoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setProfile(String profile) {
        this.Profile = profile;
    }

    public String getProfile() {
        return Profile;
    }*/

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status ;
    }

  /*  public void setPickupDate(String PickupDate) {
        this.PickupDate = PickupDate;
    }

    public String getPickupDate() {
        return PickupDate ;
    }

    public void setPickupTime(String PickupTime) {
        this.PickupTime = PickupTime;
    }

    public String getPickupTime() {
        return PickupTime ;
    }*/

/*
    public String getRemarks_ru() {
        return remarks_ru;
    }

    public String getActive_status() {
        return active_status;
    }

    public String getTimeline_status() {
        return timeline_status;
    }
*/

}


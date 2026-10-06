package com.instaguard.user.Models;

import android.content.Context;

public class TripsFirebaseGuardListModel {

    private String booking_id;

    private String driver_first;
    private String driver_last;
    private String driver_id;
    private String driver_mobile;
    private String driver_avatar;

    private String status;
    private String trip_status;


    private String accept_time;
    private String start_time;
    private String reach_time;
    private String service_start_time;

    private String rating_status;


    private final Context context_;

    public TripsFirebaseGuardListModel(Context context) {
        context_ = context;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status ;
    }


    public String getDriver_first() {
        return driver_first;
    }

    public void setDriver_first(String driver_first) {
        this.driver_first = driver_first;
    }

    public String getDriver_last() {
        return driver_last;
    }

    public void setDriver_last(String driver_last) {
        this.driver_last = driver_last;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getDriver_mobile() {
        return driver_mobile;
    }

    public void setDriver_mobile(String driver_mobile) {
        this.driver_mobile = driver_mobile;
    }

    public String getTrip_status() {
        return trip_status;
    }

    public void setTrip_status(String trip_status) {
        this.trip_status = trip_status;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getDriver_avatar() {
        return driver_avatar;
    }

    public void setDriver_avatar(String driver_avatar) {
        this.driver_avatar = driver_avatar;
    }

    public String getAccept_time() {
        return accept_time;
    }

    public void setAccept_time(String accept_time) {
        this.accept_time = accept_time;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getReach_time() {
        return reach_time;
    }

    public void setReach_time(String reach_time) {
        this.reach_time = reach_time;
    }

    public String getService_start_time() {
        return service_start_time;
    }

    public void setService_start_time(String service_start_time) {
        this.service_start_time = service_start_time;
    }

    public String getRating_status() {
        return rating_status;
    }

    public void setRating_status(String rating_status) {
        this.rating_status = rating_status;
    }
}


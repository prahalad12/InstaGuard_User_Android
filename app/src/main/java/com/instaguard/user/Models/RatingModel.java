package com.instaguard.user.Models;

public class RatingModel {

    private String driver_first;
    private String driver_last;
    private String driver_avatar;
    private String trip_status;
    private String driver_id;

    private String rating_status;
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

    public String getDriver_avatar() {
        return driver_avatar;
    }

    public void setDriver_avatar(String driver_avatar) {
        this.driver_avatar = driver_avatar;
    }

    public String getTrip_status() {
        return trip_status;
    }

    public void setTrip_status(String trip_status) {
        this.trip_status = trip_status;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getRating_status() {
        return rating_status;
    }

    public void setRating_status(String rating_status) {
        this.rating_status = rating_status;
    }
}

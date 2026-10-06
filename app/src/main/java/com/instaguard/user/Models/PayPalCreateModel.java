package com.instaguard.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by system7 on 10/11/2017.
 */

public class PayPalCreateModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /*{
    "id": "1TR48676AT269483W",
    "status": "CREATED",
    "links": [
        {
            "href": "https://api.sandbox.paypal.com/v2/checkout/orders/1TR48676AT269483W",
            "rel": "self",
            "method": "GET"
        },
        {
            "href": "https://www.sandbox.paypal.com/checkoutnow?token=1TR48676AT269483W",
            "rel": "approve",
            "method": "GET"
        },
        {
            "href": "https://api.sandbox.paypal.com/v2/checkout/orders/1TR48676AT269483W",
            "rel": "update",
            "method": "PATCH"
        },
        {
            "href": "https://api.sandbox.paypal.com/v2/checkout/orders/1TR48676AT269483W/capture",
            "rel": "capture",
            "method": "POST"
        }
    ],
    "message": "Order Created"
}*/
}
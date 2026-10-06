package com.instaguard.user.Models.Subscription;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CancelAutoRenewPlanModel {

    @SerializedName("message")
    @Expose
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    /*{
    "data": {
        "id": 41,
        "order_id": "ORD1828334",
        "cus_id": 249,
        "plan_id": 1,
        "plan_name": "Roadside Assistance Plan ($89.99)",
        "subscription_description": "<p>Roadside Assistance Plan ($89.99)</p>\r\n\r\n<p>&nbsp;</p>\r\n\r\n<ul>\r\n\t<li>Towing ($89.99)</li>\r\n</ul>\r\n\r\n<p>Tire&nbsp;&nbsp;($89.99)</p>\r\n\r\n<p>Battery ($89.99)</p>\r\n\r\n<p>Car Wash ($89.99)</p>\r\n\r\n<p>Polishing ($89)</p>\r\n\r\n<p>&nbsp;</p>\r\n\r\n<p>Roadside Assistance Plan ($89.99)</p>\r\n\r\n<p>&nbsp;</p>\r\n\r\n<p>Towing ($89.99)</p>\r\n\r\n<p>Tire&nbsp;&nbsp;($89.99)</p>\r\n\r\n<p>Battery ($89.99)</p>\r\n\r\n<p>Car Wash ($89.99)</p>\r\n\r\n<p>Polishing ($89)</p>\r\n\r\n<p>&nbsp;</p>\r\n\r\n<p>Roadside Assistance Plan ($89.99)</p>\r\n\r\n<p>&nbsp;</p>\r\n\r\n<p>Towing ($89.99)</p>\r\n\r\n<p>Tire&nbsp;&nbsp;($89.99)</p>\r\n\r\n<p>Battery ($89.99)</p>\r\n\r\n<p>Car Wash ($89.99)</p>\r\n\r\n<p>Polishing ($89)</p>",
        "period_type": 1,
        "period": 1,
        "purchase_date": "2023-03-07",
        "subscription_code": "0kRGz",
        "expiry_date": "2023-03-08",
        "plan_period": "1 days",
        "amount": 89.99,
        "discount_percentage": 1,
        "maximum_discount_amount": 100,
        "tow_service_id": "52",
        "appointment_service_id": "",
        "auto_renew": 1,
        "free_job_request": 3,
        "status": 1,
        "created_at": "2023-03-07 10:42:32",
        "updated_at": "2023-03-08 04:42:35"
    },
    "message": "auto renew updated successfully"
}*/
}
package com.instaguard.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by system7 on 10/11/2017.
 */

public class BalanceWalletAddMonetDataModel {

    @SerializedName("customer_id")
    @Expose
    private String customerId;
    @SerializedName("balance_amount")
    @Expose
    private String balanceAmount;
    @SerializedName("booking_id")
    @Expose
    private String bookingId;
    @SerializedName("driver_id")
    @Expose
    private String driverId;
    @SerializedName("card_number")
    @Expose
    private String cardNumber;
    @SerializedName("transaction_token")
    @Expose
    private String transactionToken;
    @SerializedName("driver_wallet_balance")
    @Expose
    private Double driverWalletBalance;
    @SerializedName("customer_wallet_balance")
    @Expose
    private Integer customerWalletBalance;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(String balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getTransactionToken() {
        return transactionToken;
    }

    public void setTransactionToken(String transactionToken) {
        this.transactionToken = transactionToken;
    }

    public Double getDriverWalletBalance() {
        return driverWalletBalance;
    }

    public void setDriverWalletBalance(Double driverWalletBalance) {
        this.driverWalletBalance = driverWalletBalance;
    }

    public Integer getCustomerWalletBalance() {
        return customerWalletBalance;
    }

    public void setCustomerWalletBalance(Integer customerWalletBalance) {
        this.customerWalletBalance = customerWalletBalance;
    }

}
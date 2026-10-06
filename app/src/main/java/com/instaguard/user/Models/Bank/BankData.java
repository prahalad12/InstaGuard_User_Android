package com.instaguard.user.Models.Bank;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BankData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("driverid")
    @Expose
    private String driverid;
    @SerializedName("bankname")
    @Expose
    private String bankname;
    @SerializedName("account_num")
    @Expose
    private String accountNum;
    @SerializedName("bank_code")
    @Expose
    private String bankCode;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("bank_fname")
    @Expose
    private String bankFname;
    @SerializedName("bank_lname")
    @Expose
    private String bankLname;
    @SerializedName("bank_email")
    @Expose
    private String bankEmail;
    @SerializedName("bank_dob")
    @Expose
    private String bankDob;
    @SerializedName("bank_phone")
    @Expose
    private String bankPhone;
    @SerializedName("created")
    @Expose
    private String created;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDriverid() {
        return driverid;
    }

    public void setDriverid(String driverid) {
        this.driverid = driverid;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getAccountNum() {
        return accountNum;
    }

    public void setAccountNum(String accountNum) {
        this.accountNum = accountNum;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBankFname() {
        return bankFname;
    }

    public void setBankFname(String bankFname) {
        this.bankFname = bankFname;
    }

    public String getBankLname() {
        return bankLname;
    }

    public void setBankLname(String bankLname) {
        this.bankLname = bankLname;
    }

    public String getBankEmail() {
        return bankEmail;
    }

    public void setBankEmail(String bankEmail) {
        this.bankEmail = bankEmail;
    }

    public String getBankDob() {
        return bankDob;
    }

    public void setBankDob(String bankDob) {
        this.bankDob = bankDob;
    }

    public String getBankPhone() {
        return bankPhone;
    }

    public void setBankPhone(String bankPhone) {
        this.bankPhone = bankPhone;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

}
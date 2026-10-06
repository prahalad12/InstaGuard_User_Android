package com.instaguard.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by system7 on 10/11/2017.
 */

public class LinkedInUserDetailsModel {

    @SerializedName("sub")
    @Expose
    private String sub;
    @SerializedName("email_verified")
    @Expose
    private Boolean emailVerified;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("locale")
    @Expose
    private Locale locale;
    @SerializedName("given_name")
    @Expose
    private String givenName;
    @SerializedName("family_name")
    @Expose
    private String familyName;
    @SerializedName("email")
    @Expose
    private String email;

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public class Locale {

        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("language")
        @Expose
        private String language;

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

    }
}
package com.instaguard.user.Models;

import java.io.Serializable;

public class TransactionModel implements Serializable {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String cabId) {
        this.id = cabId;
    }

    private boolean is_selected;

    public boolean getiIsSelected() {
        return is_selected;
    }

    public void setIsSelected(boolean is_selected) {
        this.is_selected = is_selected;
    }

    private String TransactionType;

    public String getTransactionType() {
        return TransactionType;
    }

    public void setTransactionType(String TransactionType) {
        this.TransactionType = TransactionType;
    }

    private String TransactionAmount;

    public String getTransactionAmount() {
        return TransactionAmount;
    }

    public void setTransactionAmount(String TransactionAmount) {
        this.TransactionAmount = TransactionAmount;
    }

    private String TransactionDate;

    public String getTransactionDate() {
        return TransactionDate;
    }

    public void setTransactionDate(String TransactionDate) {
        this.TransactionDate = TransactionDate;
    }

    private String TransactionNotes;

    public String getTransactionNotes() {
        return TransactionNotes;
    }

    public void setTransactionNotes(String TransactionNotes) {
        this.TransactionNotes = TransactionNotes;
    }

    private String TransactionNotesAr;

    public String getTransactionNotesAr() {
        return TransactionNotesAr;
    }

    public void setTransactionNotesAr(String TransactionNotesAr) {
        this.TransactionNotesAr = TransactionNotesAr;
    }

    private String TransactionMode;

    public String getTransactionMode() {
        return TransactionMode;
    }

    public void setTransactionMode(String TransactionMode) {
        this.TransactionMode = TransactionMode;
    }
}

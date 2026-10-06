package com.instaguard.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UniqeIdResModel {

        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("result")
        @Expose
        private ArrayList<String> result;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public ArrayList<String> getResult() {
            return result;
        }

        public void setResult(ArrayList<String> result) {
            this.result = result;
        }


}

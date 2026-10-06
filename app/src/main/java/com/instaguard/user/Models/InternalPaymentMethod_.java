package com.instaguard.user.Models;

public class InternalPaymentMethod_ {
   private String id;
   private String name;
   private String key;
   private int status;
   private boolean bgpay;

   public InternalPaymentMethod_(String id, String name, String key, int status/*,boolean bgpay*/) {
      this.id = id;
      this.name = name;
      this.key = key;
      this.status = status;
      //this.bgpay = bgpay;
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getKey() {
      return key;
   }

   public void setKey(String key) {
      this.key = key;
   }

   public int getStatus() {
      return status;
   }

   public void setStatus(int status) {
      this.status = status;
   }

   public void setBgpay(boolean bgpay) {
      this.bgpay = bgpay;
   }

   public boolean isBgpay() {
      return bgpay;
   }

   // Getters and setters
}
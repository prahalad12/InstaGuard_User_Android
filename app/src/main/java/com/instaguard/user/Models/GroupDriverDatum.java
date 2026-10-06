package com.instaguard.user.Models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupDriverDatum {

   @SerializedName("id")
   @Expose
   private Integer id;
   @SerializedName("first_name")
   @Expose
   private String firstName;
   @SerializedName("last_name")
   @Expose
   private String lastName;
   @SerializedName("email")
   @Expose
   private String email;
   @SerializedName("phone_number")
   @Expose
   private Long phoneNumber;
   @SerializedName("avatar")
   @Expose
   private Object avatar;

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public String getFirstName() {
      return firstName;
   }

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   public String getLastName() {
      return lastName;
   }

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public Long getPhoneNumber() {
      return phoneNumber;
   }

   public void setPhoneNumber(Long phoneNumber) {
      this.phoneNumber = phoneNumber;
   }

   public Object getAvatar() {
      return avatar;
   }

   public void setAvatar(Object avatar) {
      this.avatar = avatar;
   }

}
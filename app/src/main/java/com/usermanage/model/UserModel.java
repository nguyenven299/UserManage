package com.usermanage.model;

import android.util.Patterns;

import androidx.lifecycle.LiveData;

public class UserModel  {
    private String Uid = "";
    private String Email = "";
    private String Name = "";
    private String Birthday = "";
    private String PhoneNumber = "";
    private String Avatar = "";

    public UserModel() {
    }

    public UserModel(String uid, String email, String name, String birtday, String phoneNumber, String avatar) {
        Uid = uid;
        Email = email;
        Name = name;
        Birthday = birtday;
        PhoneNumber = phoneNumber;
        Avatar = avatar;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }


    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birtday) {
        Birthday = birtday;
    }

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }

    public boolean isEmailValid() {
        return Patterns.EMAIL_ADDRESS.matcher(getEmail()).matches();
    }


}

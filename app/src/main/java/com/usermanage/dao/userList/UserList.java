package com.usermanage.dao.userList;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserList {

    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo
    public String Uid = "";
    @ColumnInfo(name = "Email")
    public String Email = "";
    @ColumnInfo(name = "Name")
    public String Name = "";
    @ColumnInfo(name = "BirthDay")
    public String Birthday = "";
    @ColumnInfo(name = "PhoneNumber")
    public String PhoneNumber = "";

    public UserList() {
    }

    public UserList(int id, String uid, String email, String name, String birthday, String phoneNumber, String avatar) {
        this.id = id;
        Uid = uid;
        Email = email;
        Name = name;
        Birthday = birthday;
        PhoneNumber = phoneNumber;
        Avatar = avatar;
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

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ColumnInfo(name = "Avatar")
    public String Avatar = "";
}

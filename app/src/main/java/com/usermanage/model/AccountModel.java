package com.usermanage.model;

public class AccountModel {
    private String Uid;
    private String Account;
    private String Password;
    private String RePassword;
    private String NewPassword;
    public void setNewPassword(String newPassword) {
        NewPassword = newPassword;
    }

    public AccountModel(String uid, String account, String password, String rePassword, String newPassword) {
        Uid = uid;
        Account = account;
        Password = password;
        RePassword = rePassword;
        NewPassword = newPassword;
    }

    public String getNewPassword() {
        return NewPassword;
    }

    public AccountModel() {
    }

    public String getRePassword() {
        return RePassword;
    }

    public void setRePassword(String rePassword) {
        RePassword = rePassword;
    }


    public String getAccount() {
        return Account;
    }

    public void setAccount(String account) {
        Account = account;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }
}

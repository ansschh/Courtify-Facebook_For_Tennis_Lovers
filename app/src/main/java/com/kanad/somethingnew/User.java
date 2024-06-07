package com.kanad.somethingnew;

public class User {
    public String fullname, ph_number, email, password,msgToken;

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPh_number() {
        return ph_number;
    }

    public void setPh_number(String ph_number) {
        this.ph_number = ph_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMsgToken() {
        return msgToken;
    }

    public void setMsgToken(String msgToken) {
        this.msgToken = msgToken;
    }

    public User(String fullname, String ph_number, String email, String password, String msgToken) {
        this.fullname = fullname;
        this.ph_number = ph_number;
        this.email = email;
        this.password = password;
        this.msgToken = msgToken;
    }

    public User() {
    }
}

package com.kanad.somethingnew;

public class ComplaintModel {

    String name;
    String email;
    String subject;
    String message;
    String uid;

    public ComplaintModel() {
    }

    public ComplaintModel(String name, String email, String subject, String message, String uid) {
        this.name = name;
        this.email = email;
        this.subject = subject;
        this.message = message;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

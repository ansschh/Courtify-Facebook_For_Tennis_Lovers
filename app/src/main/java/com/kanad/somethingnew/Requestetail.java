package com.kanad.somethingnew;

public class Requestetail {
    String uid,status;

    public Requestetail() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Requestetail(String uid, String status) {
        this.uid = uid;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

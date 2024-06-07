package com.kanad.somethingnew;

public class FriendRequests {

    String age,gender,skill,zipcode,ageRange, uid,url;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public FriendRequests(String age, String gender, String skill, String zipcode, String ageRange, String uid, String url) {
        this.age = age;
        this.gender = gender;
        this.skill = skill;
        this.zipcode = zipcode;
        this.ageRange = ageRange;
        this.uid = uid;
        this.url = url;
    }

    public FriendRequests() {
    }
}

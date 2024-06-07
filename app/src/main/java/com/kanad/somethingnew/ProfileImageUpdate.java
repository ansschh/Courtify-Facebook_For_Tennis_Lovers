package com.kanad.somethingnew;

public class ProfileImageUpdate {
    String zipcode, age, gender, skill,ageRange,url,Uid;

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

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

    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public ProfileImageUpdate(String zipcode, String age, String gender, String skill, String ageRange, String url, String uid) {
        this.zipcode = zipcode;
        this.age = age;
        this.gender = gender;
        this.skill = skill;
        this.ageRange = ageRange;
        this.url = url;
        Uid = uid;
    }

    public ProfileImageUpdate() {
    }
}

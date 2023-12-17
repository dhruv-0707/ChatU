package com.example.chatu;

public class DataModel {
    String uid;
    String name;
    String phone;
    String email;
    String status;
    String profile;
    public DataModel(String uid,String nametxt, String phonetxt, String emailtxt,String statustxt) {
        this.uid = uid;
        name = nametxt;
        phone = phonetxt;
        email = emailtxt;
        status = statustxt;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStatus() {
        return status;
    }

    public String getProfile() {
        return profile;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }


    public  DataModel(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

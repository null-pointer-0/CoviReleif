package com.example.covirelief.Model;

import android.content.SharedPreferences;

public class Entry {
    String name;
    String address;
    String h_name;
    String contact;
    double time;
    String need;
    String descrip;
    String uid;
    String secret;
    long till;
    public Entry(){}

    public long getTill() {
        return till;
    }

    public void setTill(long till) {
        this.till = till;
    }

    public Entry(String name,
                 String address,
                 String h_name,
                 String contact,
                 double time,
                 String need,
                 String descrip,
                 String uid,
                 String secret,
                 long till){
        this.address = address;
        this.name = name;
        this.contact = contact;
        this.h_name = h_name;
        this.need = need;
        if(time == 0){
            this.time = 72;
        }else{
            this.time = time;
        }
        if(descrip == null || descrip.isEmpty()){
            this.descrip = "";
        }else{
            this.descrip = descrip;
        }
        this.uid = uid;
        this.secret = secret;
        this.till = till;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getH_name() {
        return h_name;
    }

    public void setH_name(String h_name) {
        this.h_name = h_name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public String getNeed() {
        return need;
    }

    public void setNeed(String need) {
        this.need = need;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }
}

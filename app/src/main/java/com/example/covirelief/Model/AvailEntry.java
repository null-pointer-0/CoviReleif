package com.example.covirelief.Model;

public class AvailEntry {
    String s_name, s_provider, s_contact, s_address, description ,uid,secret;
    public AvailEntry(){

    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public AvailEntry(String s_name,
                      String s_provider,
                      String s_contact,
                      String s_address,
                      String description,
                      String uid,
                      String secret){
        this.s_name=s_name;
        this.s_address=s_address;
        this.s_contact=s_contact;
        this.s_provider=s_provider;
        if(description == null || description.isEmpty()){
            this.description="";
        }else
            this.description = description;
        this.uid = uid;
        this.secret = secret;
    }

    public String getS_name() {
        return s_name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setS_name(String s_name) {
        this.s_name = s_name;
    }

    public String getS_provider() {
        return s_provider;
    }

    public void setS_provider(String s_provider) {
        this.s_provider = s_provider;
    }

    public String getS_contact() {
        return s_contact;
    }

    public void setS_contact(String s_contact) {
        this.s_contact = s_contact;
    }

    public String getS_address() {
        return s_address;
    }

    public void setS_address(String s_address) {
        this.s_address = s_address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

package com.example.covirelief.Model;

public class Message {
    String msg;
    String sender_uid;
    public Message(){

    }
    public Message(String s,String uid){
        this.msg = s;
        this.sender_uid = uid;
    }

    public String getSender_uid() {
        return sender_uid;
    }

    public void setSender_uid(String sender_uid) {
        this.sender_uid = sender_uid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

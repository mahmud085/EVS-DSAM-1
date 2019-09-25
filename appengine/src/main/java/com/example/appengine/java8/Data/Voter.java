package com.example.appengine.java8.Data;

import com.google.appengine.api.datastore.Key;
//TODO add property for unique token
public class Voter {
    private Boolean emailSent;
    private Boolean reminder;
    Key key;
    String email;
    String name;
    String token;
    Boolean isVoted;

    public Voter(Key key, String email, String name, String token, Boolean emailSent, Boolean reminder, Boolean isVoted) {
        this.key=key;
        this.email=email;
        this.name=name;
        this.token = token;
        this.emailSent=emailSent;
        this.reminder=reminder;
        this.isVoted=isVoted;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() { return this.name; }

    public void setName(String name) {  this.name = name; }

    public String getToken() { return this.token; }

    public void setToken(String token) {  this.token = token; }

    public Boolean getEmailSent() {
        return emailSent;
    }

    public void setEmailSent(boolean emailSent) {
        this.emailSent=emailSent;
    }

    public Boolean getReminder() {
        return reminder;
    }

    public void setReminder(boolean reminder) {
        this.reminder=reminder;
    }

    public Boolean getVoted() {return isVoted;}

    public void setVoted(Boolean voted) {isVoted = voted;}

}

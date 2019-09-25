package com.example.appengine.java8.Data;

import com.google.appengine.api.datastore.Key;

import java.io.Serializable;

public class Candidate implements Serializable {


    String firstname;
    String surname;
    String faculty;
    Key key;
    Long votecount;

    public Candidate(Key key, String firstname, String surname, String faculty, Long votecount) {
        this.key=key;
        this.firstname = firstname;
        this.surname = surname;
        this.faculty = faculty;
        this.votecount=votecount;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public Key getKey() {
        return key;
    }
    public void setKey(Key key) {
        this.key = key;
    }


    public Long getVotecount() {
        return votecount;
    }

    public void setVotecount(Long votecount) {
        this.votecount = votecount;
    }


}

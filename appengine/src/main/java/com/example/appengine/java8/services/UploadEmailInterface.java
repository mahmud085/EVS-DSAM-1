package com.example.appengine.java8.services;

import com.example.appengine.java8.customexceptions.VotingException;

public interface UploadEmailInterface {
    void uploadFile(String []data) throws VotingException;
}

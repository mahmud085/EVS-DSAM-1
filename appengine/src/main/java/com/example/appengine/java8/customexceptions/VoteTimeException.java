package com.example.appengine.java8.customexceptions;


public class VoteTimeException extends Exception  {

    public VoteTimeException(String message) {
        super(message);
    }

}

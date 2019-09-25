package com.example.appengine.java8.services;

import com.example.appengine.java8.Data.VoteTime;
import com.example.appengine.java8.customexceptions.VoteTimeException;

import java.util.Date;


public interface VoteTimeInterface {
    VoteTime getVoteTimings() throws VoteTimeException;

    void create(Date startDateString, Date endDateString) throws VoteTimeException;

    void update(Date startDateString, Date endDateString) throws VoteTimeException;
}

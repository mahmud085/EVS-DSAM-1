package com.example.appengine.java8.services;

import com.example.appengine.java8.Data.Voter;
import com.example.appengine.java8.customexceptions.CandidateException;
import com.example.appengine.java8.customexceptions.VotingException;

import java.security.Principal;
import java.util.List;

public interface VotingService {

    List<Voter> getVoterList() throws VotingException;

    void addVoter(String name, String email) throws VotingException;

    Boolean updateVoter(String id, String name, String email,String token,
                        Boolean emailSent, Boolean reminder) throws VotingException;

    Boolean delete(String id) throws VotingException;

    List<Voter> getUnnotifiedVoters() throws VotingException;

    List<Voter> getVoterListForReminderEmail() throws VotingException;

    int getPendingVoterCount();

    int getCastedVoterCount();

    Boolean castVoteWithToken(String candidateid, String token) throws VotingException,CandidateException;

    Voter getVoterByEmail(String email) throws VotingException;
}

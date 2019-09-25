package com.example.appengine.java8.services;

import com.example.appengine.java8.Data.Candidate;
import com.example.appengine.java8.customexceptions.CandidateException;
import com.example.appengine.java8.customexceptions.VotingException;

import java.util.List;

public interface CandidateService {

    List<Candidate> getAllCandidates() throws CandidateException;

    void createCandidate(String firstname, String surname, String faculty) throws CandidateException;

    boolean deleteCandidate(String id) throws CandidateException;

    Boolean updateCandidate(String id, String firstname, String surname, String faculty) throws CandidateException;


    Candidate getCandidateAndUpdateCount(String candidateId) throws CandidateException, VotingException;
}

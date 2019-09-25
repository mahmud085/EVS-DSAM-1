package com.example.appengine.java8.services;

import com.example.appengine.java8.Data.Candidate;
import com.example.appengine.java8.customexceptions.CandidateException;
import com.example.appengine.java8.helper.Constants;
import com.google.appengine.api.datastore.*;

import java.util.ArrayList;
import java.util.List;

public class CandidateManagement implements CandidateService {

    private String firstnameProperty = Constants.CANDIDATE_FIRSTNAME_PROPERTY;
    private String surnameProperty;
    private String facultyProperty;
    private boolean valid;
    private boolean invalid;
    private String candidatekind;
    private Key electionKey;
    private Transaction txn;
    private String votecountProperty;

    public CandidateManagement() {
        surnameProperty = Constants.CANDIDATE_SURNAME_PROPERTY;
        facultyProperty = Constants.CANDIDATE_FACULTY_PROPERTY;
        votecountProperty = Constants.CANDIDATE_VOTECOUNT_PROPERTY;
        valid = Constants.VALID;
        invalid = Constants.INVALID;
        candidatekind = Constants.CANDIDATE;
        electionKey = KeyFactory.createKey(Constants.ROOTKIND, Constants.ROOTANCESTOR);
    }


    /**
     * Retreive candidates
     *
     * @return candidate list
     * @throws CandidateException
     */
    @Override
    public List<Candidate> getAllCandidates() throws CandidateException {
        List<Candidate> candidates;
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Query q;
        q = new Query(candidatekind).addSort(votecountProperty, Query.SortDirection.DESCENDING);
        PreparedQuery pq = ds.prepare(q);
        List<Entity> candidateEntities = pq.asList(FetchOptions.Builder.withDefaults());
        candidates = convert(candidateEntities);
        return candidates;
    }

    /**
     * create candidate
     *
     * @param firstname
     * @param surname
     * @param faculty
     * @throws CandidateException
     */
    @Override
    public void createCandidate(String firstname, String surname, String faculty)
            throws CandidateException {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        checkFirstName(firstname);
        checkSurname(surname);
        checkFaculty(faculty);

        txn = ds.beginTransaction();
        try {
            Entity candidate = new Entity(candidatekind, electionKey);
            candidate.setProperty(firstnameProperty, firstname);
            candidate.setProperty(surnameProperty, surname);
            candidate.setProperty(facultyProperty, faculty);
            candidate.setProperty(votecountProperty, 0);
            ds.put(txn, candidate);
            txn.commit();
        } finally {
            if (txn.isActive()) {
                txn.rollback();
            }
        }
    }


    /**
     * delete candidate
     *
     * @param id
     * @return true if deletion successful
     * @throws CandidateException
     */
    @Override
    public boolean deleteCandidate(String id) throws CandidateException {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        checkId(id);
        Key k = KeyFactory.createKey(electionKey, candidatekind, Long.valueOf(id));
        txn = ds.beginTransaction();
        try {
            ds.delete(txn, k);
            txn.commit();
            return valid;
        } finally {
            if (txn.isActive()) {
                txn.rollback();
            }
        }
    }

    /**
     * updates candidates
     *
     * @param id
     * @param firstname
     * @param surname
     * @param faculty
     * @return true if update successful
     * @throws CandidateException
     */
    @Override
    public Boolean updateCandidate(String id, String firstname, String surname, String faculty) throws CandidateException {

        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Key k = KeyFactory.createKey(electionKey, candidatekind, Long.valueOf(id));
        Entity candidate;
        checkFirstName(firstname);
        checkSurname(surname);
        checkFaculty(faculty);
        txn = ds.beginTransaction();
        try {
            candidate = ds.get(k);
            candidate.setProperty(firstnameProperty, firstname);
            candidate.setProperty(surnameProperty, surname);
            candidate.setProperty(facultyProperty, faculty);
            ds.put(txn, candidate);
            txn.commit();
            return valid;
        } catch (EntityNotFoundException e) {
            throw new CandidateException("No such entity "+e.getMessage());
        } finally {
            if (txn.isActive()) {
                txn.rollback();
            }
        }
    }

    /**
     * Converts every item in list to corresponding object
     *
     * @param candidateEntities
     * @return converted list
     * @throws CandidateException
     */
    private List<Candidate> convert(List<Entity> candidateEntities) throws CandidateException {

        List<Candidate> candidates = new ArrayList<>();
        if (!candidateEntities.isEmpty()) {
            for (Entity candidateEntity : candidateEntities) {
                try {
                    Candidate candidate = new Candidate(candidateEntity.getKey(),
                            (String) candidateEntity.getProperty(firstnameProperty),
                            (String) candidateEntity.getProperty(surnameProperty),
                            (String) candidateEntity.getProperty(facultyProperty),
                            (Long) candidateEntity.getProperty(votecountProperty));
                    candidates.add(candidate);
                } catch (Exception e) {
                    throw new CandidateException(e.getMessage());
                }
            }
        }
        return candidates;
    }

    /**
     * update votecount for candidate
     * @param candidateId
     * @return candidate after incrementing vote
     * @throws CandidateException
     */
    @Override
    public Candidate getCandidateAndUpdateCount(String candidateId) throws CandidateException {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Candidate candres = null;
        txn = ds.beginTransaction();
        Key k = KeyFactory.createKey(electionKey, candidatekind, Long.valueOf(candidateId));
        try {
            Entity candidate = ds.get(k);
            long votecount = (Long)candidate.getProperty(votecountProperty);
            votecount++;
            candidate.setProperty(votecountProperty, votecount);
            ds.put(txn, candidate);
            txn.commit();
            List<Candidate> candidates = convert(java.util.Arrays.asList(candidate));
            candres = candidates.get(0);
        } catch (EntityNotFoundException e) {
            throw new CandidateException("Invalid candidate");
        }

        return candres;
    }

    /*VALIDATIONS*/
    private boolean checkString(String stringval) {
        if (stringval.length() == 0)
            return invalid;
        else
            return valid;
    }

    private void checkFaculty(String faculty) throws CandidateException {
        if (!checkString(faculty))
            throw new CandidateException("Candidate faculty is invalid");
    }

    private void checkSurname(String surname) throws CandidateException {
        if (!checkString(surname))
            throw new CandidateException("Candidate surname is invalid");
    }

    private void checkFirstName(String firstname) throws CandidateException {
        if (!checkString(firstname))
            throw new CandidateException("Candidate firstname is invalid");
    }

    private void checkId(String id) throws CandidateException {
        if (!checkString(id))
            throw new CandidateException("Candidate id is invalid");
    }

}


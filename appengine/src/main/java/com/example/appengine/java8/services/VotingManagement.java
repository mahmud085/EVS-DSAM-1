package com.example.appengine.java8.services;

import com.example.appengine.java8.Data.Candidate;
import com.example.appengine.java8.Data.Voter;
import com.example.appengine.java8.customexceptions.CandidateException;
import com.example.appengine.java8.customexceptions.VotingException;
import com.example.appengine.java8.helper.Constants;
import com.google.appengine.api.datastore.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VotingManagement implements VotingService {
    private Key electionKey;
    private Transaction txn;
    private boolean invalid;
    private boolean valid = Constants.VALID;
    private String voterskind;
    private String nameProperty;
    private String emailProperty;
    private String tokenProperty;
    private String emailSentProperty;
    private String reminderProperty;
    private DatastoreService ds;
    private String isVotedProperty;


    public VotingManagement() {
        electionKey = KeyFactory.createKey(Constants.ROOTKIND, Constants.ROOTANCESTOR);
        invalid = Constants.INVALID;
        voterskind = Constants.VOTERS;
        nameProperty = Constants.VOTER_NAME_PROPERTY;
        emailProperty = Constants.VOTER_EMAIL_PROPERTY;
        tokenProperty = Constants.VOTER_TOKEN_PROPERTY;
        emailSentProperty = Constants.VOTER_EMAILSENT_PROPERTY;
        reminderProperty = Constants.VOTER_REMINDER_PROPERTY;
        isVotedProperty = Constants.VOTER_ISVOTED_PROPERTY;
        ds = DatastoreServiceFactory.getDatastoreService();
    }

    /**
     * fetch voter list
     *
     * @return voters as list
     * @throws VotingException
     */
    @Override
    public List<Voter> getVoterList() throws VotingException {

        List<Voter> voterList;
        Query q = new Query(voterskind).setAncestor(electionKey);
        PreparedQuery pq = ds.prepare(q);
        List<Entity> voters = pq.asList(FetchOptions.Builder.withDefaults());
        voterList = convert(voters);
        return voterList;
    }


    /**
     * creates new voter
     *
     * @param name
     * @param email
     * @throws VotingException
     */
    @Override
    public void addVoter(String name, String email) throws VotingException {

        checkVoterName(name);
        checkVoterEmail(email);
        checkEmailUnique(email, null);
        txn = ds.beginTransaction();
        try {
            Entity voter = new Entity(voterskind, electionKey);
            voter.setProperty(nameProperty, name);
            voter.setProperty(emailProperty, email);
            voter.setProperty(tokenProperty, null);
            voter.setProperty(emailSentProperty, false);
            voter.setProperty(reminderProperty, false);
            voter.setProperty(isVotedProperty, false);
            ds.put(txn, voter);
            txn.commit();
        } finally {
            if (txn.isActive()) {
                txn.rollback();
            }
        }
    }

    private void checkEmailUnique(String email, Key k) throws VotingException {
        Query.FilterPredicate emailfilter=new Query.FilterPredicate(emailProperty, Query.FilterOperator.EQUAL,email);
        Query q = new Query(voterskind).setAncestor(electionKey).setFilter(emailfilter);
        PreparedQuery pq = ds.prepare(q);
        Entity voter = pq.asSingleEntity();
        if(k==null&&voter!=null)
            throw new VotingException("A user with this email already exists");
        else if(k!=null && voter!=null && voter.getKey().getId()!=k.getId())
            throw new VotingException("A user with this email already exists");
    }

    /**
     * converts to object
     *
     * @param voters
     * @return List of converted voter objects
     */
    private List<Voter> convert(List<Entity> voters) throws VotingException {

        List<Voter> voterList = new ArrayList<>();
        try {

            for (Entity voterEntity : voters) {
                Voter voter = new Voter(voterEntity.getKey(),
                        (String) voterEntity.getProperty(emailProperty),
                        (String) voterEntity.getProperty(nameProperty),
                        (String) voterEntity.getProperty(tokenProperty),
                        (Boolean) voterEntity.getProperty(emailSentProperty),
                        (Boolean) voterEntity.getProperty(reminderProperty),
                        (Boolean) voterEntity.getProperty(isVotedProperty));
                voterList.add(voter);
            }

        } catch (Exception e) {
            throw new VotingException("Could not convert entity to object " + e.getMessage());
        }
        return voterList;
    }


    /**
     * updates a voter's details
     *
     * @param id
     * @param name
     * @param email
     * @param emailSent
     * @param reminder
     * @return boolean -true is successful
     * @throws VotingException
     */
    @Override
    public Boolean updateVoter(String id, String name, String email, String token,
                               Boolean emailSent, Boolean reminder) throws VotingException {
        checkVoterId(id);
        Key k = KeyFactory.createKey(electionKey, voterskind, Long.valueOf(id));
        Entity voter;
        checkVoterName(name);
        checkVoterEmail(email);
        checkEmailUnique(email,k);
        txn = ds.beginTransaction();
        try {
            voter = ds.get(k);
            voter.setProperty(nameProperty, name);
            voter.setProperty(emailProperty, email);
            voter.setProperty(tokenProperty, token);
            voter.setProperty(emailSentProperty, emailSent);
            voter.setProperty(reminderProperty, reminder);
            ds.put(txn, voter);
            txn.commit();
            return this.valid;
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (txn.isActive()) {
                txn.rollback();
            }
        }
        return this.invalid;
    }

    /**
     * deletes a voter and returns true is successful
     *
     * @param id
     * @return boolean
     * @throws VotingException
     */
    @Override
    public Boolean delete(String id) throws VotingException {
        checkVoterId(id);
        txn = ds.beginTransaction();
        Key k = KeyFactory.createKey(electionKey, voterskind, Long.valueOf(id));
        try {
            ds.delete(txn, k);
            txn.commit();
            return valid;
        }finally {
            if (txn.isActive()) {
                txn.rollback();
            }
        }
    }


    /*VALIDATIONS*/
    private boolean checkString(String stringval) {
        if (stringval.length() == 0 || stringval.equals(null))
            return this.invalid;
        else
            return this.valid;
    }

    private void checkVoterId(String id) throws VotingException {
        if (!checkString(id))
            throw new VotingException("Voter id is invalid");
    }

    private void checkVoterEmail(String email) throws VotingException {
        Pattern pattern = Pattern.compile("^.+@.+\\..+$");
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches())
            throw new VotingException("voter email is invalid");
    }

    private void checkVoterName(String name) throws VotingException {
        if (!checkString(name))
            throw new VotingException("Voter name is invalid");
    }

    /**
     * returns voters who have not yet received an email regarding the election
     * @return list<Voter>
     * @throws VotingException
     */
    @Override
    public List<Voter> getUnnotifiedVoters() throws VotingException {
        Query.Filter keyFilter = new Query.FilterPredicate(emailSentProperty,
                Query.FilterOperator.EQUAL, false);
        Query query = new Query(voterskind).setAncestor(electionKey).setFilter(keyFilter);
        PreparedQuery pq1 = ds.prepare(query);
        List<Entity> voters = pq1.asList(FetchOptions.Builder.withDefaults());
        List<Voter> voterList = convert(voters);
        return voterList;
    }

    /**
     * returns voters who have not yet received a reminder email
     * @return list<Voter>
     * @throws VotingException
     */
    @Override
    public List<Voter> getVoterListForReminderEmail() throws VotingException {
        Query.Filter keyFilter = new Query.FilterPredicate(reminderProperty,
                Query.FilterOperator.EQUAL, false);
        Query q = new Query(Constants.VOTERS).setAncestor(electionKey).setFilter(keyFilter);
        PreparedQuery pq1 = ds.prepare(q);
        List<Entity> voters = pq1.asList(FetchOptions.Builder.withDefaults());
        List<Voter> voterList = convert(voters);
        return voterList;
    }

    /**
     *
     * @return count of voters who have not voted
     */
    @Override
    public int getPendingVoterCount() {
        Query.Filter keyFilter = new Query.FilterPredicate(isVotedProperty,
                Query.FilterOperator.EQUAL, false);
        Query q = new Query(Constants.VOTERS).setAncestor(electionKey).setFilter(keyFilter);
        PreparedQuery pq1 = ds.prepare(q);
        List<Entity> voters = pq1.asList(FetchOptions.Builder.withDefaults());
        return voters.size();
    }

    /**
     *
     * @return count of voters/votes casted
     */
    @Override
    public int getCastedVoterCount() {
        Query.Filter keyFilter = new Query.FilterPredicate(isVotedProperty,
                Query.FilterOperator.EQUAL, true);
        Query q = new Query(Constants.VOTERS).setAncestor(electionKey).setFilter(keyFilter);
        PreparedQuery pq1 = ds.prepare(q);
        List<Entity> voters = pq1.asList(FetchOptions.Builder.withDefaults());
        return voters.size();
    }

    /**
     *
     * @param candidateid
     * @param token unique token
     * @return
     * @throws CandidateException
     */
    @Override
    public Boolean castVoteWithToken(String candidateid, String token) throws VotingException,CandidateException {

        Candidate v;
        txn = ds.beginTransaction();
        try {
            //voter list: with matched token ID and isvoted false
            Query.Filter tokenfilter = new Query.FilterPredicate(tokenProperty,
                    Query.FilterOperator.EQUAL, token);
            Query.Filter isvotedfilter = new Query.FilterPredicate(isVotedProperty,
                    Query.FilterOperator.EQUAL, false);
            Query.Filter tokenandvoted =
                    Query.CompositeFilterOperator.and(tokenfilter, isvotedfilter);
            Query q = new Query(voterskind).setAncestor(electionKey).setFilter(tokenandvoted);
            PreparedQuery pq1 = ds.prepare(q);
            Entity voter = pq1.asSingleEntity();

            // candidate list, to whom vote was casted
            if (voter != null) {
                CandidateManagement candidateManagement = new CandidateManagement();
                v = candidateManagement.getCandidateAndUpdateCount(candidateid);
                if (v != null) {
                    voter.setProperty(isVotedProperty, true);
                    ds.put(txn, voter);
                }
            } else {
                throw new VotingException("If you have voted please come back after the voting period to see" +
                        " the results.If not make sure you are a registered voter");
            }
            txn.commit();
            return valid;
        }finally {
            if (txn.isActive()) {
                txn.rollback();
            }
        }
    }

    @Override
    public Voter getVoterByEmail(String email) throws VotingException {
        checkVoterEmail(email);
        Query.FilterPredicate emailfilter=new Query.FilterPredicate(emailProperty, Query.FilterOperator.EQUAL,email);
        Query q = new Query(voterskind).setAncestor(electionKey).setFilter(emailfilter);
        PreparedQuery pq = ds.prepare(q);
        Entity voter = pq.asSingleEntity();

        Voter voter1=voter!=null?convert(Arrays.asList(voter)).get(0):null;
        return voter1;

    }
}


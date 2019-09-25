package com.example.appengine.java8.services;

import com.example.appengine.java8.Data.VoteTime;
import com.example.appengine.java8.customexceptions.VoteTimeException;
import com.example.appengine.java8.customexceptions.VotingException;
import com.example.appengine.java8.helper.Constants;
import com.google.appengine.api.datastore.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VoteTimeManagement implements VoteTimeInterface {


    private String votetimingkind;
    private String startDateProperty;
    private String endDateProperty;
    private Key electionKey;
    private Transaction txn;

    public VoteTimeManagement() {
        endDateProperty = Constants.VOTETIMING_ENDDATE_PROPERTY;
        startDateProperty = Constants.VOTETIMING_STARTDATE_PROPERTY;
        electionKey = KeyFactory.createKey(Constants.ROOTKIND, Constants.ROOTANCESTOR);
        votetimingkind = Constants.VOTETIMING;
    }

    //VOTE TIMING MANAGEMENT

    /**
     * fetch startdate and enddate
     *
     * @return VoteTime
     */
    @Override
    public VoteTime getVoteTimings() throws VoteTimeException {
        VoteTime voteTime = null;
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Query q = new Query(votetimingkind).setAncestor(electionKey);
        PreparedQuery pq = ds.prepare(q);
        Entity entity = pq.asSingleEntity();
        if(entity!=null)
        voteTime = convert(entity);
        return voteTime;
    }

    private VoteTime convert(Entity entity){
        try {
            return new VoteTime((Date)entity.getProperty(startDateProperty),
                    (Date)entity.getProperty(endDateProperty));

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * create vote timing
     *
     * @param startDateString
     * @param endDateString
     * @throws VotingException
     */
    @Override
    public void create(Date startDateString, Date endDateString) throws VoteTimeException {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        txn = ds.beginTransaction();
        try {
            Entity timingEntity = new Entity(votetimingkind, electionKey);
            timingEntity.setProperty(startDateProperty, startDateString);
            timingEntity.setProperty(endDateProperty, endDateString);
            ds.put(txn, timingEntity);
            txn.commit();

        } finally {
            if (txn.isActive()) {
                txn.rollback();
            }
        }
    }


    /**
     * update vote timing
     *
     * @param startDateString
     * @param endDateString
     * @throws VotingException
     */
    @Override
    public void update(Date startDateString, Date endDateString) throws VoteTimeException {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        txn = ds.beginTransaction();
        try {
            Query q = new Query(votetimingkind, electionKey);
            PreparedQuery pq = ds.prepare(q);
            Entity timingEntity = pq.asSingleEntity();
            timingEntity.setProperty(startDateProperty, startDateString);
            timingEntity.setProperty(endDateProperty, endDateString);
            ds.put(txn, timingEntity);
            txn.commit();
        } catch (Exception e) {
            throw new VoteTimeException("Could not update voting schedule " + e.getMessage());
        } finally {
            if (txn.isActive()) {
                txn.rollback();
            }
        }
    }



}

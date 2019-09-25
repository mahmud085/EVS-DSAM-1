package com.example.appengine.java8.services;

import com.example.appengine.java8.Data.Voter;
import com.example.appengine.java8.customexceptions.VotingException;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class UploadEmail implements UploadEmailInterface {
    private static Logger logger = Logger.getLogger
            (UploadEmail.class.getName());

    /**
     * imports csv of voters
     * @param data
     * @throws VotingException
     */
    @Override
    public void uploadFile(String[] data) throws VotingException {
        if(data[0] !=null && data[1]!=null) {
            List<Voter> voterList = null;
            VotingManagement votingService = new VotingManagement();
            try {
                voterList = votingService.getVoterList();
                if (!voterList.stream().filter(o -> o.getEmail().equals(data[1].trim())).findFirst().isPresent())
                    votingService.addVoter(data[0], data[1]);
            } catch (VotingException e) {
                votingService.addVoter(data[0].trim(), data[1].trim());
                logger.severe("Error adding voter" + e.getMessage());
            }
        }
    }
}

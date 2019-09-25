package com.example.appengine.java8.servlet;

import com.example.appengine.java8.Data.VoteTime;
import com.example.appengine.java8.Data.Voter;
import com.example.appengine.java8.customexceptions.VoteTimeException;
import com.example.appengine.java8.customexceptions.VotingException;
import com.example.appengine.java8.helper.Constants;
import com.example.appengine.java8.services.VoteTimeManagement;
import com.example.appengine.java8.services.VotingManagement;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class VotingReminderServlet extends HttpServlet {
    private static Logger logger = Logger.getLogger
            (VotingReminderServlet.class.getName());
    /**
     * checks if it is 1 day to voting
     * sends reminder email by checking boolean reminder
     * @param req
     * @param resp
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        VotingManagement votingManagement = new VotingManagement();
        VoteTimeManagement votetimeManagement = new VoteTimeManagement();
        VoteTime voteTimingEntries=null;
        List<Voter> voters=new ArrayList<>();
        try {
            voters=votingManagement.getVoterListForReminderEmail();
            voteTimingEntries=votetimeManagement.getVoteTimings();
        } catch (VotingException | VoteTimeException e) {
            logger.severe("Could not fetch vote time \n"+e.getMessage());
        }
        //Check and process
        if (voteTimingEntries!= null) {
            Date today = new Date();
            SimpleDateFormat sdf=new SimpleDateFormat(Constants.DateFormat_MM_DD_YYYY_HH_MM_A);
            Date startDate = null;
            try {
                startDate = voteTimingEntries.getStartDate();
            } catch (Exception e) {
                logger.severe(e.getMessage());
            }
            long diffInMillies = Math.abs(Objects.requireNonNull(startDate).getTime() - today.getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);


            //send reminder email if startdate is not before todays date and if
            // there is less than 1 day for voting to begin
            if (diff>=0 && diff <= 1) {

                for (Voter voter : voters) {
                            String email = voter.getEmail();
                            Properties props = new Properties();
                            Session session = Session.getDefaultInstance(props, null);
                            try {
                                Message msg = new MimeMessage(session);
                                msg.setFrom(new InternetAddress(Constants.EMAILSENDER_AUTHORIZED));
                                msg.addRecipient(Message.RecipientType.TO,
                                        new InternetAddress(email));
                                msg.setSubject(Constants.MSG_SUBJ_VOTING_REMINDER);
                                String URLpath=req.getScheme()+"://"+req.getServerName()+":"+req.getServerPort()+ Constants.USERS_VOTING_INTERFACE;
                                msg.setContent(
                                        "<h3>Dear "+ voter.getName() + "</h3><br>" +
                                                "Here we go!! Finally Our University Election is tomorrow!!." +
                                                "You are cordially invited to cast your vote." +
                                                "We have already sent you an email where you can find your unique token and the link to give your vote.<br>Or simply <a href='"+URLpath+"'>Click Here</a><br>" +
                                                "And don't forget to use the token when casting your vote.<br>"+
                                                "<br>You can use the same link to see the voting results after the voting period ends</br>"+
                                                "<br>Regards<br>Voting Team",
                                        "text/html");
                                Transport.send(msg);
                                voter.setReminder(true);
                                votingManagement.updateVoter(voter.getKey().getId() + "",voter.getName(),voter.getEmail(),
                                        voter.getToken(),voter.getEmailSent(),voter.getReminder());
                            } catch (MessagingException | VotingException e) {
                                logger.severe("Could not send reminder email \n"+e.getMessage());
                            }
                }
            }
        }
    }
}

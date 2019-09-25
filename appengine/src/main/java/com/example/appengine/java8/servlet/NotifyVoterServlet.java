package com.example.appengine.java8.servlet;

import com.example.appengine.java8.Data.VoteTime;
import com.example.appengine.java8.Data.Voter;
import com.example.appengine.java8.customexceptions.VotingException;
import com.example.appengine.java8.helper.Constants;
import com.example.appengine.java8.services.VoteTimeManagement;
import com.example.appengine.java8.services.VotingManagement;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

public class NotifyVoterServlet extends HttpServlet {
    private static Logger logger = Logger.getLogger
            (NotifyVoterServlet.class.getName());
    private String message;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        VotingManagement voterManagement = new VotingManagement();
        VoteTimeManagement votetimeManagement = new VoteTimeManagement();
        SimpleDateFormat sdf=new SimpleDateFormat(Constants.DateFormat_MM_DD_YYYY_HH_MM_A);
        String sDate = "", eDate="" ;
        VoteTime voteTimingEntries;
        List<Voter> voters = new ArrayList<>();

        try {
            voters = voterManagement.getUnnotifiedVoters();
            voteTimingEntries = votetimeManagement.getVoteTimings();
            sDate = sdf.format(voteTimingEntries.getStartDate());
            eDate = sdf.format(voteTimingEntries.getEndDate());
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }

        for (Voter voter : voters) {
            String email = voter.getEmail();
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);
            try {
                String token = UUID.randomUUID().toString().substring(28);
                Message msg = new MimeMessage(session);
                msg.setFrom(new InternetAddress(Constants.EMAILSENDER_AUTHORIZED));
                msg.addRecipient(Message.RecipientType.TO,new InternetAddress(email));
                String URLpath = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort()
                        + Constants.USERS_VOTING_INTERFACE;
                msg.setSubject(Constants.MSG_SUBJ_VOTING_NOTIFICATION);
                msg.setContent(
                        "<h3>Dear " + voter.getName() + "</h3><br>" +
                                "It is our pleasure to inform you that our University Election is nearby." +
                                "You are cordially invited to cast your vote because you know every vote counts." +
                                "The electronic voting will start at " + sDate +
                                " and will end at " + eDate + ".Please visit the link below to cast your vote<br>" +
                                "&nbsp;&nbsp;<a href='" + URLpath + "'>Vote Now</a><br>" +
                                "or you can simply copy this URL(<a href='" + URLpath + "'>" + URLpath + "</a>)in your browser." +
                                "Please use this token <b>" + token + "</b> when you give your valuable vote<br>" +
                                "<br>You can use the same link to see the voting results after the voting period ends</br>" +
                                "<br>Regards<br>Voting Team",
                        "text/html");
                Transport.send(msg);
                voter.setEmailSent(true);
                voterManagement.updateVoter(voter.getKey().getId() + "", voter.getName(), voter.getEmail(), token,
                        voter.getEmailSent(), voter.getReminder());
                 message=String.valueOf(Constants.VALID);
            }catch(VotingException e){
                logger.severe(e.getMessage());
                message=e.getMessage();
            } catch (AddressException e) {
                logger.severe("invalid email address " + e.getMessage());
                message="invalid email address " + e.getMessage();
            } catch (MessagingException e) {
                logger.severe("Could not send email  " + e.getMessage());
                message="Could not send email  " + e.getMessage();
            } catch (Exception e) {
                logger.severe("Email sending failed  " + e.getMessage());
                message="Email sending failed  " + e.getMessage();
            } finally {
                resp.getWriter().write(message);
            }
        }
    }
}

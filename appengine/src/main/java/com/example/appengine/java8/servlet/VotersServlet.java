package com.example.appengine.java8.servlet;

import com.example.appengine.java8.Data.Voter;
import com.example.appengine.java8.customexceptions.VotingException;
import com.example.appengine.java8.helper.Constants;
import com.example.appengine.java8.services.VotingManagement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class VotersServlet extends HttpServlet {

    private static Logger logger = Logger.getLogger
            (VotersServlet.class.getName());

    private String nameProperty= Constants.VOTER_NAME_PROPERTY;
    private String emailProperty= Constants.VOTER_EMAIL_PROPERTY;
    private String idProperty= Constants.VOTER_ID_PROPERTY;

    /**
     * fetch voters
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        VotingManagement votingService=new VotingManagement();
        List<Voter> voterList=new ArrayList<>();
        try {
            voterList = votingService.getVoterList();
        }catch (VotingException e){
            logger.severe("Error fetching voters" + e.getMessage());
        }
        req.setAttribute("voterList",voterList);
        req.getRequestDispatcher("/voterMgmt.jsp").forward(req, resp);
    }

    /**
     * add voter
     *  voter name
     *  voteremail
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String name = req.getParameter(nameProperty);
            String email = req.getParameter(emailProperty);
            VotingManagement votingService = new VotingManagement();
            votingService.addVoter(name, email);
        }catch (VotingException e){
            logger.severe("Error adding voter" + e.getMessage());
            resp.getWriter().write(e.getMessage());
        }
    }

    /**
     * update voter
     *  voter id extracted from key
     *  voter attributes to be updated
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        boolean res=false;
        try {
            String id = req.getParameter(idProperty);
            String name = req.getParameter(nameProperty);
            String email = req.getParameter(emailProperty);
            String token = req.getParameter(Constants.VOTER_TOKEN_PROPERTY);
            Boolean emailSent = Boolean.valueOf(req.getParameter(Constants.VOTER_EMAILSENT_PROPERTY));
            Boolean reminder = Boolean.valueOf(req.getParameter(Constants.VOTER_REMINDER_PROPERTY));
            VotingManagement votingService = new VotingManagement();
            res = votingService.updateVoter(id, name, email,token,emailSent,
                    reminder);

            resp.getWriter().write(String.valueOf(res));
        }catch (VotingException e){
            logger.severe("Error updating voter" + e.getMessage());
            resp.getWriter().write(e.getMessage());
        }

    }

    /**
     * delete voter
     *  voter id extracted from key
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        boolean result=false;
        try {
            String id = req.getParameter(idProperty);
            VotingManagement votingService = new VotingManagement();
            result = votingService.delete(id);
            resp.getWriter().write(String.valueOf(result));
        }catch (VotingException e){
            logger.severe("Error deleting voter" + e.getMessage());
            resp.getWriter().write(e.getMessage());
        }
    }
}

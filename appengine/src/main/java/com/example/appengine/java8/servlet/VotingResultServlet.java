package com.example.appengine.java8.servlet;


import com.example.appengine.java8.Data.Candidate;
import com.example.appengine.java8.Data.VoteTime;
import com.example.appengine.java8.Data.Voter;
import com.example.appengine.java8.customexceptions.CandidateException;
import com.example.appengine.java8.customexceptions.VoteTimeException;
import com.example.appengine.java8.customexceptions.VotingException;
import com.example.appengine.java8.helper.Constants;
import com.example.appengine.java8.services.CandidateManagement;
import com.example.appengine.java8.services.VoteTimeInterface;
import com.example.appengine.java8.services.VoteTimeManagement;
import com.example.appengine.java8.services.VotingManagement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;


public class VotingResultServlet extends HttpServlet {
    private static Logger logger = Logger.getLogger
            (VotingResultServlet.class.getName());

    /**
     * shows voting results if past voting period.else shows notice
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        VotingManagement votingService = new VotingManagement();
        CandidateManagement candidateManagement = new CandidateManagement();
        List<Candidate> candidateList = new ArrayList<>();

        try {
            candidateList = candidateManagement.getAllCandidates();
        } catch (CandidateException e) {
            e.printStackTrace();
        }
        HashMap<String, Integer> stats = new HashMap<>();
        try {
            VoteTimeManagement voteTimeManagement = new VoteTimeManagement();
            VoteTime voteTime = voteTimeManagement.getVoteTimings();
            Calendar cal = Calendar.getInstance(); // creates calendar
            cal.setTime(new Date()); // sets calendar time/date
            cal.add(Calendar.HOUR_OF_DAY, 1); // adds one hour
            Date today=cal.getTime();
            Date eDate = voteTime.getEndDate();
            //if after voting period
            if (today.after(eDate)) {
                Integer castedvoterCount = votingService.getCastedVoterCount(),
                        allvoterCount = votingService.getVoterList().size(),
                        pendingvoterCount = votingService.getPendingVoterCount();
                stats.put("total_voters", allvoterCount);
                stats.put("pending_voters", pendingvoterCount);
                stats.put("casted_voters", castedvoterCount);

                float voterpercentage = ((float) castedvoterCount / (float) allvoterCount) * 100;
                req.setAttribute("votingpercentage", voterpercentage);

                req.setAttribute("candidateList", candidateList);
                req.setAttribute("statistics", stats);
                req.getRequestDispatcher("/votingResults.jsp").forward(req, resp);
            }
            else {
                req.getRequestDispatcher("/votingResultsNotice.jsp").forward(req, resp);
            }
            }catch(VotingException | VoteTimeException e){
                logger.severe("Error fetching voters" + e.getMessage());
            }
        }


    }

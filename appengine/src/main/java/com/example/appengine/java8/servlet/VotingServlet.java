package com.example.appengine.java8.servlet;

import com.example.appengine.java8.Data.Candidate;
import com.example.appengine.java8.Data.VoteTime;
import com.example.appengine.java8.Data.Voter;
import com.example.appengine.java8.customexceptions.CandidateException;
import com.example.appengine.java8.customexceptions.VoteTimeException;
import com.example.appengine.java8.customexceptions.VotingException;
import com.example.appengine.java8.helper.Constants;
import com.example.appengine.java8.services.CandidateManagement;
import com.example.appengine.java8.services.VoteTimeManagement;
import com.example.appengine.java8.services.VotingManagement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Logger;

public class VotingServlet extends HttpServlet {
    private static Logger logger = Logger.getLogger(VotingServlet.class.getName());

    /**
     * shows voting page with available candidates
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CandidateManagement service = new CandidateManagement();
        VoteTimeManagement voteTimeManagement = new VoteTimeManagement();

        try {
            VoteTime voteTime = voteTimeManagement.getVoteTimings();
            Calendar cal = Calendar.getInstance(); // creates calendar
            cal.setTime(new Date()); // sets calendar time/date
            cal.add(Calendar.HOUR_OF_DAY, 1); // adds one hour
            Date today = cal.getTime();
            Date sDate = voteTime.getStartDate(), eDate = voteTime.getEndDate();
            VotingManagement votingManagement = new VotingManagement();
            Voter voter = votingManagement.getVoterByEmail(req.getUserPrincipal().getName());
            if (voter != null) {
                if (voter.getVoted())
                    req.getRequestDispatcher("/thankyou.jsp").forward(req, resp);
            }
            if (today.after(eDate)) {
                resp.sendRedirect("/votingresults");
            }
            //if voting period
            else if (today.after(sDate) && today.before(eDate)) {
                List<Candidate> candidates = service.getAllCandidates();
                if (candidates != null && !candidates.isEmpty()) {
                    req.setAttribute("candidateList", candidates);
                }
                req.getRequestDispatcher("/vote.jsp").forward(req, resp);
            } else {
                req.getRequestDispatcher("/votingnotice.jsp").forward(req, resp);
            }

        } catch (CandidateException | VoteTimeException | VotingException | NullPointerException e) {
            logger.severe("Could not load page \n" + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * vote is saved
     * gets selected candidate and unique token
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        VoteTimeManagement voteTimeManagement = new VoteTimeManagement();
        String name = req.getParameter("candidateSelection");
        String token = req.getParameter("token");
        if (name != null) {
            VotingManagement votingService = new VotingManagement();
            try {
                VoteTime voteTime = voteTimeManagement.getVoteTimings();
                Calendar cal = Calendar.getInstance(); // creates calendar
                cal.setTime(new Date()); // sets calendar time/date
                cal.add(Calendar.HOUR_OF_DAY, 1); // adds one hour
                Date today = cal.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat(Constants.DateFormat_MM_DD_YYYY_HH_MM_A);
                Date startDate = voteTime.getStartDate();
                Date endDate = voteTime.getEndDate();
                if (today.after(startDate) && today.before(endDate)) {
                    votingService.castVoteWithToken(name, token);
                    resp.getWriter().write(String.valueOf(Constants.VALID));
                } else
                    resp.getWriter().write("Please try again during the voting period");
            } catch (CandidateException | VotingException | VoteTimeException e) {
                logger.severe("Error " + e.getMessage());
                resp.getWriter().write(e.getMessage());
            }
        }
    }
}




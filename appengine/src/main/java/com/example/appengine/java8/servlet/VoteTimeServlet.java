package com.example.appengine.java8.servlet;

import com.example.appengine.java8.Data.VoteTime;
import com.example.appengine.java8.customexceptions.VoteTimeException;
import com.example.appengine.java8.helper.Constants;
import com.example.appengine.java8.services.VoteTimeManagement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Logger;

public class VoteTimeServlet extends HttpServlet {
    private static Logger logger = Logger.getLogger
            (VoteTimeServlet.class.getName());

    /**
     * fetch start and end of voting period
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        VoteTimeManagement service = new VoteTimeManagement();
        VoteTime timing;
        try {
            timing = service.getVoteTimings();
            if (timing != null) {
                req.setAttribute("timings", timing);
            }
        } catch (VoteTimeException e) {
            logger.severe("Error fetching vote timings" + e.getMessage());
        }
        req.getRequestDispatcher("/voteTimeMgmt.jsp").
                forward(req, resp);
    }


    /**
     * create or update startdate and enddate of voting period
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        try {
            SimpleDateFormat sdf=new SimpleDateFormat(Constants.DateFormat_MM_DD_YYYY_HH_MM_A);
            VoteTimeManagement service = new VoteTimeManagement();

            VoteTime timing, newtiming = new VoteTime();
            String startDateString = req.getParameter(Constants.VOTETIMING_STARTDATE_PROPERTY);
            String endDateString = req.getParameter(Constants.VOTETIMING_ENDDATE_PROPERTY);
            timing = service.getVoteTimings();
            Date sDate=sdf.parse(startDateString);
            Date eDate=sdf.parse(endDateString);
            Date today=new Date();
            if((sDate.after(today)||sDate.compareTo(today)==0) && (eDate.after(sDate))) {
                newtiming.setStartDate(sdf.parse(startDateString));
                newtiming.setEndDate(sdf.parse(endDateString));

                //if the start and end date are already saved then update else create
                if (timing != null) service.update(newtiming.getStartDate(), newtiming.getEndDate());
                else service.create(newtiming.getStartDate(), newtiming.getEndDate());


                resp.getWriter().write(String.valueOf(Constants.VALID));
            }
            else {
                logger.severe("Dates are invalid");
                resp.getWriter().write("Check start date is not before present date and end date is after startdate");
            }
        } catch (VoteTimeException e) {
            logger.severe("Error adding or updating dates" + e.getMessage());
            resp.getWriter().write(e.getMessage());
        } catch (ParseException e) {
            logger.severe("Invalid dates " + e.getMessage());
            resp.getWriter().write(String.valueOf(Constants.INVALID));
        }
    }


}

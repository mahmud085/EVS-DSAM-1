package com.example.appengine.java8.servlet;

import com.example.appengine.java8.Data.Candidate;
import com.example.appengine.java8.customexceptions.CandidateException;
import com.example.appengine.java8.helper.Constants;
import com.example.appengine.java8.services.CandidateManagement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class CandidateServlet extends HttpServlet {
    private static Logger logger = Logger.getLogger
            (CandidateServlet.class.getName());
    private String firstname= Constants.CANDIDATE_FIRSTNAME_PROPERTY,
            surname= Constants.CANDIDATE_SURNAME_PROPERTY,faculty=Constants.CANDIDATE_FACULTY_PROPERTY;


    /**
     * fetches all existing candidates
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        CandidateManagement service=new CandidateManagement();
        try {
            List<Candidate> candidates = service.getAllCandidates();
            if (candidates!=null && !candidates.isEmpty()) {
                req.setAttribute("candidateList", candidates);
            }
        }catch (CandidateException e){
            logger.severe("Error fetching candidates" + e.getMessage());
        }
        req.getRequestDispatcher("/candidateMgmt.jsp").forward(req, resp);
    }

    /**
     * create candidate post
     *  candidate id extracted from key
     *  candidate attributes
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        CandidateManagement service=new CandidateManagement();
        try {
            String firstname = req.getParameter(this.firstname);
            String surname = req.getParameter(this.surname);
            String faculty = req.getParameter(this.faculty);
            service.createCandidate(firstname, surname, faculty);
            resp.sendRedirect(req.getContextPath() + "/admin/candidatemanagement");
        }catch (CandidateException e){
            logger.severe("Error creating candidate" + e.getMessage());
            resp.getWriter().write(e.getMessage());

        }
    }

    /**
     * update candidate
     *  candidate id extracted from key
     *  candidate attributes from form
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        CandidateManagement service=new CandidateManagement();
        Boolean results;
        try {
            String id = req.getParameter("id");
            String firstname = req.getParameter(this.firstname);
            String surname = req.getParameter(this.surname);
            String faculty = req.getParameter(this.faculty);
            results = service.updateCandidate(id, firstname, surname, faculty);
            resp.getWriter().write(String.valueOf(results));
        }catch (CandidateException e){
            logger.severe("Error updating candidate" + e.getMessage());
            resp.getWriter().write(e.getMessage());
        }


    }

    /**
     * delete candidare
     *  candidate id extracted from key
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        CandidateManagement service=new CandidateManagement();
        boolean res;
        try {
            String id;
            id = req.getParameter("id");
            res = service.deleteCandidate(id);
            resp.getWriter().write(String.valueOf(res));
        }catch (CandidateException e){
            logger.severe("Error deleting candidate" + e.getMessage());
            resp.getWriter().write(e.getMessage());

        }
    }
}

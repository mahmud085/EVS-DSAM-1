package com.example.appengine.java8.helper;

public final class Constants {

    //COMMON VALIDATION VALUES AND MESSAGES
    public static final boolean VALID = true;
    public static final boolean INVALID = false;

    //ROOT ANCESTOR FOR ALL KINDS
    public static final String ROOTANCESTOR = "election";
    public static final String ROOTKIND = "Election";

    //DATE FORMATS
    public static final String DateFormat_MM_DD_YYYY_HH_MM_A = "MM/dd/yyyy hh:mm a";

    //CANDIDATE
    public static final String CANDIDATE = "candidate";
    public static final String CANDIDATE_FIRSTNAME_PROPERTY = "firstname";
    public static final String CANDIDATE_SURNAME_PROPERTY = "surname";
    public static final String CANDIDATE_FACULTY_PROPERTY = "faculty";


    //VOTER
    public static final String VOTERS = "voters";
    public static final String VOTER_ID_PROPERTY = "id";
    public static final String VOTER_NAME_PROPERTY = "name";
    public static final String VOTER_EMAIL_PROPERTY = "email";
    public static final String VOTER_TOKEN_PROPERTY = "token";
    public static final String VOTER_EMAILSENT_PROPERTY = "emailsent";


    //VOTETIMING
    public static final String VOTETIMING = "votetiming";
    public static final String VOTETIMING_STARTDATE_PROPERTY = "startdate";
    public static final String VOTETIMING_ENDDATE_PROPERTY = "enddate";



    //EMAIL RELATED
    public static final String EMAILSENDER_AUTHORIZED = "oormila131@gmail.com";
    public static final String MSG_SUBJ_VOTING_NOTIFICATION = "Your Vote Matters";
    public static final String MSG_SUBJ_VOTING_REMINDER = "Reminder - University Election Day!";
    public static final String VOTER_REMINDER_PROPERTY = "reminder";
    public static final String VOTER_ISVOTED_PROPERTY = "isvoted";
    public static final String CANDIDATE_VOTECOUNT_PROPERTY = "votecount";
    public static final String USERS_VOTING_INTERFACE = "/user/vote";
}

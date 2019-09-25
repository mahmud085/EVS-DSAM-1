package com.example.appengine.java8.Data;

import java.util.Date;

public class VoteTime {
    public VoteTime() {

    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    Date startDate;
    Date endDate;


    public VoteTime(Date startDate, Date endDate) {
        this.startDate=startDate;
        this.endDate=endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }


}

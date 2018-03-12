package com.example.jaisa.smarttraumareliever_flawsome.Beans;

/**
 * Created by Varsha on 11-03-2018.
 */

public class Complaint {
    String subject;
    String desciption;
    String date;
    String time;
    String reportedTo;

    public Complaint(String subject, String desciption, String date, String time, String reportedTo) {
        this.subject = subject;
        this.desciption = desciption;
        this.date = date;
        this.time = time;
        this.reportedTo = reportedTo;
    }

    public String getSubject() {
        return subject;
    }

    public String getDesciption() {
        return desciption;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getReportedTo() {
        return reportedTo;
    }
}

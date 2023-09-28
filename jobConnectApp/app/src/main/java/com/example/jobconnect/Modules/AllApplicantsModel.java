package com.example.jobconnect.Modules;

public class AllApplicantsModel {
    private String applicantName;
    private String applicantEmail;
    private int jobId;



    public AllApplicantsModel(String applicantName, String applicantEmail, int jobId) {
        this.applicantName = applicantName;
        this.applicantEmail = applicantEmail;
        this.jobId=jobId;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getApplicantEmail() {
        return applicantEmail;
    }

    public void setApplicantEmail(String applicantEmail) {
        this.applicantEmail = applicantEmail;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }
}

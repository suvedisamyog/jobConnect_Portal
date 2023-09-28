package com.example.jobconnect.Modules;

public class JobKeyValue {
    private String jobTitle;
    private String value;

    public JobKeyValue(String jobTitle, String value) {
        this.jobTitle = jobTitle;
        this.value = value;
    }
    public String getJobTitle() {
        return jobTitle;
    }

    public String getValue() {
        return value;
    }
}

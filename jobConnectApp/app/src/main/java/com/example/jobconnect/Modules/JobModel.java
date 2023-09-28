package com.example.jobconnect.Modules;

public class JobModel {




    private  String jSalary;
    private  String jIndustry;
    private int jId;
    private String jTitle;
    private String jDescription;
    private String jEmpType;
    private String jEducation;
    private String jExperience;
    private String jCategory;
    private String jName;
    private String postedDate;
    private String deadline;
    private String JEmail;
    private String status;
    private String Jvacancies;



    public JobModel(int jId, String jTitle, String jDescription, String jEmpType, String jEducation,
                    String jExperience, String jCategory,
                    String jName, String postedDate, String deadline, String jIndustry,
                    String jSalary, String jvacancies, String JEmail,String status) {
        this.jId = jId;
        this.jTitle = jTitle;
        this.jDescription = jDescription;
        this.jEmpType = jEmpType;
        this.jEducation = jEducation;
        this.jExperience = jExperience;
        this.jCategory = jCategory;
        this.jName = jName;
        this.postedDate = postedDate;
        this.deadline = deadline;
        this.jIndustry=jIndustry;
        this.jSalary=jSalary;
        this.Jvacancies=jvacancies;
        this.JEmail=JEmail;
        this.status=status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getJId() {
        return jId;
    }

    public String getJTitle() {
        return jTitle;
    }

    public String getJDescription() {
        return jDescription;
    }

    public String getJEmpType() {
        return jEmpType;
    }

    public String getJEducation() {
        return jEducation;
    }

    public String getJExperience() {
        return jExperience;
    }

    public String getJEmail() {
        return JEmail;
    }

    public void setJEmail(String JEmail) {
        this.JEmail = JEmail;
    }

    public String getJvacancies() {
        return Jvacancies;
    }

    public void setJvacancies(String jvacancies) {
        this.Jvacancies = jvacancies;
    }




    public String getJCategory() {
        return jCategory;
    }


    public String getJName() {
        return jName;
    }

    public String getPostedDate() {
        return postedDate;
    }

    public String getDeadline() {
        return deadline;
    }

    public String getJIndustry() {return jIndustry; }

    public String getjSalary() {
        return jSalary;
    }
}

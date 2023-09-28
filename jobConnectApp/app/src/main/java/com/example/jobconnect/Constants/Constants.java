package com.example.jobconnect.Constants;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Constants {



    //    public static final String IP="192.168.1.5";
    public static final String IP="192.168.1.6";

    static String Register="Register_User.php";
    private static String Login="Login_User.php";
    private static String PostJOb="Post_Job.php";
    private static String  DISPLAY_JOB_ORG="Org_DisplayJob.php";
    private static String  DELETE_JOB="Delete_job.php";
    private static String  UPDATE_JOB="Update_job.php";
    private static String  SET_USER_PROFILE="User_Profile.php";
    private static String  SET_ORG_PROFILE="Org_Profile.php";
    private static String  FETCH_ORG_PROFILE="Fetch_Org_Info.php";
    private static String  FETCH_USER_PROFILE="Fetch_User_Info.php";
    private static String  UPDATE_ORG_PROFILE="Update_Org_Profile.php";
    private static String  UPDATE_USER_PROFILE="Update_User.php";
    private static String  CHANGE_PASSWORD="Change_Password.php";
    private static String  GET_ALL_JOBS="Get_All_Job.php";
    private static String  GET_SUGGESTED_JOBS="Get_Suggested_Job.php";
    private static String  TOGGLE_SAVED_JOBS="Toggle_savedJobs.php";
    private static String  CHECK_SAVED_JOBS="Check_saved_id.php";
    private static String  GET_ALL_SAVED_JOBS="All_SavedJobs.php";
    private static String  GET_ALL_APPLIED_JOBS="All_AppliedJobs.php";
    private static String  CHECK_IF_APPLIED="Check_if_applied.php";
    private static String  APPLY_JOB="Apply_job.php";
    private static String  UPLOAD_CV_DATA="upload_cv.php";
    private static String  DISPLAY_CV="GetCv.php";
    private static String  CHECK_CV="CheckCv.php";
    private static String  TOTAL_APPLICANTS="Get_Total_Applicants.php";
    private static String  ALL_APPLICANTS="All_Applicant_For_Each_Job.php";
    private static String  CHANGE_STATUS="Change_Status.php";
    private static String  GET_CATEGORIES="Fetch_Categories.php";





    private static final String URL_ROOT="http://"+IP+"/JobConnect_API/Web_Services/";
    public static final String URL_REGISTER=URL_ROOT+Register;
    public static final String URL_LOGIN=URL_ROOT+Login;
    public  static final String URL_POST_JOB=URL_ROOT+PostJOb;
    public  static final String URL_DELETE_JOB=URL_ROOT+DELETE_JOB;
    public  static final String URL_POSTED_JOB=URL_ROOT+DISPLAY_JOB_ORG;
    public  static final String URL_UPDATE_JOB=URL_ROOT+UPDATE_JOB;
    public  static final String URL_USER_PROFILE=URL_ROOT+SET_USER_PROFILE;
    public  static final String URL_ORG_PROFILE=URL_ROOT+SET_ORG_PROFILE;
    public  static final String URL_ORG_PROFILE_FETCH=URL_ROOT+FETCH_ORG_PROFILE;
    public  static final String URL_USER_PROFILE_FETCH=URL_ROOT+FETCH_USER_PROFILE;
    public  static final String URL_ORG_PROFILE_UPDATE=URL_ROOT+UPDATE_ORG_PROFILE;
    public  static final String URL_USER_PROFILE_UPDATE=URL_ROOT+UPDATE_USER_PROFILE;
    public  static final String URL_CHANGE_PASSWORD=URL_ROOT+CHANGE_PASSWORD;
    public  static final String URL_ALL_JOBS=URL_ROOT+GET_ALL_JOBS;
    public  static final String URL_SUGGESTED_JOBS=URL_ROOT+GET_SUGGESTED_JOBS;
    public  static final String URL_SAVE_REMOVE_SAVEDJOBS=URL_ROOT+TOGGLE_SAVED_JOBS;
    public  static final String URL_CHECK_SAVED_JOBS=URL_ROOT+CHECK_SAVED_JOBS;
    public  static final String URL_ALL_SAVED_JOBS=URL_ROOT+GET_ALL_SAVED_JOBS;
    public  static final String URL_ALL_APPLIED_JOBS=URL_ROOT+GET_ALL_APPLIED_JOBS;
    public  static final String URL_CHECK_IF_APPLIED=URL_ROOT+CHECK_IF_APPLIED;
    public  static final String URL_APPLY_JOB=URL_ROOT+APPLY_JOB;
    public  static final String URL_UPLOAD_CV_DATA=URL_ROOT+UPLOAD_CV_DATA;
    public  static final String URL_DISPLAY_CV=URL_ROOT+DISPLAY_CV;
    public  static final String URL_CHECK_CV=URL_ROOT+CHECK_CV;
    public  static final String URL_TOTAL_APPLICANTS=URL_ROOT+TOTAL_APPLICANTS;
    public  static final String URL_GET_ALL_APPLICANTS=URL_ROOT+ALL_APPLICANTS;
    public  static final String URL_CHANGE_STATUS=URL_ROOT+CHANGE_STATUS;
    public  static final String URL_GET_CATEGORIES=URL_ROOT+GET_CATEGORIES;

}

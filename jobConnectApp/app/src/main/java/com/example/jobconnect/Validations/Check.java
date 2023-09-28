package com.example.jobconnect.Validations;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.jobconnect.Constants.Constants;

public class Check {

    Boolean checkSaved(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Constants.URL_ALL_JOBS,
                response -> {

                },
                error -> {

                }){


        };
        return true;
    }
    Boolean checkApplied(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Constants.URL_ALL_JOBS,
                response -> {

                },
                error -> {

                }){

        };
        return true;
    }
}

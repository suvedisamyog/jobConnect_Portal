package com.example.jobconnect.JOB_SEEKER_PART;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.jobconnect.Constants.Constants;
import com.example.jobconnect.Modules.RequestHandler;
import com.example.jobconnect.Modules.SessionManager;
import com.example.jobconnect.R;
import com.example.jobconnect.UI_CONTROLLER.MainActivity;
import com.example.jobconnect.UI_CONTROLLER.Public_profile;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class cv_profile extends Fragment {


    String userEmail;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_cv_profile, container, false);

        SessionManager sessionManager = new SessionManager(requireContext());
        if (sessionManager.checkSession()) {
            userEmail = sessionManager.getSessionDetail("useremail");
        } else {
            startActivity(new Intent(getContext(), MainActivity.class));
        }
        StringRequest stringRequest1=new StringRequest(Request.Method.POST,
                Constants.URL_CHECK_CV,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (!jsonObject.getBoolean("error")){
                            Fragment newFragment = new Public_profile(userEmail);
                            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragment_container, newFragment);
                            transaction.commit();
                        }else{
                            Fragment newFragment = new cv_segment();
                            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragment_container, newFragment);
                            transaction.commit();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), "Error: Invalid JSON response from server", Toast.LENGTH_LONG).show();
                        throw new RuntimeException(e);
                    }

                },
                error -> {
                    Toast.makeText(getContext(), "error:" + error.getMessage(), Toast.LENGTH_LONG).show();
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", userEmail);
                return params;
            }
        };
        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest1);

        return view;
    }

}
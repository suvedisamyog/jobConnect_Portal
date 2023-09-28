package com.example.jobconnect.JOB_SEEKER_PART;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.jobconnect.ADAPTERS.All_Jobs_Adp;
import com.example.jobconnect.Constants.Constants;
import com.example.jobconnect.Modules.JobModel;
import com.example.jobconnect.Modules.RequestHandler;
import com.example.jobconnect.Modules.SessionManager;
import com.example.jobconnect.R;
import com.example.jobconnect.UI_CONTROLLER.MainActivity;
import com.example.jobconnect.Validations.FormatData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class allJobs extends Fragment {

    private String email;
    private RecyclerView recyclerView;
    private All_Jobs_Adp jobAdapter;


    private List<JobModel> jobList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SessionManager sessionManager = new SessionManager(requireContext());
        if (sessionManager.checkSession()) {
            email = sessionManager.getSessionDetail("useremail");
        } else {
            startActivity(new Intent(getContext(), MainActivity.class));
        }
        View view= inflater.inflate(R.layout.fragment_all_jobs, container, false);


        recyclerView = view.findViewById(R.id.allJobRCV);
        jobList = new ArrayList<>();

        jobAdapter = new All_Jobs_Adp(jobList,requireActivity().getSupportFragmentManager(),getContext(),email);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(jobAdapter);



        getAllJobs();

        return view;
    }



    private void getAllJobs() {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Constants.URL_ALL_JOBS,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Log.d("AllJobs", "getAllJobs: "+response);
               if(!jsonObject.getBoolean("error")){
                   JSONArray jsonArray = jsonObject.optJSONArray("data");
                   for (int i = 0; i < jsonArray.length(); i++){
                       JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                       int jId = jsonObject1.optInt("j_id");
                       String jTitle = jsonObject1.optString("j_title");
                       String jDescription = jsonObject1.optString("j_description");

                       String jEmpType = jsonObject1.optString("j_empType");
                       String jCategory = jsonObject1.optString("j_category");
                       String jExperience = jsonObject1.optString("j_experience");
                       String jEducation = jsonObject1.optString("j_education");
                       String jName = jsonObject1.optString("j_name");
                       String JIndustry=jsonObject1.optString("j_industry");
                       String JSalary=jsonObject1.getString("j_salary");
                       String postedDate = FormatData.formatDate(jsonObject1.optString("postedDate"));
                       String deadline = FormatData.formatDate(jsonObject1.optString("deadline"));
                       String Jvacancies=jsonObject1.getString("vacancies");
                       String JEmail=jsonObject1.getString("j_email");
                       String status="Not Available";



                       JobModel job = new JobModel(jId, jTitle, jDescription, jEmpType, jEducation, jExperience,
                               jCategory, jName, postedDate, deadline,JIndustry,JSalary, Jvacancies, JEmail,status);
                       jobList.add(job);
                   }
                   jobAdapter.notifyDataSetChanged();

               }else {
                   Toast.makeText(getContext(), jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
               }
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("User_HOME", "JSON Exception: " + e.getMessage());
                        throw new RuntimeException(e);
                    }

                },
                error -> {
                    Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("user_HOME", "Volley Error: " + error.getMessage());
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };
        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);

    }
}
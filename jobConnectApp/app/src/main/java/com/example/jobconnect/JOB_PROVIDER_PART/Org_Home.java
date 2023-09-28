package com.example.jobconnect.JOB_PROVIDER_PART;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.example.jobconnect.ADAPTERS.Display_Posted_job_Adp;
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

public class Org_Home extends Fragment {

    private String email;
    private List<JobModel> jobList;
    private Display_Posted_job_Adp jobAdapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SessionManager sessionManager = new SessionManager(requireContext());
        if (sessionManager.checkSession()) {
            email = sessionManager.getSessionDetail("useremail");
        } else {
            startActivity(new Intent(getContext(), MainActivity.class));
        }

        View view = inflater.inflate(R.layout.fragment_org__home, container, false);
        recyclerView = view.findViewById(R.id.orgHome_rv);
        jobList = new ArrayList<>();
        jobAdapter = new Display_Posted_job_Adp(jobList,requireActivity().getSupportFragmentManager());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(jobAdapter);

        getPostedJobs();
        return view;
    }

    private void getPostedJobs() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_POSTED_JOB,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Log.d("alld", "getPostedJobs: "+response);
                        if (!jsonObject.getBoolean("error")) {
                            JSONArray jsonArray = jsonObject.optJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
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
                                String Jvacancies=jsonObject1.getString("vacancies");
                                String JEmail=jsonObject1.getString("j_email");
                                String status="Not Available";


                                String postedDate = FormatData.formatDate(jsonObject1.optString("postedDate"));
                                String deadline = FormatData.formatDate(jsonObject1.optString("deadline"));



                                // Create a new JobModel object
                                JobModel job = new JobModel(jId, jTitle, jDescription, jEmpType, jEducation, jExperience,
                                          jCategory, jName, postedDate, deadline,JIndustry,JSalary,Jvacancies,JEmail,status);
                                jobList.add(job);
                            }

                            jobAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("Org_Home", "JSON Exception: " + e.getMessage());
                    }
                },
                error -> {
                    Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Org_Home", "Volley Error: " + error.getMessage());
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }
        };
        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
    }
}

package com.example.jobconnect.JOB_PROVIDER_PART;

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
import com.example.jobconnect.ADAPTERS.All_Applicant_Adp;
import com.example.jobconnect.ADAPTERS.Display_Posted_job_Adp;
import com.example.jobconnect.Constants.Constants;
import com.example.jobconnect.Modules.AllApplicantsModel;
import com.example.jobconnect.Modules.JobModel;
import com.example.jobconnect.Modules.RequestHandler;
import com.example.jobconnect.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class View_all_applicants extends Fragment {
private int id;
private RecyclerView recyclerView;
    private All_Applicant_Adp applicant_adp;
    private List<AllApplicantsModel> allApplicantsModelList;


    public View_all_applicants(int id) {
        this.id=id;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_view_all_applicants, container, false);
        recyclerView=view.findViewById(R.id.allApplicants);
        allApplicantsModelList = new ArrayList<>();
        applicant_adp = new All_Applicant_Adp(allApplicantsModelList,requireActivity().getSupportFragmentManager());


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(applicant_adp);

        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                Constants.URL_GET_ALL_APPLICANTS,
                response -> {
                    try {
                        Log.d("hee", "onCreateView: "+response);
                        JSONObject jsonObject = new JSONObject(response);
                        if (!jsonObject.getBoolean("error")){
                            JSONArray jsonArray = jsonObject.optJSONArray("message");
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String applicantName = jsonObject1.optString("Name");
                                String applicantEmail = jsonObject1.optString("Email");
                                int jobId = jsonObject1.optInt("jobId");

                                AllApplicantsModel value=new AllApplicantsModel(applicantName,applicantEmail,jobId);
                                allApplicantsModelList.add(value);
                            }
                            applicant_adp.notifyDataSetChanged();
                            Log.d("hrr", "onCreateView: "+allApplicantsModelList);
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        throw new RuntimeException(e);
                    }
                },
                error -> {
                    Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();

                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("jobId", String.valueOf(id));
                return params;
            }
        };
        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);

        return view;
    }
}
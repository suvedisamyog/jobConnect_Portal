package com.example.jobconnect.ADAPTERS;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.jobconnect.Constants.Constants;
import com.example.jobconnect.JOB_SEEKER_PART.IndividualJobs;
import com.example.jobconnect.Modules.JobModel;
import com.example.jobconnect.Modules.RequestHandler;
import com.example.jobconnect.R;
import com.example.jobconnect.Validations.FormatData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SavedJobs_Adp extends RecyclerView.Adapter<SavedJobs_Adp.MyViewHolder> {
    private List<JobModel> jobList;
    private FragmentManager fragmentManager;
    private Context context;
    private String email;

    View view;

    public SavedJobs_Adp(List<JobModel> jobList, FragmentManager fragmentManager, Context context, String email) {
        this.jobList = jobList;
        this.fragmentManager = fragmentManager;
        this.context = context;
        this.email = email;
    }

    @NonNull
    @Override
    public SavedJobs_Adp.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.job_card_view, parent, false);
        return new SavedJobs_Adp.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedJobs_Adp.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        JobModel job = jobList.get(position);

        holder.title.setText(job.getJTitle());
        holder.name.setText(job.getJName());
        holder.empType.setText(FormatData.formatType(job.getJEmpType()));
        holder.vacancies.setText(FormatData.reDefine(job.getJvacancies()));
        holder.education.setText(FormatData.formatType(job.getJEducation()));
        holder.removedJob.setVisibility(View.GONE);
        holder.savedJob.setVisibility(View.VISIBLE);

        holder.deadline.setText(job.getDeadline());

        holder.individualJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openIndividualJob(job.getJId());
            }
        });
        holder.removedJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedJobsOperation(job.getJId(), holder.removedJob, holder.savedJob, holder.mainLayout, holder.noData, position);
            }
        });
        holder.savedJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedJobsOperation(job.getJId(), holder.removedJob, holder.savedJob, holder.mainLayout, holder.noData, position);
            }
        });
    }

    private void savedJobsOperation(int jId, ImageView remvedJob, ImageView savedJob, LinearLayout mainLayout, ImageView noData, int position) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_SAVE_REMOVE_SAVEDJOBS,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean error = jsonObject.getBoolean("error");
                        String message = jsonObject.getString("message");
                        Log.d("nosaveerror", "savedJobsOperation: " + response);
                        if (!error) {
                            // Job toggled successfully
                            if (message.equals("Removed")) {
                                remvedJob.setVisibility(View.VISIBLE);
                                savedJob.setVisibility(View.GONE);

                                jobList.remove(position);
                                notifyDataSetChanged();
                                Toast.makeText(context, "Job removed from saved list", Toast.LENGTH_SHORT).show();
                                if (jobList.isEmpty()) {
                                    noData.setVisibility(View.VISIBLE);
                                    mainLayout.setVisibility(View.GONE);
                                }
                            } else if (message.equals("Added")) {
                                remvedJob.setVisibility(View.GONE);
                                savedJob.setVisibility(View.VISIBLE);
                                Toast.makeText(context, "Job added to saved list", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        Log.d("hello1", "savedJobsOperation: " + response);
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        throw new RuntimeException(e);
                    }
                },
                error -> {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("jobId", String.valueOf(jId));
                params.put("userEmail", email);
                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);
    }

    private void openIndividualJob(int jId) {
        IndividualJobs individualJobsFragment = new IndividualJobs();
        Bundle bundle = new Bundle();
        bundle.putInt("jobId", jId);
        bundle.putString("userType", "user");
        bundle.putSerializable("jobList", (Serializable) jobList);

        individualJobsFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, individualJobsFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name, title, empType, education, vacancies, deadline;
        private CardView individualJob;
        LinearLayout mainLayout;
        ImageView savedJob, removedJob, noData;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.job_title);
            name = itemView.findViewById(R.id.job_company);
            empType = itemView.findViewById(R.id.jop_type);
            vacancies = itemView.findViewById(R.id.job_vacancies);
            education = itemView.findViewById(R.id.job_education);
            deadline = itemView.findViewById(R.id.job_endDate);
            individualJob = itemView.findViewById(R.id.individualJob);
            savedJob = itemView.findViewById(R.id.savedJob);
            removedJob = itemView.findViewById(R.id.removedJob);
            noData = itemView.findViewById(R.id.noData);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}

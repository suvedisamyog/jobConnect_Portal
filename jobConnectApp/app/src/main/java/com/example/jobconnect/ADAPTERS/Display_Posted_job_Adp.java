package com.example.jobconnect.ADAPTERS;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.jobconnect.Constants.Constants;
import com.example.jobconnect.JOB_PROVIDER_PART.Org_Add;
import com.example.jobconnect.JOB_PROVIDER_PART.View_all_applicants;
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

public class Display_Posted_job_Adp extends RecyclerView.Adapter<Display_Posted_job_Adp.MyViewHolder> {

    private List<JobModel> jobList;
    View view;
    private FragmentManager fragmentManager;

    public Display_Posted_job_Adp(List<JobModel> jobList, FragmentManager fragmentManager) {

        this.fragmentManager = fragmentManager;
        this.jobList = jobList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.org_job_singlecard, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        JobModel job = jobList.get(position);

        holder.title.setText(job.getJTitle());
        holder.name.setText(job.getJName());
        holder.empType.setText(FormatData.formatType(job.getJEmpType()));
        holder.education.setText(FormatData.formatType(job.getJEducation()));
         holder.vacancy.setText(job.getJvacancies());
        holder.deadline.setText(job.getDeadline());

        holder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getJobDetails(job.getJId());
            }
        });

        holder.option.setOnClickListener(v -> {
            showPopupMenu(holder.option,job.getJId());
        });

        holder.viewApplicants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Applicants(job.getJId());

            }
        });
        holder.numberOfApplicants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Applicants(job.getJId());
            }
        });
        holder.viewApplicants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Applicants(job.getJId());
            }
        });
        viewApplicants(job.getJId(),holder.applicantsNumber);

    }

    private void Applicants(int id) {
        Fragment newFragment = new View_all_applicants(id);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.commit();
    }

    private void viewApplicants(int jId, TextView numberApplicant) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST,Constants.URL_TOTAL_APPLICANTS,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (!jsonObject.getBoolean("error")){
                            String number = jsonObject.optString("message");
                            numberApplicant.setText(number);
                        }

                    } catch (JSONException e) {
                        Toast.makeText(view.getContext(),"Response catch:"+e , Toast.LENGTH_SHORT).show();
                        throw new RuntimeException(e);
                    }
                },error -> {
            Toast.makeText(view.getContext(),"Response error:"+error , Toast.LENGTH_SHORT).show();


        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("j_id", String.valueOf(jId));
                return params;
            }
        };
        RequestHandler.getInstance(view.getContext()).addToRequestQueue(stringRequest);
    }



    private void getJobDetails(int jId) {
        IndividualJobs individualJobsFragment = new IndividualJobs();
        Bundle bundle = new Bundle();
        bundle.putInt("jobId", jId);
        bundle.putString("userType", "org");
        bundle.putSerializable("jobList", (Serializable) jobList);

        individualJobsFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, individualJobsFragment);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    private void showPopupMenu(View view, int jId) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.org_singlecard_option);

        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_edit) {
                editDetails(jId);
                return true;
            } else if (id == R.id.menu_delete) {
                deleteJob(jId);
                return true;
            }
            return false;
        });

        popupMenu.show();
    }

    private void editDetails(int jId) {
        Org_Add editFragment = new Org_Add("Edit");
        editFragment.edit(jId,jobList);
        FragmentManager fragmentManager = ((AppCompatActivity) view.getContext()).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, editFragment)
                .addToBackStack(null)
                .commit();


    }

    private void deleteJob(int id){
        StringRequest stringRequest=new StringRequest(Request.Method.POST,Constants.URL_DELETE_JOB,
                response -> {
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        Toast.makeText(view.getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        for (int i = 0; i < jobList.size(); i++) {
                            if (jobList.get(i).getJId() == id) {
                                jobList.remove(i);
                                break;
                            }
                        }
                        notifyDataSetChanged();
                    } catch (JSONException e) {
                        Toast.makeText(view.getContext(),"Response catch:"+e , Toast.LENGTH_SHORT).show();
                        throw new RuntimeException(e);
                    }
                },error -> {
            Log.d("deleteJob", "deleteJob: "+error.getMessage());
            Toast.makeText(view.getContext(),"Response error:"+error , Toast.LENGTH_SHORT).show();


        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("j_id", String.valueOf(id));
                return params;
            }
        };
        RequestHandler.getInstance(view.getContext()).addToRequestQueue(stringRequest);

    }



    @Override
    public int getItemCount() {
        return jobList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView title;
        TextView empType,applicantsNumber;
        TextView education,viewApplicants;
        LinearLayout detail,numberOfApplicants;

        TextView deadline;
        TextView vacancy;

        private ImageView option;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.d_title);
            name = itemView.findViewById(R.id.d_cname);
            empType = itemView.findViewById(R.id.d_empType);
            education = itemView.findViewById(R.id.d_education);
            vacancy = itemView.findViewById(R.id.d_vacancy);
            deadline = itemView.findViewById(R.id.d_endDate);
            option=itemView.findViewById(R.id.card_option);
            detail=itemView.findViewById(R.id.mainLayout);
            applicantsNumber=itemView.findViewById(R.id.applicantsNumber);
            viewApplicants=itemView.findViewById(R.id.viewApplicants);
            numberOfApplicants=itemView.findViewById(R.id.numberOfApplicants);
        }
    }
}

package com.example.jobconnect.ADAPTERS;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobconnect.JOB_SEEKER_PART.IndividualJobs;
import com.example.jobconnect.Modules.JobModel;
import com.example.jobconnect.R;
import com.example.jobconnect.Validations.FormatData;

import java.io.Serializable;
import java.util.List;

public class Applied_Jobs_Adp extends RecyclerView.Adapter<Applied_Jobs_Adp.MyViewHolder> {
    private List<JobModel> jobList;
    private FragmentManager fragmentManager;
    private Context context;
    private String email;

    View view;
    public Applied_Jobs_Adp(List<JobModel> jobList, FragmentManager fragmentManager, Context context, String email) {
        this.jobList = jobList;
        this.fragmentManager = fragmentManager;
        this.context = context;
        this.email = email;
    }



    @NonNull
    @Override
    public Applied_Jobs_Adp.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.job_card_view, parent, false);
        return new Applied_Jobs_Adp.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Applied_Jobs_Adp.MyViewHolder holder, int position) {
        JobModel job = jobList.get(position);
        holder.title.setText(job.getJTitle());
        holder.name.setText(job.getJName());
        holder.empType.setText(FormatData.formatType(job.getJEmpType()));
        holder.vacancies.setText(FormatData.reDefine(job.getJvacancies()));
        holder.education.setText(FormatData.formatType(job.getJEducation()));
        holder.deadline.setText(job.getStatus());
        if(job.getStatus().equals("Pending")){
            holder.deadline.setTextColor(context.getResources().getColor(R.color.blue)); // Change color to black
        } else if (job.getStatus().equals("Rejected")) {
            holder.deadline.setTextColor(context.getResources().getColor(R.color.red)); // Change color to black
        }else if(job.getStatus().equals("ShortListed")){
            holder.deadline.setTextColor(context.getResources().getColor(R.color.lightGreen));
        }else{
            holder.deadline.setTextColor(context.getResources().getColor(R.color.black));
        }
        holder.removedJob.setVisibility(View.GONE);
        holder.savedJob.setVisibility(View.GONE);
        holder.appliedJob.setVisibility(View.VISIBLE);
        holder.individualJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openIndividualJob(job.getJId());
            }
        });
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
        ImageView savedJob, removedJob, noData,appliedJob;

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
            appliedJob=itemView.findViewById(R.id.appliedJob);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }

}

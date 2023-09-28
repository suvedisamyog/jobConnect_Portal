package com.example.jobconnect.ADAPTERS;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobconnect.Modules.AllApplicantsModel;
import com.example.jobconnect.R;
import com.example.jobconnect.UI_CONTROLLER.Public_profile;

import java.util.List;

public class All_Applicant_Adp extends RecyclerView.Adapter<All_Applicant_Adp.ViewHolder> {
    private List<AllApplicantsModel> itemList;

    private FragmentManager fragmentManager;

    public All_Applicant_Adp(List<AllApplicantsModel> allApplicantsModelList, FragmentManager fragmentManager) {
        this.fragmentManager=fragmentManager;
        this.itemList=allApplicantsModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_applicants_layout, parent, false);
        return new All_Applicant_Adp.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull All_Applicant_Adp.ViewHolder holder, int position) {
        AllApplicantsModel item = itemList.get(position);
        holder.applicantName.setText(item.getApplicantName());
        holder.applicantEmail.setText(item.getApplicantEmail());
     holder.applicantDetail.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             openParticularApplicants(item.getApplicantEmail(),item.getJobId());
         }
     });


    }

    private void openParticularApplicants(String applicantEmail, int jobId) {
        Fragment newFragment = new Public_profile(applicantEmail, jobId);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.commit();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView applicantName;
        public TextView applicantEmail,applicantDetail;

        public ViewHolder(View itemView) {
            super(itemView);
            applicantName = itemView.findViewById(R.id.applicantName);
            applicantEmail = itemView.findViewById(R.id.applicantEmail);
            applicantEmail = itemView.findViewById(R.id.applicantEmail);
            applicantDetail = itemView.findViewById(R.id.applicantDetail);
        }
    }


}

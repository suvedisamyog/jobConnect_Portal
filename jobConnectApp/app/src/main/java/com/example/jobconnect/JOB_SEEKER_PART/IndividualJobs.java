package com.example.jobconnect.JOB_SEEKER_PART;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.jobconnect.ADAPTERS.JobInfo_Adp;
import com.example.jobconnect.Constants.Constants;
import com.example.jobconnect.Modules.JobKeyValue;
import com.example.jobconnect.Modules.JobModel;
import com.example.jobconnect.Modules.RequestHandler;
import com.example.jobconnect.Modules.SessionManager;
import com.example.jobconnect.R;
import com.example.jobconnect.UI_CONTROLLER.MainActivity;
import com.example.jobconnect.UI_CONTROLLER.Public_profile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class IndividualJobs extends Fragment {

    private int jId;
    private String userType;
    private ArrayList<JobModel> jobList;
    private RecyclerView recyclerView;
    private List<JobKeyValue> itemList;
    private JobInfo_Adp itemAdapter;




    TextView jobTitle,jobCompany,info_jobCompany,info_Email,info_Phone,info_Website,jobType,detailDescription,info_Location,actualStatus;
    String organizationWebsite,organizationPhone,organizationLocation;
    Bitmap bitmap;
    ProgressBar progressBar;
    ImageView photoPreview;
    RelativeLayout btnHowToApply;
    LinearLayout appliedStatus;
    int id;
    String userEmail,userName;


    public IndividualJobs() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            jId = getArguments().getInt("jobId");
            userType = getArguments().getString("userType");
            jobList = (ArrayList<JobModel>) getArguments().getSerializable("jobList");
        }

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_individual_jobs, container, false);


        SessionManager sessionManager = new SessionManager(requireContext());
        if (sessionManager.checkSession()) {
            userEmail = sessionManager.getSessionDetail("useremail");
            userName = sessionManager.getSessionDetail("username");
        } else {
            startActivity(new Intent(getContext(), MainActivity.class));
        }



        jobTitle = view.findViewById(R.id.jobTitle);
        jobCompany=view.findViewById(R.id.jobCompany);
        jobType =view.findViewById(R.id.jobType);
        info_jobCompany=view.findViewById(R.id.info_jobCompany);
        info_Email=view.findViewById(R.id.info_Email);
        info_Phone=view.findViewById(R.id.info_Phone);
        info_Website=view.findViewById(R.id.info_Website);
        info_Location=view.findViewById(R.id.info_Location);
        detailDescription=view.findViewById(R.id.detailDescription);
        photoPreview=view.findViewById(R.id.photoPreview);
        btnHowToApply=view.findViewById(R.id.btnHowToApply);
        appliedStatus=view.findViewById(R.id.appliedStatus);
        actualStatus=view.findViewById(R.id.actualStatus);
        progressBar=view.findViewById(R.id.progressBar);



        JobModel matchedJob = null;
        for (JobModel job : jobList) {
            if (job.getJId() == jId) {
                matchedJob = job;
                id=jId;
                break;
            }
        }

        if (matchedJob != null) {



            recyclerView = view.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            itemList = new ArrayList<>();

            itemList.add(new JobKeyValue("Job Title ", matchedJob.getJTitle()));
            itemList.add(new JobKeyValue("Job Type ", matchedJob.getJEmpType()));
            itemList.add(new JobKeyValue("No of Vacancy", matchedJob.getJvacancies()));
            itemList.add(new JobKeyValue("Education Qualification ", matchedJob.getJEducation()));
            itemList.add(new JobKeyValue("Experience ", matchedJob.getJExperience()));
            itemList.add(new JobKeyValue("Job Industry ", matchedJob.getJIndustry()));
            itemList.add(new JobKeyValue("Job Category ", matchedJob.getJCategory()));
            itemList.add(new JobKeyValue("Salary ", matchedJob.getjSalary()));
            itemList.add(new JobKeyValue("Posted On ", matchedJob.getPostedDate()));
            itemList.add(new JobKeyValue("Deadline ", matchedJob.getDeadline()));



            itemAdapter = new JobInfo_Adp(itemList);
            recyclerView.setAdapter(itemAdapter);

            detailDescription.setText(matchedJob.getJDescription());

            jobTitle.setText(matchedJob.getJTitle());
            jobCompany.setText(matchedJob.getJName());
            jobType.setText(matchedJob.getJEmpType());
            String OrgEmail=matchedJob.getJEmail();



            info_jobCompany.setText(matchedJob.getJName());
            info_Email.setText(matchedJob.getJEmail());

            String deadline=matchedJob.getDeadline();
            btnHowToApply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    applyJob(id, deadline);
                }
            });

            progressBar.setVisibility(View.VISIBLE);
            StringRequest stringRequest=new StringRequest(Request.Method.POST,Constants.URL_CHECK_IF_APPLIED,
                    response -> {
                        Log.d("ifApplied", "onCreateView: "+response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("isApplied")){
                       btnHowToApply.setVisibility(View.GONE);
                      appliedStatus.setVisibility(View.VISIBLE);
                      String actualStatusValue=jsonObject.getString("status");
                      actualStatus.setText(actualStatusValue);
                      if(actualStatusValue.equals("Rejected")){
                          actualStatus.setTextColor(getResources().getColor(R.color.red));
                      } else if (actualStatusValue.equals("ShortListed")) {
                          actualStatus.setTextColor(getResources().getColor(R.color.lightGreen));
                      }else if(actualStatusValue.equals("Pending")){
                          actualStatus.setTextColor(getResources().getColor(R.color.blue));
                      }else{
                          actualStatus.setTextColor(getResources().getColor(R.color.black));
                      }
                            }else{
                                if(userType.equalsIgnoreCase("org")){
                                    btnHowToApply.setVisibility(View.GONE);
                                } else if (userType.equalsIgnoreCase("user")) {
                                    btnHowToApply.setVisibility(View.VISIBLE);
                                }else{
                                    btnHowToApply.setVisibility(View.VISIBLE);
                                }
                                appliedStatus.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            Log.d("catch", "onCreateView: "+e.getMessage());
                        }
                        progressBar.setVisibility(View.GONE);
                    },
                    error -> {
                        Log.d("error", "onCreateView: "+error.getMessage());
                        progressBar.setVisibility(View.GONE);


                    }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", userEmail);
                    params.put("Job_id", String.valueOf(id));

                    return params;
                }
            };
            RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);



            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Constants.URL_ORG_PROFILE_FETCH,
                    response -> {
                        progressBar.setVisibility(View.VISIBLE);
                        try {
                            Log.d("orgprofile", "onCreateView: "+response);
                            JSONObject jsonObject = new JSONObject(response);
                            if (!jsonObject.getBoolean("error")){
                                JSONArray data = jsonObject.getJSONArray("data");
                                if (data.length() > 0) {
                                    JSONObject orgData = data.getJSONObject(0);
                                    organizationPhone = orgData.getString("oPhone");
                                    String organizationImage = orgData.getString("oImg");
                                    organizationWebsite = orgData.getString("oWeb");
                                    organizationLocation=orgData.getString("oLocation");
                                    String actualImg="http://"+Constants.IP+"/JobConnect_API"+organizationImage;


                                    info_Phone.setText(organizationPhone);
                                    info_Website.setText(organizationWebsite);
                                    info_Location.setText(organizationLocation);

                                    Glide.with(this)
                                            .asBitmap()
                                            .load(actualImg)
                                            .placeholder(R.drawable.baseline_qr_code_scanner_24) // Add a placeholder image resource
                                            .error(R.drawable.baseline_error_24) // Add an error image resource
                                            .into(new CustomTarget<Bitmap>(){
                                                @Override
                                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                    processBitmap(resource);
                                                }
                                                @Override
                                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                                }

                                                @Override
                                                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                                    super.onLoadFailed(errorDrawable);
                                                    photoPreview.setImageDrawable(errorDrawable);

                                                }
                                            });
                                }


                            }else {

                            }

                        } catch (JSONException e) {
                            Toast.makeText(getContext(), "Error: Invalid JSON response from server", Toast.LENGTH_LONG).show();

                            throw new RuntimeException(e);
                        }
                        progressBar.setVisibility(View.GONE);


                    }, error -> {
                Toast.makeText(getContext(), "error:" + error.getMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);


            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", OrgEmail);

                    return params;
                }

            };
            RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest1);


            info_Phone.setText(matchedJob.getJTitle());
            info_Website.setText(matchedJob.getJTitle());


        } else {
            TextView textView = view.findViewById(R.id.jobTitle);
            textView.setText(String.valueOf(jId) + " (empty)");
        }

        return view;
    }

    private void applyJob(int jId, String deadline) {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date deadlineDate = dateFormat.parse(deadline);

            if (deadlineDate.before(currentDate)) {
                Toast.makeText(getContext(),"expired", Toast.LENGTH_SHORT).show();
                getOut();

            } else {
                applyToJob();
//                Toast.makeText(getContext(),id+" "+userEmail, Toast.LENGTH_SHORT).show();

            }
        } catch (ParseException e) {
            getOut();


        }



    }

    private void applyToJob() {
        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest3=new StringRequest(Request.Method.POST,Constants.URL_APPLY_JOB,
                response -> {
                    Log.d("ifApplied", "onCreateView: "+response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if (!jsonObject.getBoolean("error")){

                            btnHowToApply.setVisibility(View.GONE);
                            appliedStatus.setVisibility(View.VISIBLE);
                           actualStatus.setText("Pending");
                           actualStatus.getResources().getColor(R.color.black);
                            Log.d("4", "onCreateView: "+jsonObject.getString("message"));

                            Toast.makeText(getContext(),jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                        }else{
                            String message=jsonObject.getString("message");
                            if(message.equalsIgnoreCase("NOCV")){
                                Toast.makeText(getContext(),"First lets make your cv",Toast.LENGTH_SHORT).show();
                                Fragment newFragment = new cv_profile();
                                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                                transaction.replace(R.id.fragment_container, newFragment);
                                transaction.commit();
                            }else{
                                Toast.makeText(getContext(),jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                            }
                            Log.d("5", "applyToJob: "+message);
                        }
                    } catch (JSONException e) {
                        Log.d("6", "applyToJob: "+e.getMessage());
                        Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();

                        Log.d("catch", "onCreateView: "+e.getMessage());
                    }
                    progressBar.setVisibility(View.GONE);


                },
                error -> {
                    Log.d("1", "applyToJob: "+error.getMessage());
                    Toast.makeText(getContext(),error.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);


                    Log.d("error", "onCreateView: "+error.getMessage());

                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", userEmail);
                params.put("name", userName);
                params.put("Job_id", String.valueOf(id));

                return params;
            }
        };
        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest3);
    }

    private void getOut() {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new User_Home());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void processBitmap(Bitmap resource) {
        this.bitmap = resource;
        photoPreview.setImageBitmap(bitmap);
    }
}

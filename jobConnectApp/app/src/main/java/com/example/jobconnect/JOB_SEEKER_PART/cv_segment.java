    package com.example.jobconnect.JOB_SEEKER_PART;

    import android.content.Intent;
    import android.graphics.Bitmap;
    import android.graphics.drawable.Drawable;
    import android.os.Bundle;

    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.appcompat.widget.AppCompatButton;
    import androidx.appcompat.widget.AppCompatEditText;
    import androidx.fragment.app.Fragment;
    import androidx.fragment.app.FragmentTransaction;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import android.text.TextUtils;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.LinearLayout;
    import android.widget.Spinner;
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
    import com.example.jobconnect.Modules.RequestHandler;
    import com.example.jobconnect.Modules.SessionManager;
    import com.example.jobconnect.R;
    import com.example.jobconnect.UI_CONTROLLER.MainActivity;
    import com.example.jobconnect.UI_CONTROLLER.Public_profile;
    import com.google.android.material.imageview.ShapeableImageView;
    import com.google.android.material.textfield.TextInputEditText;

    import org.json.JSONArray;
    import org.json.JSONException;
    import org.json.JSONObject;

    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;


    public class cv_segment extends Fragment {


        private List<JobKeyValue> itemList;
        private RecyclerView recyclerView;
        TextView myID;
        String dbImg;
        Bitmap bitmap;
       ShapeableImageView profilePic;
        String userEmail;

        private ArrayList<String> selectedCategories = new ArrayList<>();

        private JobInfo_Adp itemAdapter;
        TextInputEditText aboutMe;
        AppCompatButton addEducationBtn,generateCvBtn,addExperiencesBtn,skillBtn;
        LinearLayout eduLayout;
        LinearLayout expLayout;
        LinearLayout skillLayout;
        List<View> addedViews = new ArrayList<>();
        List<View> addedViewsExp = new ArrayList<>();
        List<View> addedViewsSkill = new ArrayList<>();
        AppCompatEditText perAddress,tempAddress;
        String perAddressValue,tempAddressValue,genderVlaue;
        Spinner gender;






        public cv_segment() {
            // Required empty public constructor
        }






        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View view=inflater.inflate(R.layout.fragment_cv_segment, container, false);
            SessionManager sessionManager = new SessionManager(requireContext());
            if (sessionManager.checkSession()) {
                userEmail = sessionManager.getSessionDetail("useremail");
            } else {
                startActivity(new Intent(getContext(), MainActivity.class));
            }




            fetchDataFromServer();
            aboutMe = view.findViewById(R.id.aboutMe);
            eduLayout=view.findViewById(R.id.addEducation);
            expLayout=view.findViewById(R.id.experienceAdd);
            skillLayout=view.findViewById(R.id.skillAdd);
            addEducationBtn=view.findViewById(R.id.addEducationBtn);
            addExperiencesBtn=view.findViewById(R.id.addExperiences);
            skillBtn=view.findViewById(R.id.addSkills);
            generateCvBtn=view.findViewById(R.id.generateCv);
            perAddress=view.findViewById(R.id.perAddress);
            tempAddress=view.findViewById(R.id.tempAddress);
            gender=view.findViewById(R.id.gender);
            profilePic=view.findViewById(R.id.profilePic);



            recyclerView = view.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            addEducationBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View addedLayout = LayoutInflater.from(getContext()).inflate(R.layout.education_layout, null);
                    addedViews.add(addedLayout);
                    eduLayout.addView(addedLayout); // Add the layout to the parent eduLayout
                }
            });
            addExperiencesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View addedLayoutExp = LayoutInflater.from(getContext()).inflate(R.layout.experiance_layout, null);
                    expLayout.addView(addedLayoutExp);
                    addedViewsExp.add(addedLayoutExp);
                }
            });
            skillBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View addedLayoutSkill = LayoutInflater.from(getContext()).inflate(R.layout.skill_layout, null);
                    skillLayout.addView(addedLayoutSkill);
                    addedViewsSkill.add(addedLayoutSkill);
                }
            });






            generateCvBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cvGenerate();
                }
            });
            // Retrieve the values of the added views


            return  view;
        }

        private void cvGenerate() {

            List<String> educationDetails = new ArrayList<>();
            List<String> experienceDetail = new ArrayList<>();
            List<String> skillDetails = new ArrayList<>();

            for (int i = 0; i < addedViews.size(); i++) {
                View educationLayout = addedViews.get(i);
                AppCompatEditText schoolNameEditText = educationLayout.findViewById(R.id.schoolNameEditText);
                AppCompatEditText educationLevelEditText = educationLayout.findViewById(R.id.educationLevelEditText);
                AppCompatEditText gradePercentageEditText = educationLayout.findViewById(R.id.gradePercentageEditText);

                String schoolName = schoolNameEditText.getText().toString().trim();
                String educationLevel = educationLevelEditText.getText().toString().trim();
                String gradePercentage = gradePercentageEditText.getText().toString().trim();


                if (!schoolName.isEmpty() && !educationLevel.isEmpty() && !gradePercentage.isEmpty()) {
                    String educationDetail = gradePercentage + " at " + educationLevel + " from " + schoolName;
                    educationDetails.add(educationDetail);
                }
            }
            for (int i = 0; i < addedViewsExp.size(); i++) {
                View experienceLayout = addedViewsExp.get(i);
                AppCompatEditText companyNameEditText = experienceLayout.findViewById(R.id.companyNameEditText);
                AppCompatEditText positionEditText = experienceLayout.findViewById(R.id.positionEditText);
                AppCompatEditText timeEditText = experienceLayout.findViewById(R.id.timeEditText);

                String companyName = companyNameEditText.getText().toString().trim();
                String position = positionEditText.getText().toString().trim();
                String time = timeEditText.getText().toString().trim();

                if (!companyName.isEmpty() && !position.isEmpty() ) {
                    if(!time.isEmpty()){
                        String expDetail = position + " at " + companyName + " for " + time;
                        experienceDetail.add(expDetail);
                    }else{
                        String expDetail = position + " at " + companyName;
                        experienceDetail.add(expDetail);
                    }
                }
            }
            for (int i = 0; i < addedViewsSkill.size(); i++) {
                View educationLayout = addedViewsSkill.get(i);
                AppCompatEditText skillNameEditText = educationLayout.findViewById(R.id.skillNameEditText);


                String skill = skillNameEditText.getText().toString().trim();


                // Check if required fields are not empty
                if (!skill.isEmpty()) {
                    String skills = skill;
                    skillDetails.add(skills);
                }
            }
            Log.d("skills", "cvGenerate: "+skillDetails.toString());
            perAddressValue=perAddress.getText().toString();
            tempAddressValue=tempAddress.getText().toString();
            genderVlaue=gender.getSelectedItem().toString();

            if(perAddressValue.isEmpty() || tempAddressValue.isEmpty() || skillDetails.isEmpty()){
                if(perAddressValue.isEmpty() ){
                    Toast.makeText(getContext(),"permanent Address  empty",Toast.LENGTH_SHORT).show();
                }
                if(tempAddressValue.isEmpty() ){
                    Toast.makeText(getContext(),"Temporary Address empty",Toast.LENGTH_SHORT).show();

                }
                if(skillDetails.isEmpty()){
                    Toast.makeText(getContext(),"Please enter at least 1 Skill",Toast.LENGTH_SHORT).show();
                }
            }else{
                uploadToServer(skillDetails,educationDetails,experienceDetail);
            }





        }

        private void uploadToServer(List<String> skillDetails, List<String> educationDetails, List<String> experienceDetail) {
            String skills = TextUtils.join(",", skillDetails);
            String educations = TextUtils.join(",", educationDetails);
            String experiences = TextUtils.join(",", experienceDetail);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_UPLOAD_CV_DATA,
                    response -> {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean error = jsonObject.getBoolean("error");
                            String message = jsonObject.getString("message");
                            if (!error) {
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                Fragment newFragment = new Public_profile(userEmail);
                                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                                transaction.replace(R.id.fragment_container, newFragment);
                                transaction.addToBackStack(null);
                                transaction.commit();
                            } else {
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getContext(), "Error: Invalid JSON response from server", Toast.LENGTH_LONG).show();
                            throw new RuntimeException(e);
                        }
                    }, error -> {
                Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("skill", skills);
                    params.put("education", educations);
                    params.put("experience", experiences);
                    params.put("gender", genderVlaue);
                    params.put("per_address", perAddressValue);
                    params.put("temp_address", tempAddressValue);
                    params.put("email", userEmail);
                    return params;
                }
            };

            RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
        }


        private void fetchDataFromServer() {
            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Constants.URL_USER_PROFILE_FETCH,
                    response -> {
                        try {

                            itemList = new ArrayList<>();
                            Log.d("orgprofile", "onCreateView: " + response);
                            JSONObject jsonObject = new JSONObject(response);
                            if (!jsonObject.getBoolean("error")) {
                                JSONArray data = jsonObject.getJSONArray("data");
                                if (data.length() > 0) {
                                    JSONObject user = data.getJSONObject(0);

                                    itemList.add(new JobKeyValue("Full Name", user.getString("uName")));
                                    itemList.add(new JobKeyValue("Phone Number", user.getString("uPhone")));
                                    itemList.add(new JobKeyValue("Email", user.getString("uEmail")));
                                    itemList.add(new JobKeyValue("Date of Birth", user.getString("uDob")));
                                    itemList.add(new JobKeyValue("Highest Education Level", user.getString("uEducation")));
                                    itemList.add(new JobKeyValue("Interested Industry", user.getString("uIndustry")));
                                    itemList.add(new JobKeyValue("Interested Categories", user.getString("uCategories")));

                                    itemAdapter = new JobInfo_Adp(itemList);
                                    recyclerView.setAdapter(itemAdapter);

                                    String aboutMeValue=user.getString("uBio");
                                    aboutMe.setText(aboutMeValue);

                                    String organizationImage = user.getString("uImg");
                                    String actualImg="http://"+Constants.IP+"/JobConnect_API"+organizationImage;
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


                                                }
                                            });













                                }
                            } else {
                                Toast.makeText(getContext(), jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getContext(), "Error: Invalid JSON response from server", Toast.LENGTH_LONG).show();
                            throw new RuntimeException(e);
                        }
                    }, error -> {
                Toast.makeText(getContext(), "error:" + error.getMessage(), Toast.LENGTH_LONG).show();
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", userEmail);
                    return params;
                }
            };
            RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest1);
        }

        private void processBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
            profilePic.setImageBitmap(bitmap);
        }



    }
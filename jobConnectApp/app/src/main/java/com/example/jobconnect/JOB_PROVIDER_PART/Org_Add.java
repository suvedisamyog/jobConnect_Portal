    package com.example.jobconnect.JOB_PROVIDER_PART;

    import android.content.Intent;
    import android.os.Bundle;

    import androidx.annotation.Nullable;
    import androidx.appcompat.widget.AppCompatButton;
    import androidx.fragment.app.Fragment;

    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.Menu;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.Spinner;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.android.volley.AuthFailureError;
    import com.android.volley.Request;
    import com.android.volley.toolbox.StringRequest;
    import com.example.jobconnect.Constants.Constants;
    import com.example.jobconnect.Modules.CategoryUtils;
    import com.example.jobconnect.Modules.JobModel;
    import com.example.jobconnect.Modules.RequestHandler;
    import com.example.jobconnect.Modules.SessionManager;
    import com.example.jobconnect.R;
    import com.example.jobconnect.UI_CONTROLLER.MainActivity;
    import com.google.android.material.navigation.NavigationView;
    import com.google.android.material.textfield.TextInputEditText;

    import org.json.JSONException;
    import org.json.JSONObject;

    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

    public class Org_Add extends Fragment {
        private String name, email;
        private TextInputEditText jTitleEditText, jDescriptionEditText, jCompanyName, jEmail,j_salary,vacancies;
        private TextView update_job_text,post_job_text;
        private Spinner empTypeSpinner, eduRequirementSpinner, experienceSpinner, deadlineSpinner, industrySpinner, categorySpinner;
        private AppCompatButton postJobButton,updateJobButton;

        private String openType;

        private  String jobId;
        private List<JobModel> jobList;

        public Org_Add(String openType) {
            this.openType=openType;
        }

        public void edit(int jobId, List<JobModel> jobList){
            this.jobId= String.valueOf(jobId);
            this.jobList=jobList;



        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_org__add, container, false);

            SessionManager sessionManager = new SessionManager(requireContext());
            if (sessionManager.checkSession()) {
                name = sessionManager.getSessionDetail("username");
                email = sessionManager.getSessionDetail("useremail");
            } else {
                startActivity(new Intent(getContext(), MainActivity.class));
            }


            jTitleEditText = view.findViewById(R.id.j_title);
            jDescriptionEditText = view.findViewById(R.id.j_description);
            empTypeSpinner = view.findViewById(R.id.emp_type);
            eduRequirementSpinner = view.findViewById(R.id.edu_requirement);
            experienceSpinner = view.findViewById(R.id.experince);
            deadlineSpinner = view.findViewById(R.id.deadline);
            jCompanyName = view.findViewById(R.id.j_company);
            jEmail = view.findViewById(R.id.j_email);
            industrySpinner = view.findViewById(R.id.industry);
            categorySpinner = view.findViewById(R.id.category);
            j_salary= view.findViewById(R.id.j_salary);
            vacancies=view.findViewById(R.id.vacancies);

            update_job_text=view.findViewById(R.id.update_job_text);
            post_job_text=view.findViewById(R.id.post_job_text);


            postJobButton = view.findViewById(R.id.post_job_button);
            updateJobButton = view.findViewById(R.id.update_job_button);


            jCompanyName.setText(name);
            jEmail.setText(email);
            setIndustryArray();



            if(openType.equalsIgnoreCase("Edit")){

                performEdit();

            } else if (openType.equalsIgnoreCase("Add")) {
                performAdd();
            }else{
                performAdd();
            }



            return view;
        }



        public void performEdit() {




            updateJobButton.setVisibility(View.VISIBLE);
            postJobButton.setVisibility(View.GONE);

            update_job_text.setVisibility(View.VISIBLE);
            post_job_text.setVisibility(View.GONE);

            deadlineSpinner.setEnabled(false);
            String title = "";
            String description="";
            String employmentType= "";
            String experience="";
            String eduationQ="";
            String industry="";
            String salary="";
            String jvacancies="";



            for (JobModel job : jobList) {

                if (String.valueOf(job.getJId()).equals(jobId)) {


                    title = job.getJTitle();
                    description = job.getJDescription();
                    employmentType = job.getJEmpType();
                    experience = job.getJExperience();
                    eduationQ= job.getJEducation();
                    industry = job.getJIndustry();
                    salary=job.getjSalary();
                    jvacancies=job.getJvacancies();

                    break; // Exit the loop if a match is found
                }
            }
            Log.d("Org_Add", "performEdit: "+title);
            jTitleEditText.setText(title);

            jDescriptionEditText.setText(description);
            j_salary.setText(salary);
            vacancies.setText(jvacancies);

            String[] employmentTypesArray = getResources().getStringArray(R.array.Employment_Type);
            String[] educationQualification = getResources().getStringArray(R.array.Edu_Qualification);
            String[] experienceArray = getResources().getStringArray(R.array.Experience_Level);
            String[] industryArray = getResources().getStringArray(R.array.Industries);


              for (int i = 0; i < experienceArray.length; i++) {
                if (experienceArray[i].equalsIgnoreCase(experience)) {
                    experienceSpinner.setSelection(i);
                    break;
                }
            }

            for (int i = 0; i < employmentTypesArray.length; i++) {
                if (employmentTypesArray[i].equalsIgnoreCase(employmentType)) {
                    empTypeSpinner.setSelection(i);

                    break;
                }
            }
            for (int i = 0; i < educationQualification.length; i++) {
                if (educationQualification[i].equalsIgnoreCase(eduationQ)) {
                    eduRequirementSpinner.setSelection(i);
                    break;
                }
            }
            for (int i = 0; i < industryArray.length; i++) {
                if (industryArray[i].equalsIgnoreCase(industry)) {
                    industrySpinner.setSelection(i);
                    break;
                }
            }

            updateJobButton.setOnClickListener(v -> {
                String jobTitle = jTitleEditText.getText().toString();
                String jobDescription = jDescriptionEditText.getText().toString();
                String upemptype = empTypeSpinner.getSelectedItem().toString();
                String educationRequirement = eduRequirementSpinner.getSelectedItem().toString();
                String experienceLevel = experienceSpinner.getSelectedItem().toString();
                String upindustry = industrySpinner.getSelectedItem().toString();
                String category = categorySpinner.getSelectedItem().toString();
                String vacanciesValue =vacancies.getText().toString();
                String salaryValue =vacancies.getText().toString();

                String salaryV,vacanciesV;
                salaryV = salaryValue.isEmpty() ? "Negotiable" : salaryValue;
                vacanciesV = vacanciesValue.isEmpty() ? "Undefined" : vacanciesValue;



                StringRequest stringRequest=new StringRequest(Request.Method.POST,Constants.URL_UPDATE_JOB,
                        response -> {
                            try {

                                JSONObject jsonObject = new JSONObject(response);
                                Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                if (!jsonObject.getBoolean("error")) {

                                    getParentFragmentManager().popBackStackImmediate();

                                }
                            } catch (JSONException e) {
                                Toast.makeText(getContext(), "Error: Invalid JSON response from server", Toast.LENGTH_LONG).show();
                                throw new RuntimeException(e);
                            }
                        },error -> {
                    Toast.makeText(getContext(), "error:" + error.getMessage(), Toast.LENGTH_LONG).show();
                }){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("jID", jobId);
                        params.put("title", jobTitle);
                        params.put("description", jobDescription);
                        params.put("empType", upemptype);
                        params.put("education", educationRequirement);
                        params.put("experience", experienceLevel);
                        params.put("industry", upindustry);
                        params.put("category", category);
                        params.put("vacancies", vacanciesV);
                        params.put("salary", salaryV);

                        return params;
                    }
                };
                RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);

            });
        }


        private void performAdd() {

            postJobButton.setOnClickListener(v -> {
                String jobTitle = jTitleEditText.getText().toString();
                String jobDescription = jDescriptionEditText.getText().toString();
                String employmentType = empTypeSpinner.getSelectedItem().toString();
                String educationRequirement = eduRequirementSpinner.getSelectedItem().toString();
                String experienceLevel = experienceSpinner.getSelectedItem().toString();
                String industry = industrySpinner.getSelectedItem().toString();
                String category = categorySpinner.getSelectedItem().toString();
                String companyName = name;
                String companyEmail = email;
                String deadline = deadlineSpinner.getSelectedItem().toString();
                String salaryValue=j_salary.getText().toString();
                String vacanciesValue=vacancies.getText().toString();
                String salaryV,vacanciesV;
                salaryV = salaryValue.isEmpty() ? "Negotiable" : salaryValue;
                vacanciesV = vacanciesValue.isEmpty() ? "Undefined" : vacanciesValue;



                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        Constants.URL_POST_JOB,
                        response -> {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                Log.d("respponse", "performAdd: "+response);
                                Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                if (!jsonObject.getBoolean("error")) {
                                    getParentFragmentManager().beginTransaction()
                                            .replace(R.id.fragment_container, new Org_Home())
                                            .commit();
                                    Intent intent = new Intent("com.example.JobConnect.SHOW_NOTIFICATION");
                                    getContext().sendBroadcast(intent);
                                }
                            } catch (JSONException e) {

                                Toast.makeText(getContext(), "Error: Invalid JSON response from server", Toast.LENGTH_LONG).show();
                                Log.d("respponse2", "performAdd: "+response);
                                Log.d("respponse3", "performAdd: "+e.getMessage());

                                throw new RuntimeException(e);
                            }
                        },
                        error -> Toast.makeText(getContext(), "error:" + error.getMessage(), Toast.LENGTH_LONG).show()) {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("title", jobTitle);
                        params.put("description", jobDescription);
                        params.put("empType", employmentType);
                        params.put("education", educationRequirement);
                        params.put("experience", experienceLevel);
                        params.put("industry", industry);
                        params.put("category", category);
                        params.put("name", companyName);
                        params.put("email", companyEmail);
                        params.put("deadline", deadline);
                        params.put("salary", salaryV);
                        params.put("vacancies", vacanciesV);
                        return params;
                    }
                };

                RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
            });

        }


        private void setIndustryArray() {
            ArrayAdapter<CharSequence> industryAdapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.Industries, android.R.layout.simple_spinner_dropdown_item);
            industryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            industrySpinner.setAdapter(industryAdapter);
            industrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedIndustry = parent.getItemAtPosition(position).toString();
                    CategoryUtils.updateCategorySpinner(selectedIndustry, categorySpinner, requireView());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

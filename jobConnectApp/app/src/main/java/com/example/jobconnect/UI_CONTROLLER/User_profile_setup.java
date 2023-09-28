package com.example.jobconnect.UI_CONTROLLER;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.InputFilter;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.jobconnect.Constants.Constants;
import com.example.jobconnect.JOB_SEEKER_PART.User_Nav;
import com.example.jobconnect.Modules.RequestHandler;
import com.example.jobconnect.Modules.SessionManager;
import com.example.jobconnect.R;
import com.example.jobconnect.Validations.VD_Registration;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class User_profile_setup extends AppCompatActivity {
    private TextView  Uname;
//    private TextView dob;
    private TextInputEditText contactNumber,userBio,userName;
    private TextView dob;
    private LinearLayout checkboxContainer;
    private AppCompatButton btnAddImg, uploadProfile, uploadCv;
    private Bitmap bitmap;
    private ImageView profilePic;
    private Spinner industryInterest,eduQualification;
    private String[] finance_categories;
    private String[] it_categories;
    private String[] engineering_categories;
    private String[] retail_categories;
    private String[] hospitality_categories;
    private String[] marketing_categories;
    private String[] manufacturing_categories;
    private String[] media_categories;
    private String[] healthcare_categories;
    private String[] education_categories;
//    private EditText userBio;

    private int count;
    private static final int MAX_INPUT_LENGTH = 250;
    String displayEmail,displayName,dobValue,base64Pdf;
    SessionManager sessionManager;


    private ArrayList<String> selectedCategories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_setup);
        displayEmail = getIntent().getStringExtra("email");
        displayName = getIntent().getStringExtra("name");



        btnAddImg = findViewById(R.id.btnAddImg);
        dob = findViewById(R.id.dob);
        userName = findViewById(R.id.userName);

        profilePic = findViewById(R.id.profilePic);
        contactNumber = findViewById(R.id.contactNumber);
        userName = findViewById(R.id.userName);
        userName.setVisibility(View.GONE);
        uploadProfile = findViewById(R.id.uploadProfile);

        userBio = findViewById(R.id.userBio);
        checkboxContainer = findViewById(R.id.checkboxContainer);
        industryInterest = findViewById(R.id.industryInterest);

        eduQualification=findViewById(R.id.eduQualification);
        Uname=findViewById(R.id.Uname);
        Uname.setText("HELLO"+" "+displayName.toUpperCase());
        dob=findViewById(R.id.dob);

        dobValue=dob.getText().toString().trim();




        finance_categories = getResources().getStringArray(R.array.finance_categories);
        it_categories = getResources().getStringArray(R.array.it_categories);
        engineering_categories = getResources().getStringArray(R.array.engineering_categories);
        retail_categories = getResources().getStringArray(R.array.retail_categories);
        hospitality_categories = getResources().getStringArray(R.array.hospitality_categories);
        marketing_categories = getResources().getStringArray(R.array.marketing_categories);
        manufacturing_categories = getResources().getStringArray(R.array.manufacturing_categories);
        media_categories = getResources().getStringArray(R.array.media_categories);
        healthcare_categories = getResources().getStringArray(R.array.healthcare_categories);
        education_categories = getResources().getStringArray(R.array.education_categories);

        // Set input length limit for userBio EditText
        userBio.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_INPUT_LENGTH)});

        btnAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });


        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        User_profile_setup.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, 1990, 0, 1);
                datePickerDialog.show();
            }
        });


        uploadProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadToServer();
            }
        });



        industryInterest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Handle category selection based on the chosen industry
                String industry = parent.getItemAtPosition(position).toString();
                switch (industry) {
                    case "Finance/Banking":
                        setCategoryOptions(finance_categories);
                        break;
                    case "Information Technology(IT)":
                        setCategoryOptions(it_categories);
                        break;
                    case "Engineering":
                        setCategoryOptions(engineering_categories);
                        break;
                    case "Retail":
                        setCategoryOptions(retail_categories);
                        break;
                    case "Hospitality/Tourism":
                        setCategoryOptions(hospitality_categories);
                        break;
                    case "Marketing/Advertising":
                        setCategoryOptions(marketing_categories);
                        break;
                    case "Manufacturing":
                        setCategoryOptions(manufacturing_categories);
                        break;
                    case "Media/Entertainment":
                        setCategoryOptions(media_categories);
                        break;
                    case "Healthcare":
                        setCategoryOptions(healthcare_categories);
                        break;
                    case "Education":
                        setCategoryOptions(education_categories);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }



    private void uploadToServer() {
        int selectedCheckboxCount = getSelectedCheckboxCount();
        String dobValue = dob.getText().toString().trim();
        String contactNumberValue = contactNumber.getText().toString().trim();
        String userBioValue = userBio.getText().toString().trim();
        String educationValue = eduQualification.getSelectedItem().toString().trim();
        String industryValue = industryInterest.getSelectedItem().toString().trim();

        boolean isValidPhone = VD_Registration.isValidPhone(contactNumberValue);

        Log.d("userBioValue", "uploadToServer: "+userBioValue);
        if (dobValue.isEmpty() || dobValue.equalsIgnoreCase("Select Date of Birth")|| !isValidPhone || userBioValue.isEmpty()
                || selectedCheckboxCount==0 || selectedCheckboxCount==1 ||
                bitmap == null || educationValue.equals("select one"))
         {
             if(dobValue.isEmpty() ||dobValue.equalsIgnoreCase("Select Date of Birth") ){
                 Toast.makeText(getApplicationContext(), "Pleas Enter Your Date Of Birth ", Toast.LENGTH_SHORT).show();
             } else if (contactNumberValue.length()!=10 || contactNumberValue.isEmpty()) {
                 Toast.makeText(getApplicationContext(), "Contact number should be of 10 digits", Toast.LENGTH_SHORT).show();
             } else if (userBioValue.isEmpty()) {
                 Toast.makeText(getApplicationContext(), "Please write about yourself in short", Toast.LENGTH_SHORT).show();
             }else if (educationValue.equals("select one")) {
                 Toast.makeText(getApplicationContext(), "Please choose education level ", Toast.LENGTH_SHORT).show();
             }else if (selectedCheckboxCount==0 || selectedCheckboxCount==1) {
                 Toast.makeText(getApplicationContext(), "Please choose at least two interested category ", Toast.LENGTH_SHORT).show();
             } else if (bitmap == null ) {
                 Toast.makeText(getApplicationContext(), "Please choose a profile image ", Toast.LENGTH_SHORT).show();
             } else{
                 Toast.makeText(getApplicationContext(), "Please fill all fields ", Toast.LENGTH_SHORT).show();
             }

        }else{
            SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date dobDate;
            try {
                dobDate = dateFormat.parse(dobValue);
            } catch (ParseException e) {
                e.printStackTrace();
                return; // Exit the method if the date format is invalid
            }
            Calendar dobCalendar = Calendar.getInstance();
            dobCalendar.setTime(dobDate);

            Calendar todayCalendar = Calendar.getInstance();
            int age = todayCalendar.get(Calendar.YEAR) - dobCalendar.get(Calendar.YEAR);

             if(age<=13){
                 Toast.makeText(getApplicationContext(), "Age should be grater than 13", Toast.LENGTH_SHORT).show();

             }else{
                 StringBuilder selectedCategoriesBuilder = new StringBuilder();
                 for (String category : selectedCategories) {
                     selectedCategoriesBuilder.append(category).append(",");
                 }
                 String selectedCategoriesString = selectedCategoriesBuilder.toString();
                 if (selectedCategoriesString.endsWith(",")) {
                     selectedCategoriesString = selectedCategoriesString.substring(0, selectedCategoriesString.length() - 1);
                 }
                 ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                 if(bitmap!=null ){
                     bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                     byte[] bytes=byteArrayOutputStream.toByteArray();
                     final String base64Img= Base64.encodeToString(bytes,Base64.DEFAULT);
                     String finalSelectedCategoriesString = selectedCategoriesString;

                     StringRequest stringRequest=new StringRequest(Request.Method.POST, Constants.URL_USER_PROFILE,
                             response -> {
                                 try {
                                     JSONObject jsonObject = new JSONObject(response);
                                     if(!jsonObject.getBoolean("error")){
                                         sessionManager=new SessionManager(getApplicationContext());
                                         sessionManager.updateIsCompleteValue("true");
                                         startActivity(new Intent(getApplicationContext(), User_Nav.class));
                                     }
                                     Log.d("serverError", "uploadToCv: "+response);
                                     Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                 } catch (JSONException e) {
                                     Log.d("catchError", "uploadToCv: "+response);
                                     Log.d("catchError2", "uploadToCv: "+e);
                                     Toast.makeText(getApplicationContext(), "Error: Invalid JSON response from server", Toast.LENGTH_LONG).show();

                                     throw new RuntimeException(e);
                                 }


                             },error -> {
                         Toast.makeText(getApplicationContext(), "error:" + error.getMessage(), Toast.LENGTH_LONG).show();


                     }){
                         @Nullable
                         @Override
                         protected Map<String, String> getParams() throws AuthFailureError {
                             Map<String, String> params = new HashMap<>();
                             params.put("uName",displayName);
                             params.put("uEmail",displayEmail);
                             params.put("uDob",dobValue);
                             params.put("uPhone",contactNumberValue);
                             params.put("uEducation",educationValue);
                             params.put("uBio",userBioValue);
                             params.put("uImg", base64Img);
                             params.put("uCv", "null");
                             params.put("uCategories", finalSelectedCategoriesString);
                             params.put("uIndustry", industryValue);


                             return params;
                         }
                     };
                     RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
                 }else{
                     Toast.makeText(getApplicationContext(),"Please upload required file",Toast.LENGTH_SHORT).show();
                 }



             }


        }

    }

    private void setCategoryOptions(String[] categories) {
        count = 0;
        checkboxContainer.removeAllViews();
        selectedCategories.clear();

        for (String category : categories) {
            CheckBox checkBox = new CheckBox(getApplicationContext());
            checkBox.setId(count);
            checkBox.setText(category);
            checkBox.setTextSize(18);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedCategories.add(buttonView.getText().toString());
                    } else {
                        selectedCategories.remove(buttonView.getText().toString());
                    }
                }
            });
            checkboxContainer.addView(checkBox);
            count++;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                profilePic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    private int getSelectedCheckboxCount() {
        int count = 0;
        for (int i = 0; i < checkboxContainer.getChildCount(); i++) {
            View view = checkboxContainer.getChildAt(i);
            if (view instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) view;
                if (checkBox.isChecked()) {
                    count++;
                }
            }
        }
        return count;
    }
}

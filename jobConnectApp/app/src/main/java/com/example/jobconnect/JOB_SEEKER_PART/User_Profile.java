package com.example.jobconnect.JOB_SEEKER_PART;

import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.jobconnect.Constants.Constants;
import com.example.jobconnect.Modules.RequestHandler;
import com.example.jobconnect.Modules.SessionManager;
import com.example.jobconnect.R;
import com.example.jobconnect.UI_CONTROLLER.MainActivity;
import com.example.jobconnect.UI_CONTROLLER.Change_Password;
import com.example.jobconnect.Validations.VD_Registration;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class User_Profile extends Fragment {

    View view;

    private AppCompatButton btnAddImg,uploadProfile;
    private Bitmap bitmap;

    Switch switchButton;
    ImageView setting;

    private int count;

    String displayEmail;
    SessionManager sessionManager;
    LinearLayout userProfileView;

    private ArrayList<String> selectedCategories = new ArrayList<>();

    View displayProfile, editDetails;
    TextView profileName, profileEmail, profileDob, profilePhone, profileEdu, profileInd, profileBio, editDob,interestedCate;

    String dbName, dbEmail, dbDob, dbPhone, dbEdu, dbInd, dbImg, dbBio;
    Spinner editEdu, editInd;
    TextInputEditText editName, editPhone, editBio;
    ShapeableImageView logo, logo2;
    String[] allCategories = null;
    CheckBox checkBox;
    private LinearLayout checkboxContainer;
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
    private MainActivity parentActivity;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            parentActivity = (MainActivity) context;
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user__profile, container, false);
        sessionManager = new SessionManager(getContext());
        if (sessionManager.checkSession()) {
            displayEmail = sessionManager.getSessionDetail("useremail");
        } else {
            startActivity(new Intent(getContext(), MainActivity.class));
        }

        displayProfile = view.findViewById(R.id.displayProfile);
        userProfileView = view.findViewById(R.id.userProfileView);
        editDetails = view.findViewById(R.id.editDetails);
        editDetails.setVisibility(View.GONE);
        fetchDataFromServer();
        userProfileView.setVisibility(View.VISIBLE);

        switchButton = view.findViewById(R.id.switchButton);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editDetails.setVisibility(View.VISIBLE);
                    displayProfile.setVisibility(View.GONE);
                    editMode();
                } else {
                    editDetails.setVisibility(View.GONE);
                    displayProfile.setVisibility(View.VISIBLE);
                    viewMode();
                }
            }
        });

        profileName = view.findViewById(R.id.profileName);
        profileEmail = view.findViewById(R.id.profileEmail);
        profileDob = view.findViewById(R.id.profileDob);
        profilePhone = view.findViewById(R.id.profilePhone);
        profileEdu = view.findViewById(R.id.profileEdu);
        profileInd = view.findViewById(R.id.profileInd);
        profileBio = view.findViewById(R.id.profileBio);
        logo = view.findViewById(R.id.profilePicDisplay);
        logo2 = view.findViewById(R.id.profilePic);
        btnAddImg = view.findViewById(R.id.btnAddImg);
        setting = view.findViewById(R.id.settingUser);
        uploadProfile = view.findViewById(R.id.uploadProfile);
        interestedCate = view.findViewById(R.id.interestedCate);
        checkboxContainer = view.findViewById(R.id.checkboxContainer);

        btnAddImg.setText("Change Photo");
        uploadProfile.setText("Update Profile");

        editDob = view.findViewById(R.id.dob);
        editPhone = view.findViewById(R.id.contactNumber);
        editName = view.findViewById(R.id.userName);
        editEdu = view.findViewById(R.id.eduQualification);
        editInd = view.findViewById(R.id.industryInterest);
        editBio = view.findViewById(R.id.userBio);

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

        btnAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });
        // Inside onCreateView method
        editInd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedIndustry = parent.getItemAtPosition(position).toString();
                updateCategoryOptions(selectedIndustry);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case when nothing is selected
            }
        });

        editDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                editDob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, 1990, 0, 1);
                datePickerDialog.show();
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showmenu(v);
            }
        });
        uploadProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadToServer();
            }
        });




        return view;


    }



    private void uploadToServer() {
        String displayName=editName.getText().toString().trim();
        String dobValue = editDob.getText().toString().trim();
        String contactNumberValue = editPhone.getText().toString().trim();
        String educationValue = editEdu.getSelectedItem().toString().trim();
        String userBioValue = editBio.getText().toString().trim();
        String userIndustry = editInd.getSelectedItem().toString().trim();
        int selectedCheckboxCount = getSelectedCheckboxCount();
        boolean isValidDisplayName = VD_Registration.isValidName(displayName);
        boolean isValidPhone = VD_Registration.isValidPhone(contactNumberValue);


        Log.d("userBioValue", "uploadToServer: "+userBioValue);
        if (!isValidDisplayName  || dobValue.isEmpty() || !isValidPhone || userBioValue.isEmpty()
                || selectedCheckboxCount==0 || selectedCheckboxCount==1 ||
                bitmap == null || educationValue.equals("select one"))
        {
            if(displayName.isEmpty() || !isValidDisplayName ){
                Toast.makeText(getContext(), "Pleas Enter Your Full Name", Toast.LENGTH_SHORT).show();
            }else if(dobValue.isEmpty() ||dobValue.equalsIgnoreCase("Select Date of Birth") ){
                Toast.makeText(getContext(), "Pleas Enter Your Date Of Birth ", Toast.LENGTH_SHORT).show();
            } else if (!isValidPhone) {
                Toast.makeText(getContext(), "Contact number should be of 10 digits", Toast.LENGTH_SHORT).show();
            } else if (userBioValue.isEmpty()) {
                Toast.makeText(getContext(), "Please write about yourself in short", Toast.LENGTH_SHORT).show();
            }else if (educationValue.equals("select one")) {
                Toast.makeText(getContext(), "Please choose education level ", Toast.LENGTH_SHORT).show();
            }else if (selectedCheckboxCount==0 || selectedCheckboxCount==1) {
                Toast.makeText(getContext(), "Please choose at least two interested category ", Toast.LENGTH_SHORT).show();
            } else if (bitmap == null ) {
                Toast.makeText(getContext(), "Please choose a profile image ", Toast.LENGTH_SHORT).show();
            } else if (displayName.isEmpty()) {
                Toast.makeText(getContext(), "Please NAme ", Toast.LENGTH_SHORT).show();

            } else{
                Toast.makeText(getContext(), "Please fill all fields ", Toast.LENGTH_SHORT).show();
            }

        }else{
            SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date dobDate;
            try {
                dobDate = dateFormat.parse(dobValue);
            } catch (ParseException e) {
                e.printStackTrace();
                return;
            }
            Calendar dobCalendar = Calendar.getInstance();
            dobCalendar.setTime(dobDate);

            Calendar todayCalendar = Calendar.getInstance();
            int age = todayCalendar.get(Calendar.YEAR) - dobCalendar.get(Calendar.YEAR);

            if(age<=13){
                Toast.makeText(getContext(), "Age should be grater than 13", Toast.LENGTH_SHORT).show();

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
                    Log.d("bit", ""+base64Img);
                    String finalSelectedCategoriesString = selectedCategoriesString;

                    StringRequest stringRequest=new StringRequest(Request.Method.POST, Constants.URL_USER_PROFILE_UPDATE,
                            response -> {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if(!jsonObject.getBoolean("error")){
                                        sessionManager=new SessionManager(getContext());
                                        sessionManager.updateIsCompleteValue("true");
                                        startActivity(new Intent(getContext(), User_Nav.class));
                                    }
                                    Log.d("serverError", "uploadToCv: "+response);
                                    Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                } catch (JSONException e) {
                                    Log.d("catchError", "uploadToCv: "+response);
                                    Log.d("catchError2", "uploadToCv: "+e);
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
                            params.put("uName",displayName);
                            params.put("uEmail",displayEmail);
                            params.put("uDob",dobValue);
                            params.put("uPhone",contactNumberValue);
                            params.put("uEducation",educationValue);
                            params.put("uIndustry",userIndustry);
                            params.put("uBio",userBioValue);
                            params.put("uImg", base64Img);
                            params.put("uCategories", finalSelectedCategoriesString);


                            return params;
                        }
                    };
                    RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
                }else{
                    Toast.makeText(getContext(),"Please upload required file",Toast.LENGTH_SHORT).show();
                }



            }


        }

    }


    private void viewMode() {
    }

    private void editMode() {
        fetchDataFromServer();
    }

    private void fetchDataFromServer() {
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Constants.URL_USER_PROFILE_FETCH,
                response -> {
                    try {
                        Log.d("orgprofile", "onCreateView: " + response);
                        JSONObject jsonObject = new JSONObject(response);
                        if (!jsonObject.getBoolean("error")) {
                            JSONArray data = jsonObject.getJSONArray("data");
                            if (data.length() > 0) {
                                JSONObject user = data.getJSONObject(0);
                                dbName = user.getString("uName");
                                dbDob = user.getString("uDob");
                                dbPhone = user.getString("uPhone");
                                dbEdu = user.getString("uEducation");
                                dbInd = user.getString("uIndustry");
                                dbEmail = user.getString("uEmail");
                                dbBio = user.getString("uBio");
                                String userImage = user.getString("uImg");
                                dbImg = "http://" + Constants.IP + "/JobConnect_API" + userImage;
                                loadImage();

                                String userCategories = user.getString("uCategories");
                                selectedCategories.addAll(Arrays.asList(userCategories.split(",")));
                                String[] categoriesArray = userCategories.split(",");

                                String[] categoriesArrays = userCategories.split(",");

                                StringBuilder categoryLines = new StringBuilder();
                                for (int i = 0; i < categoriesArrays.length; i++) {
                                    categoryLines.append(categoriesArrays[i]);
                                    if (i < categoriesArrays.length - 1) {
                                        categoryLines.append("\n");
                                        categoryLines.append("\n");
                                    }
                                }
                                interestedCate.setText(categoryLines.toString());


                                String[] educationQualification = getResources().getStringArray(R.array.Edu_Qualification);
                                String[] industryArray = getResources().getStringArray(R.array.Industries);

                                for (int i = 0; i < educationQualification.length; i++) {
                                    if (educationQualification[i].equalsIgnoreCase(dbEdu)) {
                                        editEdu.setSelection(i);
                                        break;
                                    }
                                }

                                for (int i = 0; i < industryArray.length; i++) {
                                    if (industryArray[i].equalsIgnoreCase(dbInd)) {
                                        editInd.setSelection(i);
                                        break;
                                    }
                                }


                                profileName.setText(dbName);
                                profileEmail.setText(dbEmail);
                                profileDob.setText(dbDob);
                                profilePhone.setText(dbPhone);
                                profileEdu.setText(dbEdu);
                                profileInd.setText(dbInd);
                                profileBio.setText(dbBio);

                                editName.setText(dbName);
                                editDob.setText(dbDob);
                                editPhone.setText(dbPhone);
                                editBio.setText(dbBio);

                                updateCategoryOptions(dbInd);
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
                params.put("email", displayEmail);
                return params;
            }
        };
        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest1);
    }


    private void loadImage() {
        Glide.with(this)
                .asBitmap()
                .load(dbImg)
                .placeholder(R.drawable.loading) // Add a placeholder image resource
                .error(R.drawable.baseline_error_24) // Add an error image resource
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        processBitmap(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }

    private void setCategoryOptions(String[] categories) {
        count = 0;
        for (String category : categories) {
            checkBox = new CheckBox(getContext());
            checkBox.setText(category);
            checkBox.setTextSize(18);
            if (selectedCategories.contains(category)) {
                checkBox.setChecked(true);
            }
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

    private void updateCategoryOptions(String selectedIndustry) {
        checkboxContainer.removeAllViews();

        switch (selectedIndustry) {
            case "Information Technology(IT)":
                allCategories = getResources().getStringArray(R.array.it_categories);
                setCategoryOptions(it_categories);
                break;
            case "Healthcare":
                allCategories = getResources().getStringArray(R.array.healthcare_categories);
                setCategoryOptions(healthcare_categories);
                break;
            case "Finance/Banking":
                allCategories = getResources().getStringArray(R.array.finance_categories);
                setCategoryOptions(finance_categories);
                break;
            case "Education":
                allCategories = getResources().getStringArray(R.array.education_categories);
                setCategoryOptions(education_categories);
                break;
            case "Manufacturing":
                allCategories = getResources().getStringArray(R.array.manufacturing_categories);
                setCategoryOptions(manufacturing_categories);
                break;
            case "Retail":
                allCategories = getResources().getStringArray(R.array.retail_categories);
                setCategoryOptions(retail_categories);
                break;
            case "Engineering":
                allCategories = getResources().getStringArray(R.array.engineering_categories);
                setCategoryOptions(engineering_categories);
                break;
            case "Media/Entertainment":
                allCategories = getResources().getStringArray(R.array.media_categories);
                setCategoryOptions(media_categories);
                break;
            case "Hospitality/Tourism":
                allCategories = getResources().getStringArray(R.array.hospitality_categories);
                setCategoryOptions(hospitality_categories);
                break;
            case "Marketing/Advertising":
                allCategories = getResources().getStringArray(R.array.marketing_categories);
                setCategoryOptions(marketing_categories);
                break;
        }
    }

    private void processBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        logo.setImageBitmap(bitmap);
        logo2.setImageBitmap(bitmap);
    }

    private void showmenu(View anchorView) {
        PopupMenu popupMenu = new PopupMenu(getContext(), anchorView);
        popupMenu.getMenuInflater().inflate(R.menu.settings, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ChangePassword:
                        userProfileView.setVisibility(View.GONE);
                        switchButton.setVisibility(View.GONE);
                        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, new Change_Password(displayEmail));
                        transaction.commit();
                        return true;
                    case R.id.LogOut:
                        SessionManager sessionManager = new SessionManager(getContext());
                        sessionManager.logOut();
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        requireActivity().finish();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), uri);
                logo2.setImageBitmap(bitmap);
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

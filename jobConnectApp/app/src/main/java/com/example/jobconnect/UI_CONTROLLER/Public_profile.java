package com.example.jobconnect.UI_CONTROLLER;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.media.MediaScannerConnection;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.example.jobconnect.JOB_SEEKER_PART.cv_segment;
import com.example.jobconnect.Modules.JobKeyValue;
import com.example.jobconnect.Modules.RequestHandler;
import com.example.jobconnect.Modules.SessionManager;
import com.example.jobconnect.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class Public_profile extends Fragment {

    ShapeableImageView profilePicDisplay;
    TextView interestedCate;
    TextView eduInfo;
    TextView expInfo;
    TextView skillInfo;
    String displayEmail;
    String type;
    RecyclerView tableRecycleView;
    private List<JobKeyValue> itemList;
    private JobInfo_Adp itemAdapter;
    SessionManager sessionManager;
    Bitmap bitmap;
    AppCompatButton cvDownloadBtn, cvRegenerateBtn,rejectBtn,shortlistBtn;
    LinearLayout mainCvLayout,userSideBtns,orgSideBtns;
    TextInputEditText aboutMe;
    int jobId;


    public Public_profile(String userEmail) {

        this.displayEmail=userEmail;
    }
    public Public_profile(String userEmail, int jobId) {
        this.displayEmail=userEmail;
        this.jobId=jobId;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_public_profile, container, false);

        sessionManager = new SessionManager(getContext());
        if (sessionManager.checkSession()) {
            type = sessionManager.getSessionDetail("usertype");
        } else {
            startActivity(new Intent(getContext(), MainActivity.class));
        }

        profilePicDisplay = view.findViewById(R.id.profilePicDisplay);
        interestedCate = view.findViewById(R.id.interestedCategory);
        eduInfo = view.findViewById(R.id.eduInfo);
        expInfo = view.findViewById(R.id.expInfo);
        skillInfo = view.findViewById(R.id.skillInfo);
        tableRecycleView = view.findViewById(R.id.tableRecycleView);
        cvDownloadBtn = view.findViewById(R.id.cvDownloadBtn);
        cvRegenerateBtn = view.findViewById(R.id.cvRegenerateBtn);
        mainCvLayout = view.findViewById(R.id.mainCvLayout);
        userSideBtns = view.findViewById(R.id.userSideBtns);
        orgSideBtns = view.findViewById(R.id.orgSideBtns);
        rejectBtn = view.findViewById(R.id.rejectBtn);
        shortlistBtn= view.findViewById(R.id.shortlistBtn);
        aboutMe= view.findViewById(R.id.aboutMe);

        if(type.equalsIgnoreCase("org")){
            userSideBtns.setVisibility(View.GONE);
            cvRegenerateBtn.setVisibility(View.GONE);
            cvDownloadBtn.setVisibility(View.GONE);
        }else{
            cvRegenerateBtn.setVisibility(View.VISIBLE);
            cvDownloadBtn.setVisibility(View.VISIBLE);
            orgSideBtns.setVisibility(View.GONE);
        }



        cvRegenerateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new cv_segment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        shortlistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeStatus(1);
            }
        });
        rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeStatus(0);
            }
        });


        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_DISPLAY_CV,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (!jsonObject.getBoolean("error")) {
                            JSONArray data = jsonObject.getJSONArray("data");
                            if (data.length() > 0) {
                                JSONObject user = data.getJSONObject(0);
                                itemList = new ArrayList<>();
                                tableRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
                                itemList = new ArrayList<>();

                                String organizationImage = user.getString("uImg");
                                String actualImg = "http://" + Constants.IP + "/JobConnect_API" + organizationImage;

                                Glide.with(this)
                                        .asBitmap()
                                        .load(actualImg)
                                        .placeholder(R.drawable.baseline_qr_code_scanner_24) // Add a placeholder image resource
                                        .error(R.drawable.baseline_error_24) // Add an error image resource
                                        .into(new CustomTarget<Bitmap>() {
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

                                itemList.add(new JobKeyValue("Full Name ", user.getString("uName")));
                                itemList.add(new JobKeyValue("Phone Number", user.getString("uPhone")));
                                itemList.add(new JobKeyValue("Email", user.getString("uEmail")));
                                itemList.add(new JobKeyValue("Gender ", user.getString("gender")));
                                itemList.add(new JobKeyValue("Date of Birth", user.getString("uDob")));
                                itemList.add(new JobKeyValue("Highest Education ", user.getString("uEducation")));
                                itemList.add(new JobKeyValue("Interested Industry ", user.getString("uIndustry")));
                                itemList.add(new JobKeyValue("Permanent Address ", user.getString("per_address")));
                                itemList.add(new JobKeyValue("Temporary address ", user.getString("temp_address")));
                                itemAdapter = new JobInfo_Adp(itemList);
                                tableRecycleView.setAdapter(itemAdapter);

                                String interestedCategories = user.getString("uCategories");
                                String educationDetail = user.getString("education");
                                String expDetail = user.getString("experience");
                                String skillDetail = user.getString("skill");
                                String aboutUser = user.getString("uBio");
                                aboutMe.setText(aboutUser);

                                String[] catArray = interestedCategories.split(",+");
                                String[] expArray = expDetail.split(",+");
                                String[] skillArray = skillDetail.split(",+");
                                String[] eduArray = educationDetail.split(",+");

                                StringBuilder catStringBuilder = new StringBuilder();
                                for (int i = 0; i < catArray.length; i++) {
                                    catStringBuilder.append(catArray[i]);
                                    if (i < catArray.length - 1) {
                                        catStringBuilder.append("\n");
                                    }
                                }
                                String catResult = catStringBuilder.toString();
                                interestedCate.setText(catResult);

                                StringBuilder eduStringBuilder = new StringBuilder();
                                for (int i = 0; i < eduArray.length; i++) {
                                    eduStringBuilder.append(eduArray[i]);
                                    if (i < eduArray.length - 1) {
                                        eduStringBuilder.append("\n");
                                    }
                                }
                                String eduResult = eduStringBuilder.toString();
                                eduInfo.setText(eduResult);

                                StringBuilder skillStringBuilder = new StringBuilder();
                                for (int i = 0; i < skillArray.length; i++) {
                                    skillStringBuilder.append(skillArray[i]);
                                    if (i < skillArray.length - 1) {
                                        skillStringBuilder.append("\t\t\t\t");
                                    }
                                }
                                String skillResult = skillStringBuilder.toString();
                                skillInfo.setText(skillResult);

                                StringBuilder expStringBuilder = new StringBuilder();
                                for (int i = 0; i < expArray.length; i++) {
                                    expStringBuilder.append(expArray[i]);
                                    if (i < expArray.length - 1) {
                                        expStringBuilder.append("\n");
                                    }
                                }
                                String expResult = expStringBuilder.toString();
                                expInfo.setText(expResult);
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), "Error: Invalid JSON response from server", Toast.LENGTH_LONG).show();
                        throw new RuntimeException(e);
                    }

                },
                error -> {
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
        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);

        cvDownloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    downloadCvLayout();


            }
        });

        return view;
    }

    private void changeStatus(int status) {
        String statusValue;
        if(status==0){
            statusValue="Rejected";
        }else if (status==1) {
            statusValue="ShortListed";
        }else{
            statusValue="Pending";
        }
        StringRequest stringRequest=new StringRequest(Request.Method.POST,Constants.URL_CHANGE_STATUS,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Toast.makeText(getContext(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        throw new RuntimeException(e);
                    }

                },error -> {
            Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_SHORT).show();

        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", displayEmail);
                params.put("jobID", String.valueOf(jobId));
                params.put("status", statusValue);
                return params;
            }
        };
        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);


    }

    private void processBitmap(Bitmap resource) {
        this.bitmap = resource;
        profilePicDisplay.setImageBitmap(bitmap);
    }

    private void downloadCvLayout() {
        mainCvLayout.setDrawingCacheEnabled(true);
        mainCvLayout.buildDrawingCache(true);

        // Create a bitmap from the layout
        Bitmap bitmap = Bitmap.createBitmap(mainCvLayout.getWidth(), mainCvLayout.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);

        mainCvLayout.draw(canvas);

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String uniqueFileName = "Resume" + timeStamp + ".pdf";

        // Convert bitmap to PDF using iTextPDF
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + uniqueFileName;
        try {
            File file = new File(pdfPath);
            FileOutputStream outputStream = new FileOutputStream(file);

            PdfDocument pdfDocument = new PdfDocument();
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
            PdfDocument.Page page = pdfDocument.startPage(pageInfo);

            Canvas pdfCanvas = page.getCanvas();
            pdfCanvas.drawBitmap(bitmap, 0, 0, null);

            pdfDocument.finishPage(page);
            pdfDocument.writeTo(outputStream);
            pdfDocument.close();

            outputStream.flush();
            outputStream.close();

            // Add the PDF file to the device's downloads folder
            MediaScannerConnection.scanFile(getContext(), new String[]{pdfPath}, null, null);

            Toast.makeText(getContext(), "CV  downloaded successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.d("no cv download", "downloadCvLayout: " + e.getMessage());
            Toast.makeText(getContext(), "Error saving file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        // Disable the drawing cache
        mainCvLayout.setDrawingCacheEnabled(false);
    }


}

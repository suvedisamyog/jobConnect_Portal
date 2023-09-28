package com.example.jobconnect.JOB_PROVIDER_PART;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.jobconnect.UI_CONTROLLER.Change_Password;
import com.example.jobconnect.UI_CONTROLLER.MainActivity;
import com.example.jobconnect.Validations.VD_Registration;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Org_Profile extends Fragment {

    View view;
    Bitmap bitmap;
    Switch switchButton;
    TextView editName, editPhone, editWebsite,imagetextview,passError,editLocation;
    TextInputEditText email, name, phone, website ,location;
    AppCompatButton btnSaveChange, changeLogo;
    ShapeableImageView logo;
    TextInputLayout nameerror,phoneerror,emailField,loctionerror;
    String displayEmail;
    String organizationWebsite,organizationPhone,organizationEmail,organizationName,organizationLocation;
    String EditedName,EditedPhone,EditedWeb,EditedLocation;
    ImageView setting;
    LinearLayout orgProfileView;
    EditText currentPass,newPass,reNewPass;
    FrameLayout fragment_container;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_org__profile, container, false);
        switchButton = view.findViewById(R.id.switchButton);
        email = view.findViewById(R.id.orgDEmail);
        name = view.findViewById(R.id.orgDName);
        editName = view.findViewById(R.id.editName);
        phone = view.findViewById(R.id.orgDContact);
        editPhone = view.findViewById(R.id.editPhone);
        location = view.findViewById(R.id.orgDLocation);
        loctionerror=view.findViewById(R.id.orgDLocationF);
        orgProfileView=view.findViewById(R.id.orgProfileView);
        fragment_container=view.findViewById(R.id.fragment_container);

        editLocation = view.findViewById(R.id.editLocation);
        website = view.findViewById(R.id.orgDWeb);
        editWebsite = view.findViewById(R.id.editWebsite);
        btnSaveChange = view.findViewById(R.id.btnSaveChange);
        logo = view.findViewById(R.id.profileLogo);
        changeLogo = view.findViewById(R.id.changeLogo);
        nameerror=view.findViewById(R.id.orgDNameF);
        phoneerror=view.findViewById(R.id.orgDContactF);
        emailField=view.findViewById(R.id.orgDEmailF);
        imagetextview=view.findViewById(R.id.imagetextview);
        setting=view.findViewById(R.id.settingOrg);
        SessionManager sessionManager = new SessionManager(requireContext());
        if (sessionManager.checkSession()) {
            displayEmail = sessionManager.getSessionDetail("useremail");
        } else {
            startActivity(new Intent(getContext(), MainActivity.class));
        }
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showmenu(v);
            }
        });
        fetchDataFromServer();








        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()

            {
                @Override
                public void onCheckedChanged (CompoundButton buttonView,boolean isChecked){
                if (isChecked) {
                    enableEditing();
                } else {
                    DisableEditing();
                }
            }
            });
        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeName();
            }
        });
        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeName();
            }
        });
        editPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePhone();
            }
        });
        editWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeWebsite();
            }
        });
        editLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLocation();
            }
        });
        changeLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visblebtn(imagetextview);
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });
        btnSaveChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChange();
            }
        });
        orgProfileView.setVisibility(View.VISIBLE);
        fragment_container.setVisibility(View.GONE);

        return view;
        }



    private void showmenu(View anchorView) {
        PopupMenu popupMenu = new PopupMenu(getContext(), anchorView);
        popupMenu.getMenuInflater().inflate(R.menu.settings, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ChangePassword:

                        orgProfileView.setVisibility(View.GONE);
                        fragment_container.setVisibility(View.VISIBLE);
                        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, new Change_Password(displayEmail));
                        transaction.commit();
                        return true;
                    case R.id.LogOut:
                        SessionManager sessionManager=new SessionManager(getContext());
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



    private void changeWebsite() {
        visblebtn(editWebsite);
        website.setEnabled(true);
    }

    private void changeLocation() {
        visblebtn(editLocation);
        location.setEnabled(true);
    }

    private void visblebtn(TextView editViewName) {
        btnSaveChange.setVisibility(View.VISIBLE);
        editViewName.setVisibility(View.GONE);
    }


    private void changePhone() {
        visblebtn(editPhone);
        phone.setEnabled(true);

    }

    private void changeName() {
        visblebtn(editName);
        name.setEnabled(true);

    }

    private void fetchDataFromServer() {
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Constants.URL_ORG_PROFILE_FETCH,
                response -> {
                    try {
                        Log.d("orgprofile", "onCreateView: "+response);
                        JSONObject jsonObject = new JSONObject(response);
                        if (!jsonObject.getBoolean("error")){
                            JSONArray data = jsonObject.getJSONArray("data");
                            if (data.length() > 0) {
                                JSONObject orgData = data.getJSONObject(0);
                                organizationName = orgData.getString("oName");
                                organizationEmail = orgData.getString("oEmail");
                                organizationPhone = orgData.getString("oPhone");
                                String organizationImage = orgData.getString("oImg");
                                organizationWebsite = orgData.getString("oWeb");
                                organizationLocation=orgData.getString("oLocation");
                                String actualImg="http://"+Constants.IP+"/JobConnect_API"+organizationImage;



                                name.setText(organizationName);
                                email.setText(organizationEmail);
                                phone.setText(organizationPhone);
                                website.setText(organizationWebsite);
                                location.setText(organizationLocation);

                                Glide.with(this)
                                        .asBitmap()
                                        .load(actualImg)
                                        .placeholder(R.drawable.baseline_email_24) // Add a placeholder image resource
                                        .error(R.drawable.baseline_error_24) // Add an error image resource
                                        .into(new CustomTarget<Bitmap>(){
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                // Process the fetched bitmap here
                                                processBitmap(resource);
                                            }
                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {
                                                // Optional: Handle when the resource is cleared
                                            }
                                        });
                            }
                            if (!jsonObject.getBoolean("error")) {
                                switchButton.setChecked(false);
                            }

                        }else {
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

    private void processBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        logo.setImageBitmap(bitmap);


    }

    private void DisableEditing () {
            editName.setVisibility(view.GONE);
            editPhone.setVisibility(view.GONE);
            editWebsite.setVisibility(view.GONE);
            editLocation.setVisibility(view.GONE);
            changeLogo.setVisibility(view.GONE);
            emailField.setVisibility(View.VISIBLE);
            email.setVisibility(View.VISIBLE);


            name.setEnabled(false);
            email.setEnabled(false);
            phone.setEnabled(false);
            website.setEnabled(false);
            location.setEnabled(false);

            fetchDataFromServer();
            nameerror.setError(null);
            phoneerror.setError(null);
            loctionerror.setError(null);

    }

        private void enableEditing () {
            email.setVisibility(View.GONE);
            editName.setVisibility(view.VISIBLE);
            editPhone.setVisibility(view.VISIBLE);
            editWebsite.setVisibility(view.VISIBLE);
            editLocation.setVisibility(view.VISIBLE);
            changeLogo.setVisibility(view.VISIBLE);
            emailField.setVisibility(View.GONE);
            email.setVisibility(View.GONE);


            name.setEnabled(false);
            email.setEnabled(false);
            phone.setEnabled(false);
            website.setEnabled(false);
            location.setEnabled(false);


        }
    private void saveChange() {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();

        EditedWeb=website.getText().toString();
        EditedName=name.getText().toString();
        EditedPhone=phone.getText().toString();
        EditedLocation=location.getText().toString();


        if(!VD_Registration.isValidName(EditedName)){
            nameerror.setError("Name should be 5  valid character");
        } else if (EditedPhone.length()!=10 || EditedPhone.isEmpty()) {
            phoneerror.setError("Phone number must be of 10 digit");
        } else if (EditedLocation.isEmpty()) {
            loctionerror.setError("Must specify the location of organization");
        } else{
            nameerror.setError(null);
            phoneerror.setError(null);
            if(EditedWeb.isEmpty()){
                EditedWeb="Not Available";
            }else{
                EditedWeb=EditedWeb;
            }

            if(bitmap!=null){
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                byte[] bytes=byteArrayOutputStream.toByteArray();
                final String base64Img= Base64.encodeToString(bytes,Base64.DEFAULT);
                StringRequest stringRequest=new StringRequest(Request.Method.POST,Constants.URL_ORG_PROFILE_UPDATE,
                        response -> {
                            try {
                                JSONObject jsonObject=new JSONObject(response);
                                Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                                if(!jsonObject.getBoolean("error")){
                                    SessionManager sessionManager=new SessionManager(getContext());
                                    sessionManager.updateName(EditedName);
                                    DisableEditing();
                                    btnSaveChange.setVisibility(View.GONE);
                                }

                            } catch (JSONException e) {
                                Toast.makeText(getContext(), "Error: Invalid JSON response from server", Toast.LENGTH_LONG).show();
                                Log.d("resp", "saveChange: "+response);
                                throw new RuntimeException(e);
                            }
                        },
                        error -> {
                            Toast.makeText(getContext(), "error:" + error.getMessage(), Toast.LENGTH_LONG).show();

                        }){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("email", organizationEmail);
                        params.put("name", EditedName);
                        params.put("phone", EditedPhone);
                        params.put("website", EditedWeb);
                        params.put("image", base64Img);
                        params.put("location", EditedLocation);

                        return params;
                    }
                };
                RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
            }



        }







        }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
                logo.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


    }


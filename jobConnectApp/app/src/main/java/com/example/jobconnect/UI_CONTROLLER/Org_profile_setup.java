package com.example.jobconnect.UI_CONTROLLER;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.jobconnect.Constants.Constants;
import com.example.jobconnect.JOB_PROVIDER_PART.Org_Nav;
import com.example.jobconnect.JOB_SEEKER_PART.User_Nav;
import com.example.jobconnect.Modules.RequestHandler;
import com.example.jobconnect.Modules.SessionManager;
import com.example.jobconnect.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Org_profile_setup extends AppCompatActivity {

 TextInputEditText email,name,phone,website,location;
 TextInputLayout phoneField,LocationField;
 String displayName,displayEmail,phoneValue,webValue,locationValue;

    AppCompatButton uploadOrg,btnAddLogo;
    Bitmap bitmap;
    ImageView displayLogo;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_profile_setup);

        displayEmail = getIntent().getStringExtra("email");
        displayName = getIntent().getStringExtra("name");

        email=findViewById(R.id.org_pro_email);
        name=findViewById(R.id.org_pro_name);
        location=findViewById(R.id.org_pro_location);
        

        email.setText(displayEmail);
        name.setText(displayName);

        phone=findViewById(R.id.org_pro_phone);
        website=findViewById(R.id.org_pro_website);
        btnAddLogo=findViewById(R.id.oLogo);

        phoneField=findViewById(R.id.phoneField);
        LocationField=findViewById(R.id.LocationField);
        displayLogo=findViewById(R.id.displayLogo);


        phoneValue=phone.getText().toString();
        webValue=website.getText().toString();
        locationValue=location.getText().toString();


         uploadOrg=findViewById(R.id.uploadOrg);


        btnAddLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

         uploadOrg.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 uploadToDb();
             }
         });


    }

    private void uploadToDb() {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        phoneValue=phone.getText().toString();
        webValue=website.getText().toString();
        locationValue=location.getText().toString();


        if(phoneValue.length()!=10 || phoneValue.isEmpty()){
            phoneField.setError("Phone number must be 10 digit");
        } else if (locationValue.isEmpty()) {
            LocationField.setError("Must specify the location of organization");
        } else{
            
            phoneField.setError(null);
            LocationField.setError(null);
            if(bitmap==null){
                Toast.makeText(getApplicationContext(),"Please upload  a logo",Toast.LENGTH_SHORT).show();
            }else{
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                byte[] bytes=byteArrayOutputStream.toByteArray();
                final String base64Img= Base64.encodeToString(bytes,Base64.DEFAULT);
                webValue = webValue.isEmpty() ? "Not Available" : webValue;


                StringRequest stringRequest=new StringRequest(Request.Method.POST, Constants.URL_ORG_PROFILE,
                        response ->{
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if(!jsonObject.getBoolean("error")){
                                    sessionManager=new SessionManager(getApplicationContext());
                                    sessionManager.updateIsCompleteValue("true");
                                    startActivity(new Intent(getApplicationContext(), Org_Nav.class));
                                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
                                Log.d("tt", "uploadToDb: "+response+""+e);
                                Toast.makeText(getApplicationContext(), "Error: Invalid JSON response from server", Toast.LENGTH_LONG).show();

                                throw new RuntimeException(e);
                            }

                        },error -> {
                    Toast.makeText(getApplicationContext(), "error:" + error.getMessage(), Toast.LENGTH_LONG).show();

                } ){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("oEmail",displayEmail);
                        params.put("oName",displayName);
                        params.put("oPhone",phoneValue);
                        params.put("oImg",base64Img);
                        params.put("oWeb",webValue);
                        params.put("oLocation",locationValue);
                        return params;
                    }
                };
                RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

            }


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                displayLogo.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
package com.example.jobconnect.UI_CONTROLLER;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.util.Log;
import android.view.View;



import android.content.Intent;
import android.os.Bundle;

import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;

import com.android.volley.toolbox.StringRequest;
import com.example.jobconnect.Constants.Constants;
import com.example.jobconnect.Modules.RequestHandler;
import com.example.jobconnect.Validations.VD_Reg_onChnge_Field;
import com.example.jobconnect.R;
import com.example.jobconnect.Validations.VD_Registration;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Registration extends AppCompatActivity implements  View.OnClickListener {
    private AppCompatButton jobseekerButton, jobproviderButton,btnRegisterUser,btnRegisterOrg;
    private TextView alreadyRegistered;
    private LinearLayout orgRegistrationLayout, userRegistrationLayout;
    private TextInputEditText userName,userEmail,userPassword,userRePassword,orgName,orgEmail,orgPassword,orgRePassword;
    private TextInputLayout userNameField,userEmailField,userPasswordField,userRePasswordField,orgNameField,orgEmailField,orgPasswordField,orgRePasswordField;

   private ProgressBar progressBar;
   private  FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();


        //buttons and layout  fetching to toogle
        jobseekerButton = findViewById(R.id.seeker);
        jobproviderButton = findViewById(R.id.provider);
        orgRegistrationLayout = findViewById(R.id.orgRegistration);
        userRegistrationLayout = findViewById(R.id.userRegistration);
        alreadyRegistered=findViewById(R.id.alreadyRegistered);

        //job seeker side fetching
        userName=findViewById(R.id.userName);
        userEmail=findViewById(R.id.userEmail);
        userPassword=findViewById(R.id.userPassword);
        userRePassword=findViewById(R.id.userRePassword);
        userNameField=findViewById(R.id.userNameField);
        userEmailField=findViewById(R.id.userEmailField);
        userPasswordField=findViewById(R.id.userPasswordField);
        userRePasswordField=findViewById(R.id.userRePasswordField);

        //jobprovider side fetching
        orgName=findViewById(R.id.orgName);
        orgEmail=findViewById(R.id.orgEmail);
        orgPassword=findViewById(R.id.orgPassword);
        orgRePassword=findViewById(R.id.orgRePassword);
        orgNameField=findViewById(R.id.orgNameField);
        orgEmailField=findViewById(R.id.orgEmailField);
        orgPasswordField=findViewById(R.id.orgPasswordField);
        orgRePasswordField=findViewById(R.id.orgRePasswordField);

        progressBar=findViewById(R.id.registerProgress);

        //registration button
        btnRegisterUser=findViewById(R.id.btnRegisterUser);
        btnRegisterOrg=findViewById(R.id.btnRegisterOrg);

        btnRegisterUser.setOnClickListener(this);
        btnRegisterOrg.setOnClickListener(this);
        alreadyRegistered.setOnClickListener(this);
        jobseekerButton.setOnClickListener(this);
        jobproviderButton.setOnClickListener(this);


        focusChangeValidation();



    }

    private void focusChangeValidation() {
        VD_Reg_onChnge_Field.focusChangeValidation(userName, userNameField);
        VD_Reg_onChnge_Field.focusChangeValidation(userEmail, userEmailField);
        VD_Reg_onChnge_Field.focusChangeValidation(userPassword, userPasswordField);
        VD_Reg_onChnge_Field.focusChangeValidation(orgName, orgNameField);
        VD_Reg_onChnge_Field.focusChangeValidation(orgEmail, orgEmailField);
        VD_Reg_onChnge_Field.focusChangeValidation(orgPassword, orgPasswordField);
        VD_Reg_onChnge_Field.passwordMatch(userPassword,userRePassword,userRePasswordField);
        VD_Reg_onChnge_Field.passwordMatch(orgPassword,orgRePassword,orgRePasswordField);
    }


    private  void registerUser(final String userType){
        int errors=0;
        progressBar.setVisibility(View.VISIBLE);

        final String name, email, pass, repass,isComplete;
        if (userType.equals("user")) {
            name = userName.getText().toString().trim();
            email = userEmail.getText().toString().trim();
            pass = userPassword.getText().toString().trim();
            repass = userRePassword.getText().toString().trim();
            isComplete = "false";
        } else {
            name = orgName.getText().toString().trim();
            email = orgEmail.getText().toString().trim();
            pass = orgPassword.getText().toString().trim();
            repass = orgRePassword.getText().toString().trim();
            isComplete = "false";
        }

        if(!VD_Registration.isValidPassword(pass)){
            errors=1;
            String msgPassError="Minimum 6 characters long\nMust contain at least 1 uppercase letter, 1 lowercase letter, 1 number & 1 special character";
            Toast.makeText(getApplicationContext(), msgPassError, Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);

        }
        if(!VD_Registration.isValidRePassword(pass,repass)){
            errors=1;
            Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);

        }
        if(errors==0){
            StringRequest stringRequest=new StringRequest(Request.Method.POST, Constants.URL_REGISTER,
                    response -> {
                        progressBar.setVisibility(View.INVISIBLE);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.d("registration response", "registerUser: "+response);



                            if(jsonObject.getString("message").equals("Registered successfully")) {
                                mAuth.createUserWithEmailAndPassword(email, pass)
                                        .addOnCompleteListener(this, task -> {
                                            Log.d("user", "registerUser: first");

                                            if (task.isSuccessful()) {

                                                FirebaseUser user = mAuth.getCurrentUser();
                                                Log.d("user", "registerUser: "+user);

                                                Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_LONG).show();
                                                if (user != null && !user.isEmailVerified()) {

                                                    user.sendEmailVerification()
                                                            .addOnCompleteListener(emailTask -> {
                                                                if (emailTask.isSuccessful()) {
                                                                    Toast.makeText(getApplicationContext(), "Registration successful. Please check your email for verification.", Toast.LENGTH_LONG).show();
                                                                } else {
                                                                    Toast.makeText(getApplicationContext(), "Failed to send verification email.", Toast.LENGTH_LONG).show();
                                                                }
                                                            });
                                                }else{
                                                    Log.d("user", "registerUser: second");

                                                }

                                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                            } else {
                                                // Registration failed
                                                Exception exception = task.getException();
                                                if (exception instanceof FirebaseAuthUserCollisionException) {
                                                    // Email already exists in Firebase Authentication
                                                    Toast.makeText(getApplicationContext(), "Email is already in use. Please use a different email.", Toast.LENGTH_LONG).show();
                                                } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                                                    // Invalid email format
                                                    Toast.makeText(getApplicationContext(), "Invalid email format. Please enter a valid email address.", Toast.LENGTH_LONG).show();
                                                } else {
                                                    // Other registration error
                                                    Toast.makeText(getApplicationContext(), "Registration failed. " + exception.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                Toast.makeText(getApplicationContext(),"Successfuly created",Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }else{
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error: Invalid JSON response from server", Toast.LENGTH_LONG).show();
                        }
                    }, error ->{
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "error:"+error.getMessage(), Toast.LENGTH_LONG).show();

            }
            ){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params=new HashMap<>();
                    params.put("name",name);
                    params.put("email",email);
                    params.put("userType",userType);
                    params.put("completeProfile",isComplete);

                    return params;
                }
            };

//        RequestQueue requestQueue= Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
            RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        }



    }




    @Override
    public void onClick(View v) {
        if (v == btnRegisterUser){registerUser("user");}
        if (v == btnRegisterOrg){registerUser("org");}
        if(v==alreadyRegistered){login();}
        if(v==jobseekerButton){userRegPage();}
        if(v==jobproviderButton){orgRegPage();}
    }

    private void orgRegPage() {
        userRegistrationLayout.setVisibility(View.GONE);
        orgRegistrationLayout.setVisibility(View.VISIBLE);
    }

    private void userRegPage() {
        orgRegistrationLayout.setVisibility(View.GONE);
        userRegistrationLayout.setVisibility(View.VISIBLE);
    }

    private void login() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }
}


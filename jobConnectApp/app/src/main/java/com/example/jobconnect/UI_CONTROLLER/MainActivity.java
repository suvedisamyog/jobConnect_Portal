package com.example.jobconnect.UI_CONTROLLER;



import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.example.jobconnect.Constants.Constants;


import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.jobconnect.JOB_PROVIDER_PART.Org_Nav;
import com.example.jobconnect.JOB_SEEKER_PART.User_Nav;
import com.example.jobconnect.Modules.RequestHandler;
import com.example.jobconnect.Modules.SessionManager;
import com.example.jobconnect.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView newUser, forgetPass;
    TextInputEditText loginEmail, loginPassword;
    TextInputLayout loginEmailField, loginPassField;
    AppCompatButton btnLogin;
    Boolean isError;
    ProgressBar loginProgress;
    SessionManager sessionManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        sessionManager=new SessionManager(getApplicationContext());
        if(sessionManager.checkSession()){
           String userType= sessionManager.getSessionDetail("usertype");
            if(userType.equals("org")){
                Intent intent = new Intent(getApplicationContext(), Org_Nav.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return;

            } else if (userType.equals("user")) {
                Intent intent = new Intent(getApplicationContext(), User_Nav.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return;
            }else{
                Log.d("null", "onCreate: "+userType);
            }

        }else{
            Log.d("No session", "onCreate:No session ");
        }





        newUser = findViewById(R.id.newRegister);
        forgetPass = findViewById(R.id.forgotPassword);
        btnLogin = findViewById(R.id.btnLogin);
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        loginEmailField = findViewById(R.id.loginEmailField);
        loginPassField = findViewById(R.id.loginPassField);
        loginProgress = findViewById(R.id.loginProgress);

        btnLogin.setOnClickListener(this);
        forgetPass.setOnClickListener(this);
        newUser.setOnClickListener(this);


    }

    public void onClick(View v) {
        if (v == btnLogin) {
            btnLogin();
        }
        if (v == newUser) {
            newUser();
        }
        if (v == forgetPass) {
            forgetPass();
        }

    }

    private void btnLogin() {
        String email = loginEmail.getText().toString().trim();
        String password = loginPassword.getText().toString();
        checkEmpty(email, password);
        if (!isError) {
            loginProgress.setVisibility(View.VISIBLE);

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                if (user.isEmailVerified()) {
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST,
                                            Constants.URL_LOGIN,
                                            response -> {
                                                try {
                                                    JSONObject obj = new JSONObject(response);
                                                    if (!obj.getBoolean("error")) {
                                                        String dbuserType = obj.getString("userType");
                                                        String dbname = obj.getString("name");
                                                        String dbemail = obj.getString("email");
                                                        String dbisComplete = obj.getString("isComplete");
                                                        sessionManager=new SessionManager(getApplicationContext());
                                                        sessionManager.createSession(dbname,dbemail,dbuserType,dbisComplete);
                                                        if (dbuserType.equals("org")) {
                                                            startActivity(new Intent(getApplicationContext(), Org_Nav.class));
                                                            finish();
                                                            return;
                                                        } else if (dbuserType.equals("user")) {
                                                            startActivity(new Intent(getApplicationContext(), User_Nav.class));
                                                            finish();
                                                            return;
                                                        }

                                                    } else {
                                                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                                    }
                                                    Log.d("TAG", "btnLogin: " + response);
                                                    loginProgress.setVisibility(View.INVISIBLE);



                                                } catch (JSONException e) {
                                                    loginProgress.setVisibility(View.INVISIBLE);
                                                    e.printStackTrace();
                                                    Log.d("TAG2", "btnLogin: " + e);

                                                    Toast.makeText(getApplicationContext(), "error:" + e.getMessage(), Toast.LENGTH_SHORT).show();


                                                }

                                            }, error -> {
                                        loginProgress.setVisibility(View.INVISIBLE);
                                        Toast.makeText(getApplicationContext(), "errorsss:" + error.getMessage(), Toast.LENGTH_SHORT).show();
                                        error.printStackTrace();
                                        Log.d("TAG3", "btnLogin: " + error);


                                    }) {
                                        @Nullable
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            Map<String, String> params = new HashMap<>();
                                            params.put("email", email);
                                            return params;

                                        }
                                    };
                                    RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
                                    loginProgress.setVisibility(View.GONE);

                                } else {

                                    Toast.makeText(this, "Please verify your email first.", Toast.LENGTH_LONG).show();
                                    sendVerificationEmail();
                                    loginProgress.setVisibility(View.GONE);
                                }
                            }
                        } else {
                            Toast.makeText(this, "Login failed. " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            loginProgress.setVisibility(View.GONE);
                        }
                    });




        }

    }

    private void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Verification email sent. Please check your inbox.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this, "Failed to send verification email.", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private void newUser() {
        startActivity(new Intent(getApplicationContext(), Registration.class));
    }

    private void forgetPass() {
        startActivity(new Intent(getApplicationContext(), ForgetPassword.class));
    }


    private void checkEmpty(String email, String password) {

        if (email.isEmpty()) {
            loginEmailField.setError("Email Cannot Be Empty");
            isError = true;
        } else {
            loginEmailField.setError(null);
            isError = false;
        }
        if (password.isEmpty()) {
            loginPassField.setError("Password Cannot Be Empty");
            isError = true;
        } else {
            loginPassField.setError(null);
            isError = false;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);

    }


}
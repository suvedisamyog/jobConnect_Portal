package com.example.jobconnect.UI_CONTROLLER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.jobconnect.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;


public class ForgetPassword extends AppCompatActivity {
    AppCompatButton btnResetPass;
    TextInputLayout forgetPassField;
    TextInputEditText forgotPassEmail;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        btnResetPass=findViewById(R.id.btnResetPass);
        forgetPassField=findViewById(R.id.forgetPassField);
        forgotPassEmail=findViewById(R.id.forgotPassEmail);
         progressBar=findViewById(R.id.progressBar);

        btnResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiatePasswordReset();
            }
        });





    }

    private void initiatePasswordReset() {
        progressBar.setVisibility(View.VISIBLE);
        String email = forgotPassEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            forgetPassField.setError("Please enter your email");
        }else{
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Password reset email sent to your email address.", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            } else {
                                Toast.makeText(getApplicationContext(), "Failed to send password reset email. Please check your email address and try again.", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
        }



    }

}
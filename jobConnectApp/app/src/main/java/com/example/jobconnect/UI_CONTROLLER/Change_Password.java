package com.example.jobconnect.UI_CONTROLLER;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.jobconnect.Constants.Constants;
import com.example.jobconnect.Modules.RequestHandler;
import com.example.jobconnect.Modules.SessionManager;
import com.example.jobconnect.R;
import com.example.jobconnect.Validations.VD_Registration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Change_Password extends Fragment {

       String displayEmail;
    AppCompatButton btnchange;
    TextInputEditText currentPassword,newPassword,rePassword;
    TextInputLayout currentPassField,newPassField,rePassField;
    public Change_Password(String displayEmail) {
        this.displayEmail=displayEmail;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_change_password, container, false);
        btnchange=view.findViewById(R.id.changePassBtn);

        currentPassword=view.findViewById(R.id.currentPassword);
        newPassword=view.findViewById(R.id.newPassword);
        rePassword=view.findViewById(R.id.rePassword);

        currentPassField=view.findViewById(R.id.currentPassField);
        newPassField=view.findViewById(R.id.NewPassField);
        rePassField=view.findViewById(R.id.rePassField);




        btnchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPasswordVal = currentPassword.getText().toString().trim();
                String newPasswordVal = newPassword.getText().toString().trim();
                String rePasswordVal = rePassword.getText().toString().trim();

                if(!VD_Registration.isValidPassword(newPasswordVal)){
                    newPassField.setError("Minimum 6 characters long\nMust contain at least 1 uppercase letter, 1 lowercase letter, 1 number & 1 special character");
                    rePassField.setError(null);

                }else if(!VD_Registration.isValidRePassword(newPasswordVal,rePasswordVal)){
                    rePassField.setError("Password and Re-Password doesnot match");
                    newPassField.setError(null);

                }else{
                   newPassField.setError(null);
                   rePassField.setError(null);
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        // Reauthenticate the user to make sure the current password is correct
                        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPasswordVal);
                        user.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // If reauthentication is successful, update the password
                                            user.updatePassword(newPasswordVal)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                SessionManager sessionManager = new SessionManager(getContext());
                                                                sessionManager.logOut();
                                                                Intent intent = new Intent(getContext(), MainActivity.class);
                                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                startActivity(intent);
                                                                requireActivity().finish();
                                                                Toast.makeText(getActivity(), "Password changed successfully.", Toast.LENGTH_SHORT).show();

                                                            } else {
                                                                Toast.makeText(getActivity(), "Failed to change password. Please try again later.", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        } else {
                                            Toast.makeText(getActivity(), "Current password is incorrect.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(getActivity(), "User not signed in.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return view;
    }
}
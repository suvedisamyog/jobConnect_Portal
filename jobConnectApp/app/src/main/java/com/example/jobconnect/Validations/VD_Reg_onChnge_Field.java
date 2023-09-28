package com.example.jobconnect.Validations;


import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import com.example.jobconnect.R;



public class VD_Reg_onChnge_Field {

    static String msgNameError="Should be at least 3 character long\nCan Only Contain AlphaNumric & _ character";
    static String msgEmailError ="Email Address Not Valid";
    static String msgPassError="Minimum 6 characters long\nMust contain at least 1 uppercase letter, 1 lowercase letter, 1 number & 1 special character";

    static String msgRePassError="Passwords don't Match";
    public static String errorMessage;

    public static void focusChangeValidation(EditText editText, TextInputLayout textInputLayout) {
        editText.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                String input = editText.getText().toString().trim();
                boolean isValid = false;
                switch (textInputLayout.getId()) {
                    case R.id.userNameField:
                    case R.id.orgNameField:
                        isValid = VD_Registration.isValidName(input);
                        errorMessage=msgNameError;
                        break;
                    case R.id.userEmailField:
                    case R.id.orgEmailField:
                        isValid = VD_Registration.isValidEmail(input);
                        errorMessage=msgEmailError;
                        break;
                    case R.id.userPasswordField:
                    case R.id.orgPasswordField:
                        isValid = VD_Registration.isValidPassword(input);
                        errorMessage=msgPassError;
                        
                        break;

                    default:
                        break;
                }
                if (!isValid) {
                    textInputLayout.setError(errorMessage);

                } else {
                    textInputLayout.setError(null);
                }
            }
        });
    }

    public static void passwordMatch(EditText password, EditText Repassword,TextInputLayout textInputLayout){
        Repassword.setOnFocusChangeListener((view,hasFocus)->{
            if(!hasFocus){

                String pass = password.getText().toString().trim();
                String Repass = Repassword.getText().toString().trim();
                boolean isValid = false;
                isValid=VD_Registration.isValidRePassword(pass,Repass);
                if(!isValid){
                    errorMessage=msgRePassError;
                    textInputLayout.setError(errorMessage);
                }else{
                    textInputLayout.setError(null);


                }


            }
        });
    }

}

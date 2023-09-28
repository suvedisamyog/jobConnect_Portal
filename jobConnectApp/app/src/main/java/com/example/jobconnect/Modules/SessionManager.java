package com.example.jobconnect.Modules;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.jobconnect.UI_CONTROLLER.MainActivity;

public class SessionManager {
    Context context;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    private  final String PREF_FILE_NAME="checkLogin";
    private  final String KEY_NAME="username";
    private  final String KEY_EMAIL="useremail";
    private  final String KEY_USERTYPE="usertype";
    private  final String KEY_ISCOMPLETE="isComplete";


    public SessionManager(Context context){
        this.context=context;
        sp=context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        editor=sp.edit();

    }
    public boolean checkSession(){
        if(sp.contains(KEY_EMAIL) ){
            return true;
        }else{
            return false;
        }
    }
    public boolean checkProfile() {
        if (sp.contains(KEY_ISCOMPLETE)) {
            String isComplete = sp.getString(KEY_ISCOMPLETE, null);
            if (isComplete != null && isComplete.equals("false")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public String getSessionDetail(String key){
        String value=sp.getString(key,null);
        return  value;
    }
    public void createSession(String name, String email, String userType, String dbisComplete){
        editor.putString(KEY_NAME,name);
        editor.putString(KEY_EMAIL,email);
        editor.putString(KEY_USERTYPE,userType);
        editor.putString(KEY_ISCOMPLETE,dbisComplete);
        editor.commit();
    }

    public void updateIsCompleteValue(String dbIsComplete) {
        editor.putString(KEY_ISCOMPLETE, dbIsComplete);
        editor.apply();
    }

    public void updateName(String updateName) {
        editor.putString(KEY_NAME, updateName);
        editor.apply();
    }

    public  void logOut(){
        editor.clear();
        editor.apply();
        context.startActivity(new Intent(context, MainActivity.class));

    }

}

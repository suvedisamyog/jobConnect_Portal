package com.example.jobconnect.JOB_SEEKER_PART;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.example.jobconnect.JOB_PROVIDER_PART.Org_Home;
import com.example.jobconnect.JOB_PROVIDER_PART.Org_Nav;
import com.example.jobconnect.JOB_PROVIDER_PART.Org_Profile;
import com.example.jobconnect.Modules.SessionManager;
import com.example.jobconnect.R;
import com.example.jobconnect.UI_CONTROLLER.Change_Password;
import com.example.jobconnect.UI_CONTROLLER.Public_profile;
import com.example.jobconnect.UI_CONTROLLER.User_profile_setup;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class User_Nav extends AppCompatActivity {
    private FrameLayout fragment_container;
    private BottomNavigationView bottomNav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_nav);
        String displayName = null;
        String displayEmail ;

        SessionManager  s=new SessionManager(getApplicationContext());
         if(s.checkSession()){
             displayName= s.getSessionDetail("username");
             displayEmail = s.getSessionDetail("useremail");

             if(s.checkProfile()){
                 Intent intentProfile=new Intent(getApplicationContext(), User_profile_setup.class);
                 intentProfile.putExtra("email",displayEmail);
                 intentProfile.putExtra("name",displayName);
                 startActivity(intentProfile);
                 finish();

             }
         }
        fragment_container = findViewById(R.id.fragment_container);
        bottomNav = findViewById(R.id.bottomNav_user);
        replaceFragment(new User_Home());

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                switch (itemId) {
                    case R.id.userHome:
                        replaceFragment(new User_Home());
                        break;
                    case R.id.appliedJob:
                        replaceFragment(new Applied_Jobs());
                        break;
                    case R.id.savedJob:
                        replaceFragment(new saved_jobs());
                        break;
                    case R.id.resume:
                        replaceFragment(new cv_profile());
                        break;
                    case R.id.userProfile:
                        replaceFragment(new User_Profile());
                        break;

                    default:
                        replaceFragment(new User_Home());
                        break;


                }
                return true;
            }
        });
    }

    private void replaceFragment(Fragment fragment) {

        FragmentTransaction transaction =  getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);

        FrameLayout fragmentContainer = null;
        if (currentFragment instanceof User_Home) {
            int backStackEntryCount = fragmentManager.getBackStackEntryCount();
            if (backStackEntryCount > 0) {
                fragmentManager.popBackStack();
            } else {
                super.onBackPressed();
            }
            finish();
            System.exit(0);
        } else if (currentFragment instanceof Applied_Jobs) {
            startActivity(new Intent(getApplicationContext(), User_Nav.class));
            finish();
        } else if (currentFragment instanceof saved_jobs) {
            startActivity(new Intent(getApplicationContext(), User_Nav.class));
            finish();
        } else if (currentFragment instanceof cv_segment) {
            startActivity(new Intent(getApplicationContext(), User_Nav.class));
            finish();
        } else if (currentFragment instanceof cv_profile) {
            fragmentContainer = findViewById(R.id.fragment_container);
            Switch switchButton = fragmentContainer.findViewById(R.id.switchButton);
            if (switchButton.isChecked()) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new User_Profile())
                        .commit();
            } else {
                startActivity(new Intent(getApplicationContext(), User_Nav.class));
                finish();
            }

        } else if (currentFragment instanceof User_Profile) {

                startActivity(new Intent(getApplicationContext(), User_Nav.class));
                finish();



        } else if (currentFragment instanceof Public_profile) {
            startActivity(new Intent(getApplicationContext(), User_Nav.class));
            finish();
        } else if (currentFragment instanceof IndividualJobs) {
            super.onBackPressed();

        } else {
            int backStackEntryCount = fragmentManager.getBackStackEntryCount();
            if (backStackEntryCount > 0) {
                finish();
            } else {
                super.onBackPressed();
            }
        }

    }








}
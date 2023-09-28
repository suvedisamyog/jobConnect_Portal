package com.example.jobconnect.JOB_PROVIDER_PART;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Switch;

import com.example.jobconnect.JOB_SEEKER_PART.IndividualJobs;
import com.example.jobconnect.Modules.SessionManager;
import com.example.jobconnect.R;
import com.example.jobconnect.UI_CONTROLLER.Org_profile_setup;
import com.example.jobconnect.UI_CONTROLLER.Public_profile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Org_Nav extends AppCompatActivity {
    private BottomNavigationView bottomNav;
    private FrameLayout fragment_container;
    String displayName = null;
    String displayEmail ;
    int count=0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_nav);

        SessionManager sessionManager = new SessionManager(getApplicationContext());


        if(sessionManager.checkSession()){
            displayName= sessionManager.getSessionDetail("username");
            displayEmail = sessionManager.getSessionDetail("useremail");

            if(sessionManager.checkProfile()){
                Intent intentProfile=new Intent(getApplicationContext(), Org_profile_setup.class);
                intentProfile.putExtra("email",displayEmail);
                intentProfile.putExtra("name",displayName);
                intentProfile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentProfile);
                finish();

            }
        }

        fragment_container = findViewById(R.id.fragment_container);
        bottomNav = findViewById(R.id.bottomNav_org);
        replaceFragment(new Org_Home());


        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                switch (itemId) {
                    case R.id.orgHome:
                        replaceFragment(new Org_Home());
                        break;
                    case R.id.addJob:
                        replaceFragment(new Org_Add("Add"));
                        break;
                    case R.id.orgProfile:
                        replaceFragment(new Org_Profile());
                        break;
                    default:
                        replaceFragment(new Org_Home());
                        break;


                }
                return true;
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if (currentFragment instanceof Org_Home) {
            finish();
            System.exit(0);
        } else if (currentFragment instanceof Org_Profile) {
            FrameLayout fragmentContainer = findViewById(R.id.fragment_container);
            Switch switchButton = fragmentContainer.findViewById(R.id.switchButton);
            if (switchButton.isChecked()) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new Org_Profile())
                        .commit();
            } else {
                startActivity(new Intent(getApplicationContext(), Org_Nav.class));
            }

        } else if (currentFragment instanceof IndividualJobs) {
            startActivity(new Intent(getApplicationContext(), Org_Nav.class));

        } else {
            startActivity(new Intent(getApplicationContext(), Org_Nav.class));
        }
    }


    public void updateSelectedItem(int addJob) {
        bottomNav.post(() -> {
            bottomNav.setSelectedItemId(addJob);
        });
    }
}

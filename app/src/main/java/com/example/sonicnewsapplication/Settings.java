package com.example.sonicnewsapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Settings extends AppCompatActivity {
    Switch darkMode;
    TextView profile,logOut;
    ImageView profileImage,logoutImg;
    BottomNavigationView bottomNavBar;
    private static final String DARK_MODE_STATE = "dark_mode_state";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        darkMode=findViewById(R.id.dark_mode_switch);
        if (savedInstanceState != null) {
            darkMode.setChecked(savedInstanceState.getBoolean(DARK_MODE_STATE));
        }
        else {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            boolean isDarkModeOn = sharedPreferences.getBoolean(DARK_MODE_STATE, false);
            darkMode.setChecked(isDarkModeOn);
        }

        darkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                int mode = isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
                AppCompatDelegate.setDefaultNightMode(mode);

                // Save the state of the switch to shared preferences
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Settings.this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(DARK_MODE_STATE, isChecked);
                editor.apply();
            }
        });


        profile=findViewById(R.id.profile_txt);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this,UserDetails.class));

            }
        });

        profileImage=findViewById(R.id.profile_img);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this,UserDetails.class));
            }
        });

        logOut=findViewById(R.id.Logout);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                if (account != null) {
                    mGoogleSignInClient.signOut()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    SharedPreferences.Editor editor = getSharedPreferences("myPrefs", MODE_PRIVATE).edit();
                                    editor.clear();
                                    editor.apply();

                                    // Navigate back to the login screen
                                    Intent intent = new Intent(Settings.this, Login.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                }
                else {
                    startActivity(new Intent(Settings.this, Login.class));
                    Toast.makeText(Settings.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        logoutImg=findViewById(R.id.logout_img);
        logoutImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                if (account != null) {
                    mGoogleSignInClient.signOut()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    SharedPreferences.Editor editor = getSharedPreferences("myPrefs", MODE_PRIVATE).edit();
                                    editor.clear();
                                    editor.apply();

                                    // Navigate back to the login screen
                                    Intent intent = new Intent(Settings.this, Login.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                }
                else {
                    startActivity(new Intent(Settings.this, Login.class));
                    Toast.makeText(Settings.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        bottomNavBar=findViewById(R.id.bottom_nav_bar);
        bottomNavBar.setSelectedItemId(R.id.settings);
        bottomNavBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.news:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(),Settings.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        bottomNavBar.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.news:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);

                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(),Settings.class));
                        overridePendingTransition(0,0);
                }
            }
        });
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            bottomNavBar.setBackgroundColor(getResources().getColor(R.color.turquiose));
        } else {
            bottomNavBar.setBackgroundColor(getResources().getColor(R.color.turquiose));
        }

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save the state of the switch
        outState.putBoolean(DARK_MODE_STATE, darkMode.isChecked());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        bottomNavBar.setSelectedItemId(R.id.news);
    }
}
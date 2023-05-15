package com.example.sonicnewsapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserDetails extends AppCompatActivity {

    TextView nameTextView, emailTextView, phoneTextView,passTextView,showPass,changePassword;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        // initialize TextViews
        nameTextView = findViewById(R.id.nameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        passTextView=findViewById(R.id.passTextView);

        showPass=findViewById(R.id.showPass);
        changePassword=findViewById(R.id.changePassText);

        //Set password type to text
        showPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserDetails.this, "Password is:"+passTextView.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserDetails.this,ForgotPassword.class));
            }
        });

        // get current user ID
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(UserDetails.this);
        if (account != null) {
            // update TextViews with Google account details
            nameTextView.setText(account.getDisplayName());
            emailTextView.setText(account.getEmail());
            phoneTextView.setText("Google Login Used Cant Access");
            passTextView.setText("Google Login Used Cant Access");
        } else {
            // the user has not logged in with Google account, update TextViews with Firebase login details

            // get current user ID
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String userId = user.getUid();

                // get user details from database
                databaseReference = FirebaseDatabase.getInstance().getReference().child("UserData").child(userId);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // retrieve user details
                            String name = snapshot.child("fullname").getValue().toString();
                            String email = snapshot.child("email").getValue().toString();
                            String phone = snapshot.child("phone").getValue().toString();
                            String password = snapshot.child("password").getValue().toString();

                            // update TextViews with user details
                            nameTextView.setText(name);
                            emailTextView.setText(email);
                            phoneTextView.setText(phone);
                            passTextView.setText(password);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        nameTextView.setText("Unable to get data");
                        emailTextView.setText("Unable to get data");
                        phoneTextView.setText("Unable to get data");
                        passTextView.setText("Unable to get data");
                        Log.d("UserDetails", "Failed to retrieve user details: " + error.getMessage());
                    }
                });
            }
        }
    }
}
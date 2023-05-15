package com.example.sonicnewsapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference usersRef = database.getReference("UserData");
//    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://loginregister-229c2-default-rtdb.firebaseio.com/");

    private  EditText fullName,email,phone,password,conPass;
    private Button registerBtn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mAuth=FirebaseAuth.getInstance();

        fullName=findViewById(R.id.full_name);
        email=findViewById(R.id.email);
        phone=findViewById(R.id.phone);
        password=findViewById(R.id.password);
        conPass=findViewById(R.id.conPassword);

        registerBtn=findViewById(R.id.registerBtn);
        final TextView loginNowBtn=findViewById(R.id.loginNowBtn);


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        loginNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private void registerUser(){
        String uname,uemail,uphone,upassword,uconPass;

        uemail=email.getText().toString();
        uname=fullName.getText().toString();
        uphone=phone.getText().toString();
        upassword=password.getText().toString();
        uconPass=conPass.getText().toString();

        if(TextUtils.isEmpty(uemail)){
            Toast.makeText(this,"Email field empty",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(uname)){
            Toast.makeText(this,"Full Name field empty",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(uphone)){
            Toast.makeText(this,"Phone Number field empty",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(upassword)){
            Toast.makeText(this,"Password field empty",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(uconPass)){
            Toast.makeText(this,"Confirm Password field empty",Toast.LENGTH_SHORT).show();
            return;
        }
        if(upassword.equals(uconPass)){
            mAuth.createUserWithEmailAndPassword(uemail,upassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                UserData data = new UserData(uemail, uname, upassword, uphone);

                                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                usersRef.child(userId).setValue(data)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(Register.this, "Successful Registered", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(Register.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    Toast.makeText(Register.this, "Failed to register. Please try again later.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                FirebaseDatabase.getInstance().getReference("UserData")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(data)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(Register.this, "Successful Registered", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(Register.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                            }
                            else {
                                Toast.makeText(Register.this, "Check Email id or Password", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

        }else{
            Toast.makeText(this,"Passwords don't match",Toast.LENGTH_SHORT).show();
            return;
        }
        }


}
package com.example.sonicnewsapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    private EditText edit_txt_resetEmail;
    private Button button_resetPassword;
    private ProgressBar resetPassword_progress;
    TextView loginPageButton;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        edit_txt_resetEmail = findViewById(R.id.edit_txt_resetEmail);
        button_resetPassword = findViewById(R.id.button_resetPassword);
        resetPassword_progress = findViewById(R.id.resetPassword_progress);
        loginPageButton=findViewById(R.id.loginPgBtn);
        //        Get Firebase auth instance
        firebaseAuth = FirebaseAuth.getInstance();

        loginPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPassword.this,Login.class));
                finish();
            }
        });

        button_resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword_progress.setVisibility(View.VISIBLE);
                button_resetPassword.setEnabled(false);

                firebaseAuth.sendPasswordResetEmail(edit_txt_resetEmail.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                resetPassword_progress.setVisibility(View.GONE);
                                button_resetPassword.setEnabled(true);

                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgotPassword.this, "Password reset link sent to your Email", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(ForgotPassword.this, Login.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(ForgotPassword.this, "Failed to send password reset email", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }
}
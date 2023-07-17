package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.whatsappclone.Models.User;
import com.example.whatsappclone.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private ActivitySignupBinding binding;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We're creating your account");

        binding.SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.textusernamee.getText().toString().trim();
                String email = binding.textemaill.getText().toString().trim();
                String password = binding.textpasswordd.getText().toString().trim();

                if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    progressDialog.show();

                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();
                                    if (task.isSuccessful()) {
                                        String userId = mAuth.getCurrentUser().getUid();
                                        String profilePic = ""; // Add the profile picture URL here
                                        String lastMessage = ""; // Add the last message here

                                        User users = new User(profilePic, username, email, password, userId, lastMessage, "", false);

                                        usersRef.child(userId).setValue(users, new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@NonNull DatabaseError error, @NonNull DatabaseReference ref) {
                                                if (error == null) {
                                                    Toast.makeText(SignupActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finish(); // Close the sign-up activity
                                                } else {
                                                    Toast.makeText(SignupActivity.this, "Failed to create account", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(SignupActivity.this, "Failed to create account", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(SignupActivity.this, "Enter credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.txtalreadyhaveaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
    }
}



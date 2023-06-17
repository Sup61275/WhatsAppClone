package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.FirebaseException;


public class MainActivity extends AppCompatActivity {

    EditText phoneNumber, code;
    Button button;
    String verificationId;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        userIsLoggedIn();
        phoneNumber = findViewById(R.id.phoneNumber);
        code = findViewById(R.id.code);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onClick(View v) {
                if(verificationId!=null){
                    VerifyPhoneWithCode();
                }
                else
                startVerification();
            }
        });

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This method is called when the verification is successfully completed.
                // You can use the provided PhoneAuthCredential to sign in the user or perform any necessary actions.
                // For example, you can call a method to verify the credential and sign in the user.
                VerifyPhoneWithCode();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This method is called when the verification process fails.
                // Handle the error appropriately, such as displaying an error message to the user.
                showErrorToUser(e.getMessage());
            }
            // Override other necessary methods of the PhoneAuthProvider.OnVerificationStateChangedCallbacks interface.

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationId=s;
                button.setTag("verify code");
            }
        };
    }
    private void VerifyPhoneWithCode(){
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationId,code.getText().toString());
       verifyCredentialAndSignIn( credential);

    }

    private void startVerification() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber.getText().toString(),
                60,
                TimeUnit.SECONDS,
                MainActivity.this,
                callbacks
        );
    }

    private void verifyCredentialAndSignIn(PhoneAuthCredential credential) {
        // Implement your logic to verify the credential and sign in the user.
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                    userIsLoggedIn();
            }
        });
    }

    private void userIsLoggedIn() {
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            startActivity(new Intent(getApplicationContext(),MainActivitypage.class));
            finish();
            return;
        }
    }

    private void showErrorToUser(String errorMessage) {
        // Implement your logic to show the error message to the user.
    }
}

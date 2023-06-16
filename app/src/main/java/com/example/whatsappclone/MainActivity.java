package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.FirebaseException;


public class MainActivity extends AppCompatActivity {

    EditText phoneNumber, code;
    Button button;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        phoneNumber = findViewById(R.id.phoneNumber);
        code = findViewById(R.id.code);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVerification();
            }
        });

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This method is called when the verification is successfully completed.
                // You can use the provided PhoneAuthCredential to sign in the user or perform any necessary actions.
                // For example, you can call a method to verify the credential and sign in the user.
                verifyCredentialAndSignIn(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This method is called when the verification process fails.
                // Handle the error appropriately, such as displaying an error message to the user.
                showErrorToUser(e.getMessage());
            }

            // Override other necessary methods of the PhoneAuthProvider.OnVerificationStateChangedCallbacks interface.
        };
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
    }

    private void showErrorToUser(String errorMessage) {
        // Implement your logic to show the error message to the user.
    }
}

package com.example.sportssocialapp;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class LoginActivity extends AppCompatActivity {
    private final static String TAG = "LoginActivity";

    Firebase ref;

    CoordinatorLayout coordinatorLayout;
    EditText emailInput;
    EditText passwordInput;
    Button submitButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ref = new Firebase(BuildConfig.FIREBASE_URL);
        ref.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if (authData != null) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        emailInput = (EditText) findViewById(R.id.email);
        passwordInput = (EditText) findViewById(R.id.password);
        submitButton = (Button) findViewById(R.id.submit);
    }

    public void onSubmit(View v) {
        int errorCount = 0;
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Must be a valid email");
            errorCount++;
        } else {
            emailInput.setError(null);
        }

        if (errorCount > 0)
            return;

        Firebase.AuthResultHandler authResultHandler = new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                Snackbar.make(coordinatorLayout, "Authenticated", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                Snackbar.make(coordinatorLayout, "Error: " + firebaseError.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        };

        ref.authWithPassword(email, password, authResultHandler);
    }

    public void startRegistration(View v) {
        Intent intent = new Intent(this, UserRegistrationActivity.class);
        startActivity(intent);
    }

}

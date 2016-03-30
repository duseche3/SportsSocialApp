package com.example.sportssocialapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.text.DateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserRegistrationActivity extends Activity {
    private final static String TAG = "UserRegistrationActivity";

    Firebase ref;

    EditText dobText;
    DatePickerDialog dobDialog;
    DateFormat dateFormat;
    Button submit;

    public UserRegistrationActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        ActionBar actionBar = getActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);


        dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        dobText = (EditText) findViewById(R.id.userDob);
        submit = (Button) findViewById(R.id.submitBtn);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    // create account
                }
            }
        });

        Calendar calendar = Calendar.getInstance();
        dobDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dobText.setText(dateFormat.format(newDate.getTime()));
            }

        },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        dobText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dobDialog.show();
                return false;
            }
        });


        ref = new Firebase(BuildConfig.FIREBASE_URL);
        ref.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if (authData != null) {
                    Intent intent = new Intent(UserRegistrationActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    private boolean validate(){
        Boolean missing = false;
        Boolean emailInvalid = false;
        Boolean passwordInvalid = false;

        TextView nameErr = (TextView) findViewById(R.id.nameError);
        TextView emailErr = (TextView) findViewById(R.id.emailError);
        TextView passErr = (TextView) findViewById(R.id.passError);
        TextView confirmErr = (TextView) findViewById(R.id.confirmError);
        TextView dobErr = (TextView) findViewById(R.id.dobError);
        TextView genderErr = (TextView) findViewById(R.id.genderError);
        TextView errorMsg = (TextView) findViewById(R.id.errorMessage);

        nameErr.setVisibility(View.INVISIBLE);
        emailErr.setVisibility(View.INVISIBLE);
        passErr.setVisibility(View.INVISIBLE);
        confirmErr.setVisibility(View.INVISIBLE);
        dobErr.setVisibility(View.INVISIBLE);
        genderErr.setVisibility(View.INVISIBLE);
        errorMsg.setText("");


        // name error checking
        String name = ((EditText) findViewById(R.id.userName)).getText().toString().trim().toLowerCase();
        if(name.equals("")){
            missing = true;
            nameErr.setVisibility(View.VISIBLE);
        }
        else
            nameErr.setVisibility(View.INVISIBLE);

        // email error checking
        String email = ((EditText) findViewById(R.id.userEmail)).getText().toString().trim().toLowerCase();
        if(!email.equals("")) {
            String regex = "^(.+)@(.+)$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(email);
            if (!matcher.matches()) {
                emailErr.setVisibility(View.VISIBLE);
                emailInvalid = true;
            }
            else{
                emailErr.setVisibility(View.INVISIBLE);
            }
        }
        else{
            emailErr.setVisibility(View.VISIBLE);
            missing = true;
        }

        // password error checking
        String pass1 = ((EditText) findViewById(R.id.userPassword)).getText().toString().trim();
        String pass2 = ((EditText) findViewById(R.id.userConfirmPassword)).getText().toString().trim();

        if(pass1.equals("")){
            missing = true;
            passErr.setVisibility(View.VISIBLE);
        }

        if(pass2.equals("")){
            missing = true;
            confirmErr.setVisibility(View.VISIBLE);
        }

        if(!(pass1.equals(pass2))){
            passwordInvalid = true;
            confirmErr.setVisibility(View.VISIBLE);
        }


        // date of birth error checking
        String dob = ((EditText) findViewById(R.id.userDob)).getText().toString().trim();
        if(dob.equals("")){
            missing = true;
            dobErr.setVisibility(View.VISIBLE);
        }

        // gender error checking
        RadioGroup gender = (RadioGroup) findViewById(R.id.userGender);
        if(gender.getCheckedRadioButtonId() == -1){
            missing = true;
            genderErr.setVisibility(View.VISIBLE);
        }

        if(missing){
            errorMsg.setText("Missing fields!");
            return false;
        }
        else if(emailInvalid){
            errorMsg.setText("Invalid email entered!");
            return false;
        }
        else if(passwordInvalid){
            errorMsg.setText("Passwords do not match!");
            return false;
        }
        else
            return true;
    }
}

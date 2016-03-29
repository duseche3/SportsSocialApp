package com.example.sportssocialapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.text.DateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserRegistration extends Activity {

    EditText dobText;
    DatePickerDialog dobDialog;
    DateFormat dateFormat;
    Button submit;

    public UserRegistration() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_registration);

        dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        dobText = (EditText) findViewById(R.id.userDob);
        submit = (Button) findViewById(R.id.submitBtn);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
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

    }

    private boolean validate(){

        String email = ((EditText) findViewById(R.id.userEmail)).getText().toString().trim().toLowerCase();
        String regex = "^(.+)@(.+)$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if(!matcher.matches()) {
            Log.i("Email","invalid");
            return false;
        }

        EditText pass1 = (EditText) findViewById(R.id.userPassword);
        EditText pass2 = (EditText) findViewById(R.id.userConfirmPassword);

        if(!(pass1.getText().toString().trim().equals(pass2.getText().toString().trim())
                && !pass1.getText().toString().trim().equals(""))){
            Log.i("Email","not equals");

        }
        else
            Log.i("Email","equals");

        RadioGroup gender = (RadioGroup) findViewById(R.id.userGender);
        if(gender.getCheckedRadioButtonId() == -1){
            Log.i("Gender","none");
            return false;
        }

        return true;
    }
}

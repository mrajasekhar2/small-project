package com.adhishta.syam.brightfuture.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.adhishta.syam.brightfuture.Dto.RegisterDto;
import com.adhishta.syam.brightfuture.MainActivity;
import com.adhishta.syam.brightfuture.R;
import com.adhishta.syam.brightfuture.Utils.GCMregistration;
import com.adhishta.syam.brightfuture.Utils.Utils;

import java.util.Calendar;

/**
 * Created by admin on 05/03/2018.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv_signup, tv_terms,tv_dob;
    EditText et_firstname, et_lastname, et_username, et_email, et_mobile, et_pass, et_passconf;
    RadioGroup rdgrp_gender;
    Context mContext;
    private int year, month, day;
    private Calendar calendar;

    RadioButton selectedgender;
    String gender, DOB;
    RegisterDto dto;
    int is_fromlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        mContext = this;

        dto = new RegisterDto();

        mInitialization();

        tv_signup.setOnClickListener(this);
        tv_dob.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_signup) {
            mRegisterService();
        } else if (view.getId() == R.id.tv_dob) {
            showDialog(999);
        }

    }


    private void mInitialization() {

        tv_signup = findViewById(R.id.tv_signup);
        tv_dob = findViewById(R.id.tv_dob);

        et_firstname = findViewById(R.id.et_firstname);
        et_lastname = findViewById(R.id.et_lastname);
        et_username = findViewById(R.id.et_username);
        et_email = findViewById(R.id.et_email);
        et_mobile = findViewById(R.id.et_mobile);
        et_pass = findViewById(R.id.et_pass);
        et_passconf = findViewById(R.id.et_passconf);

        rdgrp_gender = findViewById(R.id.rdgrp_gender);

        tv_terms = findViewById(R.id.tv_terms);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

    }

    private void mRegisterService() {

        int selectedId = rdgrp_gender.getCheckedRadioButtonId();
        selectedgender = (RadioButton) findViewById(selectedId);
        if (selectedgender.getText().equals("Male")) {
            gender = "M";
        } else {
            gender = "F";
        }

        if (et_firstname.getText().toString().trim().isEmpty()) {
            Utils.mCustomToast(mContext, "please enter first name..", 1);
        } else if (et_lastname.getText().toString().trim().isEmpty()) {
            Utils.mCustomToast(mContext, "please enter last name..", 1);
        } else if (et_username.getText().toString().trim().isEmpty()) {
            Utils.mCustomToast(mContext, "please enter user name..", 1);
        } else if (et_email.getText().toString().trim().isEmpty()) {
            Utils.mCustomToast(mContext, "please enter valid email id..", 1);
        } else if (et_mobile.getText().toString().trim().isEmpty()) {
            Utils.mCustomToast(mContext, "please enter mobile number..", 1);
        } else if (et_mobile.getText().toString().trim().length() != 10) {
            Utils.mCustomToast(mContext, "please enter valid 10 digit mobile number..", 1);
        } else if (tv_dob.getText().toString().trim().isEmpty()) {
            Utils.mCustomToast(mContext, "please select date of birth..", 1);
        } else if (selectedId == 0) {
            Utils.mCustomToast(mContext, "please select gender..", 1);
        } else if (et_pass.getText().toString().trim().isEmpty()) {
            Utils.mCustomToast(mContext, "please enter password..", 1);
        } else if (et_passconf.getText().toString().trim().isEmpty()) {
            Utils.mCustomToast(mContext, "please enter confirm password..", 1);
        } else if (!et_pass.getText().toString().trim().equals(et_passconf.getText().toString().trim())) {
            Utils.mCustomToast(mContext, "password and confirm password must be same", 1);
        } else {

            dto.setFirstname(et_firstname.getText().toString().trim());
            dto.setLsatname(et_lastname.getText().toString().trim());
            dto.setUsername(et_username.getText().toString().trim());
            dto.setEmail(et_email.getText().toString().trim());
            dto.setMobileNumber(et_mobile.getText().toString().trim());
            dto.setDOB(DOB);
            dto.setGender(gender);
            dto.setPassword(et_pass.getText().toString().trim());

            is_fromlogin = 1;

            GCMregistration gcm = new GCMregistration();
            gcm.setFrom_login(false);
            gcm.initial(this, dto, this, is_fromlogin);

        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override

                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2 + 1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        tv_dob.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
        DOB = year + "-" + month + "-" + day;
    }

}

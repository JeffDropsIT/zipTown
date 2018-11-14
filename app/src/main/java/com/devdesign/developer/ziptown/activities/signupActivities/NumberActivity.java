package com.devdesign.developer.ziptown.activities.signupActivities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.devdesign.developer.ziptown.R;
import com.devdesign.developer.ziptown.activities.LandingPageActivity;
import com.devdesign.developer.ziptown.activities.LoginActivity;
import com.devdesign.developer.ziptown.activities.NetworkIssuesActivity;
import com.devdesign.developer.ziptown.connection.ServerRequest;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.rilixtech.CountryCodePicker;

import java.util.HashMap;
import java.util.Map;

public class NumberActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "WSX";
    private CountryCodePicker ccp;
    private EditText edtContact;
    private FloatingActionButton fabNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.number_layout);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ccp =  findViewById(R.id.ccp);

        edtContact = findViewById(R.id.edt_contact);
        fabNext = findViewById(R.id.fab_next);
        fabNext.setOnClickListener(this);
        setDefaultCountry();
    }

    private void setDefaultCountry(){
        ccp.setDefaultCountryUsingNameCode(getCurrentCountry());
    }
    private String getCurrentCountry(){
        String locale = this.getResources().getConfiguration().locale.getCountry();
        Log.i("WSX", "getCurrentCountry: code: "+locale);
        return locale;
    }

    private void validateContact() {
        String contact = edtContact.getText().toString().replace(" ", "");
        if(contact.isEmpty()){
            edtContact.setError("number required");
        }else {
            validateNumber(contact);
        }
    }

    private void validateNumber(String contact){
        String number;
        number = ccp.getSelectedCountryCode();
        number += checkNumber(contact);
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber numberProto = null;
        try {
            numberProto = phoneUtil.parse(contact, ccp.getDefaultCountryNameCode());
        } catch (NumberParseException e) {
            Log.i(TAG, "validateContact error: "+e.getMessage());
        }
        if(numberProto != null){
            if(phoneUtil.isValidNumber(numberProto)){

                Toast.makeText(this, number, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, VerifyNumberActivity.class);
                intent.putExtra("number", number);
                startActivity(intent);
            }else {
                edtContact.setError("invalid number");
            }
        }else {
            edtContact.setError("invalid number");
        }
    }
    private String checkNumber(String string){

        if('0' == string.charAt(0)){
            return string.substring(1);
        }
        return string;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_next:
                validateContact();
                break;
        }
    }



}

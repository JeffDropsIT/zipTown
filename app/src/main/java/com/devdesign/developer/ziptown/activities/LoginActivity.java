package com.devdesign.developer.ziptown.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.devdesign.developer.ziptown.models.forms.UserLogin;
import com.devdesign.developer.ziptown.models.responses.GenericErrorResponse;
import com.devdesign.developer.ziptown.models.responses.UserSignInAndLoginResponse;
import com.devdesign.developer.ziptown.R;
import com.devdesign.developer.ziptown.connection.ServerRequest;
import com.google.i18n.phonenumbers.AsYouTypeFormatter;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.rilixtech.CountryCodePicker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.devdesign.developer.ziptown.activities.LandingPageActivity.isNetworkAvailable;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ServerRequest.OnTaskCompleted {
    public static final String TAG = "WSX";
    private FloatingActionButton fabLogin;
    private EditText edtPassword, edtContact;
    public static ProgressDialog progressDialog;
    private CountryCodePicker ccp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_sign_in_layout);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        if (!isNetworkAvailable() ) {
            startErrorActivity();
        }
        ccp =  findViewById(R.id.ccp);
        setDefaultCountry();
        fabLogin = findViewById(R.id.fab_next);
        edtContact = findViewById(R.id.edt_contact);
        edtPassword = findViewById(R.id.edt_password);
        fabLogin.setOnClickListener(this);
        setToolBar();

    }

    private void setDefaultCountry(){
        ccp.setDefaultCountryUsingNameCode(getCurrentCountry());
    }
    private String getCurrentCountry(){
        String locale = this.getResources().getConfiguration().locale.getCountry();
        Log.i("WSX", "getCurrentCountry: code: "+locale);
        return locale;
    }
    private String checkNumber(String string){

        if('0' == string.charAt(0)){
            return string.substring(1);
        }
        return string;
    }

    private void startErrorActivity() {
        Intent intent = new Intent(this, NetworkIssuesActivity.class);
        startActivity(intent);
    }

    public static void showProgress(String title, String msg, Context context) {
        if(progressDialog == null){
            View parent = LayoutInflater.from(context).inflate(R.layout.progress_bar_layout, null, false);
            TextView ttvTitle = parent.findViewById(R.id.ttv_title), ttvMsg = parent.findViewById(R.id.ttv_msg);
            ttvTitle.setText(title);
            ttvMsg.setText(msg);
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            progressDialog.setContentView(parent);

        }else {
            dismissProgress();
            showProgress(title, msg, context);
        }
    }

    @Override
    protected void onPause() {
        dismissProgress();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        dismissProgress();
        super.onDestroy();
    }

    public static void dismissProgress(){
        if(progressDialog != null){
            progressDialog.dismiss();
            progressDialog  = null;
        }
    }
    private void validateNumber(String contact, String password){
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
                UserLogin userLogin = new UserLogin(number, password);
                Map<String, Object> map = new HashMap<>();
                map.put("type", new String("UserLogin"));
                map.put("model", userLogin);

                if(isNetworkAvailable()){
                    showProgress("Sign In", "Signing In User", LoginActivity.this);
                    new ServerRequest(this, getApplicationContext()).execute(map);
                }else {
                    Intent intent = new Intent(this, NetworkIssuesActivity.class);
                    startActivity(intent);
                }


                Toast.makeText(this, "Logging in", Toast.LENGTH_SHORT).show();
            }else {
                edtContact.setError("invalid number");
            }
        }else {
            edtContact.setError("invalid number");
        }
    }
    private void verifyPasswordAndContact(){
        String password = edtPassword.getText().toString();
        String contact = edtContact.getText().toString();
        if(password.isEmpty()){
            edtPassword.setError("Password required");
        }else if(contact.isEmpty()){
            edtContact.setError("Contact number required");
        }else {
            //call api to verify password
            validateNumber(contact, password);
        }

    }
    private void goToMyProfile(UserSignInAndLoginResponse user){
        Intent intent = new Intent(this, CurrentUserActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
    private void setToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar_sign_up);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        //actionbar.setTitle("Log in");
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setHomeButtonEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
    private void showErrorIncorrectCreds(){
        TextView error = findViewById(R.id.ttv_error);
        error.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_next:
                verifyPasswordAndContact();
                break;
        }
    }

    @Override
    public void onTaskCompleted() {
        Log.i("WSX", "onTaskCompleted: logged in");
        dismissProgress();
    }

    @Override
    public void onDataFetched(Map<String, Object> object) {

        if(object.get("response").toString().contains("error")){
            final GenericErrorResponse error = (GenericErrorResponse)object.get("object");
            Log.i("WSX", "onDataFetched: error "+error.toString());
            LoginActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView textView = findViewById(R.id.ttv_error);
                    textView.setVisibility(View.VISIBLE);
                }
            });

        }else {
            UserSignInAndLoginResponse res = (UserSignInAndLoginResponse) object.get("object");
            Log.i("WSX", "onDataFetched: res: "+res+" resType: "+object.get("response"));
            goToMyProfile(res);
            Log.i("WSX", "onDataFetched: success "+object.get("response")+" user "+res.getUser().getFullName());
        }
    }


    @Override
    public void onTaskFailed() {
        Intent intent = new Intent(this, NetworkIssuesActivity.class);
        //startActivity(intent);
    }
}

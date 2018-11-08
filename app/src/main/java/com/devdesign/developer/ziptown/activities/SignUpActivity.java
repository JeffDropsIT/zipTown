package com.devdesign.developer.ziptown.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.devdesign.developer.ziptown.adapters.PlaceAutocompleteAdapter;
import com.devdesign.developer.ziptown.connection.ServerRequest;
import com.devdesign.developer.ziptown.models.forms.CreateUser;
import com.devdesign.developer.ziptown.models.responses.ContactVerificationSuccessResponse;
import com.devdesign.developer.ziptown.models.responses.GenericErrorResponse;
import com.devdesign.developer.ziptown.models.responses.UserSignInAndLoginResponse;
import com.devdesign.developer.ziptown.R;
import com.devdesign.developer.ziptown.fragments.VerificationCodeFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.rilixtech.CountryCodePicker;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, ServerRequest.OnTaskCompleted, VerificationCodeFragment.DialogCompleteListener {
    private static final int MY_PERMISSIONS_REQUEST_SMS = 121;
    private AutoCompleteTextView mSearchCity;
    private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;

    String selectedItem;
    private Button btnSubmit, btnVerify;
    private EditText edtPassword, edtPasswordConfirm;
    private EditText edtContact, edtUsername;
    private CountryCodePicker ccp;
    private String code;
    String msgBody;
    VerificationCodeFragment verificationCodeFragment;
    boolean verified = false;
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("android.provider.Telephony.SMS_RECEIVED")) {
                //action for sms received
                Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
                SmsMessage[] msgs = null;
                String msg_from;
                if (bundle != null) {
                    //---retrieve the SMS message received---
                    try {
                        Object[] pdus = (Object[]) bundle.get("pdus");
                        msgs = new SmsMessage[pdus.length];
                        for (int i = 0; i < msgs.length; i++) {
                            msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                            msgBody = msgs[i].getMessageBody();
                            if(code != null){
                                if(msgBody.contains(code)){
                                    verificationCodeFragment.close();
                                    btnVerify.setEnabled(false);
                                    verified = true;
                                    Log.i("WSX", "onReceive: received on main: bool: " + msgBody.contains(code)+" message: "+msgBody+" code: "+code);
                                    Toast.makeText(context, "Verified", Toast.LENGTH_SHORT).show();
                                }else {
                                    btnVerify.setEnabled(true);
                                    verified = false;
                                    Log.i("WSX", "onReceive: received on main: bool: " + msgBody.contains(code)+" message: "+msgBody+" code: "+code);
                                    Toast.makeText(context, "incorrect OTP entered", Toast.LENGTH_SHORT).show();
                                }
                                Log.i("WSX", "onReceive: received on main: bool: " + msgBody.contains(code)+" message: "+msgBody+" code: "+code);
                            }else {
                                Log.i("WSX", "onReceive: received no code: " + msgBody.contains(code)+" message: "+msgBody+" code: "+code);
                            }

                        }
                    } catch (Exception e) {
                        Log.d("Exception caught",e.getMessage());
                    }

                }
            }
        }
    };
    private GenericDialog dialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        if (!LandingPageActivity.isNetworkAvailable() ) {
            startErrorActivity();
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.addAction(android.telephony.TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        registerReceiver(receiver, filter);
        edtPasswordConfirm = findViewById(R.id.edt_password_confirm);
        edtUsername = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_password);
        edtContact = findViewById(R.id.edt_contact);
        btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(this);
        btnVerify = findViewById(R.id.btn_verify);
        btnVerify.setOnClickListener(this);
        ccp =  findViewById(R.id.ccp);
        setDefaultCountry();
        getCitySuggestions();
        setToolBar();
        setSpinner();
    }

    private void startErrorActivity() {
        Intent intent = new Intent(this, NetworkIssuesActivity.class);
        startActivity(intent);
    }

    private String getCurrentCountry(){
        String locale = this.getResources().getConfiguration().locale.getCountry();
        Log.i("WSX", "getCurrentCountry: code: "+locale);
        return locale;
    }
    private void setDefaultCountry(){
        ccp.setDefaultCountryUsingNameCode(getCurrentCountry());
    }
    private void setSpinner(){
        Spinner spinner = findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.states_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }


    private void getCitySuggestions(){
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        placeAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient,
                AddGenericPostActivity.LAT_LNG_BOUNDS, null);
        mSearchCity = findViewById(R.id.edt_city);
        mSearchCity.setAdapter(placeAutocompleteAdapter);
    }
    private void setToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("Sign Up");
        actionbar.setDisplayHomeAsUpEnabled(true);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedItem = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_submit:
                collectAndVerify();
                break;
            case R.id.btn_verify:
                //validateContact();
                if(edtContact.getText().toString().replace(" ", "").isEmpty()){
                    edtContact.setError("number required");
                }else {
                    checkForPhonePermission();
                }
                break;
        }
    }

    private void validateContact() {
        String contact = edtContact.getText().toString().replace(" ", "");
        String number;
        if(contact.isEmpty()){
            edtContact.setError("number required");
        }else {

            btnVerify.setEnabled(false);
            number = ccp.getSelectedCountryCode();
            number += contact;
            Map<String, Object> map = new HashMap<>();
            map.put("contact", number);
            map.put("type", "VerifyContact");
            if(LandingPageActivity.isNetworkAvailable()){
                LoginActivity.showProgress("Verification", "Verifying", SignUpActivity.this);
                new ServerRequest(this).execute(map);
            }else {
                Intent intent = new Intent(this, NetworkIssuesActivity.class);
                startActivity(intent);
            }
            Log.i("WSX", "validateContact: contact: "+number);
        }
    }

    private void validateInputs(){
        String password = edtPassword.getText().toString().trim();
        String passwordConfirm = edtPasswordConfirm.getText().toString().trim();
        String contact = edtContact.getText().toString().replace(" ", "");
        String name = edtUsername.getText().toString().trim();
        String city = mSearchCity.getText().toString().trim();
        if(password.isEmpty()){
            edtPassword.setError("Password required");

        }else if(passwordConfirm.isEmpty()){
            edtPasswordConfirm.setError("Password required");

        }else if(name.isEmpty()){
            edtUsername.setError("Name required");

        }else if(contact.isEmpty()) {
            edtContact.setError("Contact number required");

        }else if(city.isEmpty()) {
            mSearchCity.setError("city required");

        }else {
            if(checkPasswordMatch(password, passwordConfirm)){
                CreateUser createUser = new CreateUser(password, name, selectedItem, city, ccp.getSelectedCountryCode()+contact);
                Map<String, Object> map = new HashMap<>();
                map.put("type", new String("CreateUser"));
                map.put("model", createUser);


                if(verified){
                    if(LandingPageActivity.isNetworkAvailable()){
                        LoginActivity.showProgress("Sign up", "Signing up User", SignUpActivity.this);
                        new ServerRequest(this).execute(map);
                    }else {
                        Intent intent = new Intent(this, NetworkIssuesActivity.class);
                        startActivity(intent);
                    }
                }else {
                    Toast.makeText(this, "Please Verify number", Toast.LENGTH_SHORT).show();
                }
                //submit to api


                //su
            }else{
                //throw error pass
                edtPassword.setError("Password doesn't match");
                edtPasswordConfirm.setError("Password doesn't match");
            }

        }

    }

    private void goHomeActivity(){


        if(verified){
            Intent intent = new Intent(this, CurrentUserActivity.class);
            startActivity(intent);
            finish();
        }else {
            if(verificationCodeFragment == null){
                verificationCodeFragment = new VerificationCodeFragment();
                verificationCodeFragment.setContextListener(getApplicationContext(), SignUpActivity.this);
                verificationCodeFragment.show(getFragmentManager(), "verificationCodeFragment");
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SMS: {
                if (permissions[0].equalsIgnoreCase
                        (Manifest.permission.RECEIVE_SMS)
                        && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted.
                    Log.d("WSX", "grant granted sms");
                    validateContact();
                    Toast.makeText(this, "sms permission granted", Toast.LENGTH_SHORT).show();

                } else {
                    // Permission denied. Stop the app.
                    Log.d("WSX", "failed to get permission");
                    Toast.makeText(this, "sms denied", Toast.LENGTH_SHORT).show();
                    // Disable the call button

                }
            }
        }
    }
    private void checkForPhonePermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS) !=
                PackageManager.PERMISSION_GRANTED) {
            // Permission not yet granted. Use requestPermissions().
            Log.d("WSX", "grant permission");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECEIVE_SMS},
                    MY_PERMISSIONS_REQUEST_SMS);
        }else {
            validateContact();
        }
    }
    private boolean checkPasswordMatch(String password, String passwordConfirm) {
        return password.equals(passwordConfirm);
    }

    private void collectAndVerify() {
        validateInputs();
    }

    @Override
    public void onTaskCompleted() {
        Log.i("WSX", "onTaskCompleted: created user");
        LoginActivity.dismissProgress();
        //goHomeActivity();
    }

    @Override
    public void onDataFetched(final Map<String, Object> object) {
        if(object.get("response").equals("code")){
            final ContactVerificationSuccessResponse codeObj = (ContactVerificationSuccessResponse)object.get("object");

            this.runOnUiThread(new Runnable() {
                public void run() {
                    code = codeObj.getCode();
                    verificationCodeFragment = new VerificationCodeFragment();
                    verificationCodeFragment.setContextListener(getApplicationContext(), SignUpActivity.this);
                    verificationCodeFragment.show(getFragmentManager(), "verificationCodeFragment");
                    Toast.makeText(getApplicationContext(), "Code: "+codeObj.getCode(), Toast.LENGTH_SHORT).show();

                }
            });
        }else if(object.get("response").equals("user")){

            UserSignInAndLoginResponse res = (UserSignInAndLoginResponse) object.get("object");
            Log.i("WSX", "onDataFetched: res: "+res+" resType: "+object.get("response"));
            goHomeActivity();
            Log.i("WSX", "onDataFetched: success "+object.get("response")+" user "+res.getUser().getFullName());

        }else {
            this.runOnUiThread(new Runnable() {
                public void run() {

                    GenericErrorResponse res = (GenericErrorResponse) object.get("object");
                    Log.i("WSX", "onDataFetched: res: "+res.getError()+" resType: "+object.get("response"));
                    dialogFragment = new GenericDialog();
                    dialogFragment.setActivity(SignUpActivity.this);
                    dialogFragment.show(getFragmentManager(), "SignUpDialog");
                    dialogFragment.setError(res.getError());
                    Toast.makeText(getApplicationContext(), ""+res.getError(), Toast.LENGTH_SHORT).show();

                }
            });
        }

    }

    @Override
    protected void onPause() {
        LoginActivity.dismissProgress();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        LoginActivity.dismissProgress();
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public void onTaskFailed() {

        Intent intent = new Intent(this, NetworkIssuesActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCodeReceived(final String pcode) {

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(code != null){
                    if(pcode.contains(code)){
                        verificationCodeFragment.close();
                        btnVerify.setEnabled(false);
                        verified = true;
                        Toast.makeText(getApplicationContext(), "Verified", Toast.LENGTH_SHORT).show();
                    }else {
                        btnVerify.setEnabled(true);
                        verified = false;
                        Toast.makeText(getApplicationContext(), "incorrect OTP entered", Toast.LENGTH_SHORT).show();
                    }
                    Log.i("WSX", "onReceive: received on main: bool: " + pcode.contains(code)+" message: "+msgBody+" code: "+code);
                }else {
                    Log.i("WSX", "onReceive: received no code: " + pcode.contains(code)+" message: "+msgBody+" code: "+code);
                }
            }
        });
    }
}

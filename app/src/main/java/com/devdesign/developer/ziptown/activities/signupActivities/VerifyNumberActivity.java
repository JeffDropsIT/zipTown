package com.devdesign.developer.ziptown.activities.signupActivities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devdesign.developer.ziptown.R;
import com.devdesign.developer.ziptown.activities.GenericDialog;
import com.devdesign.developer.ziptown.activities.LandingPageActivity;
import com.devdesign.developer.ziptown.activities.LoginActivity;
import com.devdesign.developer.ziptown.activities.NetworkIssuesActivity;
import com.devdesign.developer.ziptown.connection.ServerRequest;
import com.devdesign.developer.ziptown.fragments.VerificationCodeFragment;
import com.devdesign.developer.ziptown.models.responses.ContactVerificationSuccessResponse;
import com.devdesign.developer.ziptown.models.responses.GenericErrorResponse;
import com.devdesign.developer.ziptown.models.responses.UserSignInAndLoginResponse;

import java.util.HashMap;
import java.util.Map;

public class VerifyNumberActivity extends AppCompatActivity implements View.OnClickListener, ServerRequest.OnTaskCompleted {
    private static final int MY_PERMISSIONS_REQUEST_SMS = 121;
    public static final String TAG = "WSX";
    private String code, userCode;
    String msgBody;
    boolean verified = false;
    private FloatingActionButton fabNext;
    private TextView ttvDisplayNumber;
    EditText edtDigitOne, edtDigitTwo, edtDigitThree, edtDigitFour;
    private GenericDialog dialogFragment;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification_layout);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setReceiver();
        ttvDisplayNumber = findViewById(R.id.ttv_number_display);
        linearLayout = findViewById(R.id.lin_display_msg);
        checkForPhonePermission();




        fabNext = findViewById(R.id.fab_next);
        edtDigitOne = findViewById(R.id.edt_first_digit);
        edtDigitTwo = findViewById(R.id.edt_second_digit);
        edtDigitThree = findViewById(R.id.edt_third_digit);
        edtDigitFour = findViewById(R.id.edt_forth_digit);


        fabNext.setOnClickListener(this);
        edtDigitOne.setOnClickListener(this);

        edtDigitOne.requestFocus();




    }


    private void formatCode(){
        edtDigitOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0)
                    return;
                if(s.length() >= 1 ){
                    //edtDigitOne.setText(String.valueOf(s.charAt(0)));
                    edtDigitTwo.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtDigitTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0)
                    return;
                if(s.length() >= 1 ){
                    //edtDigitTwo.setText(String.valueOf(s.charAt(1)));
                    edtDigitThree.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtDigitThree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0)
                    return;
                if(s.length() >= 1 ){
                    //edtDigitThree.setText(String.valueOf(s.charAt(2)));
                    edtDigitFour.requestFocus();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtDigitFour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0)
                    return;
                if(s.length() >= 1 ){
                    //edtDigitFour.setText(String.valueOf(s.charAt(3)));
                    fabNext.setVisibility(View.VISIBLE);
                    fabNext.setEnabled(true);
                    getUserCode();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    public void getUserCode() {
        userCode = getText(edtDigitOne) + getText(edtDigitTwo) + getText(edtDigitThree) + getText(edtDigitFour);
        if(code != null){
            if(userCode.contains(code)){
                goNext();
            }else {
                //show incorrect code dialog
                fabNext.setEnabled(false);
                fabNext.setVisibility(View.INVISIBLE);
            }
        }
    }
    private void goNext(){
        Intent intent = new Intent(this, FullNameActivity.class);
        startActivity(intent);
    }
    private void setTextCode(boolean isCorrect){

        if(code == null){
            return;
        }
        Log.i(TAG, "setTextCode: we are here");
        fabNext.setVisibility(View.VISIBLE);
        fabNext.setEnabled(false);
        if(isCorrect){
            Log.i(TAG, "setTextCode: we are here");
            edtDigitOne.setText(String.valueOf(code.charAt(0)));
            edtDigitTwo.setText(String.valueOf(code.charAt(1)));
            edtDigitThree.setText(String.valueOf(code.charAt(2)));
            edtDigitFour.setText(String.valueOf(code.charAt(3)));
            goNext();
        }

    }
    private String getText(EditText editText){
        return editText.getText().toString().trim();
    }

    private void getVerificationCode(){
        if(getIntent() == null)
            return;
        linearLayout.setVisibility(View.VISIBLE);
        String number = getIntent().getStringExtra("number");
        String fNumber = "+"+number.substring(0, 4)+" "+number.substring(4, 7)+" "+number.substring(7);
        ttvDisplayNumber.setText(fNumber);
        Map<String, Object> map = new HashMap<>();
        map.put("contact", number);
        map.put("type", "VerifyContact");
        if(LandingPageActivity.isNetworkAvailable()){
                LoginActivity.showProgress("Verification", "Verifying", VerifyNumberActivity.this);
                new ServerRequest(this).execute(map);
        }else {
            Intent intent = new Intent(this, NetworkIssuesActivity.class);
            startActivity(intent);
        }
    }
    private void setReceiver(){
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.addAction(android.telephony.TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        registerReceiver(receiver, filter);
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
            getVerificationCode();
        }
    }
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("android.provider.Telephony.SMS_RECEIVED")) {
                //action for sms received
                Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
                SmsMessage[] msgs = null;
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
                                    //btnVerify.setEnabled(false);

                                    setTextCode(true);
                                    Log.i("WSX", "onReceive: received on main: bool: " + msgBody.contains(code)+" message: "+msgBody+" code: "+code);
                                    Toast.makeText(context, "Verified", Toast.LENGTH_SHORT).show();
                                }else {
                                    //btnVerify.setEnabled(true);
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
    @Override
    protected void onDestroy() {
        LoginActivity.dismissProgress();
        unregisterReceiver(receiver);
        super.onDestroy();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_next:
                getUserCode();
                break;
            case R.id.edt_first_digit:
                formatCode();
                break;
        }
    }

    @Override
    public void onTaskCompleted() {
        LoginActivity.dismissProgress();

    }

    @Override
    public void onDataFetched(final Map<String, Object> object) {
        if(object.get("response").equals("code")){
            final ContactVerificationSuccessResponse codeObj = (ContactVerificationSuccessResponse)object.get("object");

            this.runOnUiThread(new Runnable() {
                public void run() {
                    code = codeObj.getCode();
                    Toast.makeText(getApplicationContext(), "debug code: "+codeObj.getCode(), Toast.LENGTH_SHORT).show();

                }
            });
        }else {
            this.runOnUiThread(new Runnable() {
                public void run() {

                    GenericErrorResponse res = (GenericErrorResponse) object.get("object");
                    Log.i("WSX", "onDataFetched: res: "+res.getError()+" resType: "+object.get("response"));
                    dialogFragment = new GenericDialog();
                    dialogFragment.setActivity(VerifyNumberActivity.this);
                    dialogFragment.show(getFragmentManager(), "SignUpDialog");
                    dialogFragment.setError(res.getError());
                    Toast.makeText(getApplicationContext(), ""+res.getError(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    @Override
    public void onTaskFailed() {

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
                    getVerificationCode();
                    Toast.makeText(this, "sms permission granted", Toast.LENGTH_SHORT).show();

                } else {
                    // Permission denied. Stop the app.
                    getVerificationCode();
                    Log.d("WSX", "failed to get permission");
                    Toast.makeText(this, "sms denied", Toast.LENGTH_SHORT).show();
                    // Disable the call button

                }
            }
        }
    }
}

package com.devdesign.developer.ziptown.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.devdesign.developer.ziptown.models.forms.UserLogin;
import com.devdesign.developer.ziptown.models.responses.GenericErrorResponse;
import com.devdesign.developer.ziptown.models.responses.UserSignInAndLoginResponse;
import com.devdesign.developer.ziptown.R;
import com.devdesign.developer.ziptown.connection.ServerRequest;

import java.util.HashMap;
import java.util.Map;

import static com.devdesign.developer.ziptown.activities.LandingPageActivity.isNetworkAvailable;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ServerRequest.OnTaskCompleted {
    private Button btnLogin;
    private EditText edtPassword, edtContact;
    public static ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        if (!isNetworkAvailable() ) {
            startErrorActivity();
        }

        btnLogin = findViewById(R.id.btn_login);
        edtContact = findViewById(R.id.edt_contact);
        edtPassword = findViewById(R.id.edt_password);
        btnLogin.setOnClickListener(this);
        setToolBar();

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
    private void verifyPasswordAndContact(){
        String password = edtPassword.getText().toString();
        String contact = edtContact.getText().toString();
        if(password.isEmpty()){
            edtPassword.setError("Password required");
        }else if(contact.isEmpty()){
            edtContact.setError("Contact number required");
        }else {
            //call api to verify password
            UserLogin userLogin = new UserLogin(contact, password);
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
        Toolbar toolbar = findViewById(R.id.toolbar_container);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("Sign In");
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
    private void showErrorIncorrectCreds(){
        TextView error = findViewById(R.id.ttv_error);
        error.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
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

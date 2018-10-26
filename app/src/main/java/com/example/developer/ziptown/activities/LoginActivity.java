package com.example.developer.ziptown.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.developer.ziptown.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnLogin;
    private EditText edtPassword, edtContact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btn_login);
        edtContact = findViewById(R.id.edt_contact);
        edtPassword = findViewById(R.id.edt_password);
        btnLogin.setOnClickListener(this);
        setToolBar();

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
            goToMyProfile();
            Toast.makeText(this, "Logging in", Toast.LENGTH_SHORT).show();
        }

    }
    private void goToMyProfile(){
        Intent intent = new Intent(this, CurrentUserActivity.class);
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
}

package com.devdesign.developer.ziptown.activities.signupActivities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.devdesign.developer.ziptown.R;
import com.devdesign.developer.ziptown.activities.LandingPageActivity;
import com.devdesign.developer.ziptown.activities.LoginActivity;
import com.devdesign.developer.ziptown.activities.NetworkIssuesActivity;
import com.devdesign.developer.ziptown.connection.ServerRequest;
import com.devdesign.developer.ziptown.models.forms.CreateUser;

import java.util.HashMap;
import java.util.Map;

public class PasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtPassword, edtPasswordConfirm;
    private FloatingActionButton fabNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_layout);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        edtPasswordConfirm = findViewById(R.id.edt_password_confirm);
        edtPassword = findViewById(R.id.edt_password);
        fabNext = findViewById(R.id.fab_next);
        fabNext.setOnClickListener(this);
    }
    private void goNext(){
        Intent intent = new Intent(this, TermsAndConditionsActivity.class);
        startActivity(intent);
    }

    private void validatePassword(){
        String password = edtPassword.getText().toString().trim();
        String passwordConfirm = edtPasswordConfirm.getText().toString().trim();
        if(password.isEmpty()){
            edtPassword.setError("Password required");

        }else if(passwordConfirm.isEmpty()){
            edtPasswordConfirm.setError("Password required");

        }else {
            if(checkPasswordMatch(password, passwordConfirm)){
                goNext();
            }else{
                //throw error pass
                edtPassword.setError("Password doesn't match");
                edtPasswordConfirm.setError("Password doesn't match");
            }

        }

    }
    private boolean checkPasswordMatch(String password, String passwordConfirm) {
        return password.equals(passwordConfirm);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_next:
                validatePassword();
                break;
        }
    }
}

package com.example.developer.ziptown.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.developer.ziptown.R;

public class LandingPageActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnSignUp, btnSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);


        btnSignIn = findViewById(R.id.btn_sign_in);
        btnSignUp = findViewById(R.id.btn_sign_up);

        btnSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_sign_in:
                Intent intentSignIn = new Intent(this, LoginActivity.class);
                startActivity(intentSignIn);
                break;
            case R.id.btn_sign_up:
                Intent intentSignUp = new Intent(this, SignUpActivity.class);
                startActivity(intentSignUp);
                break;
        }
    }
}

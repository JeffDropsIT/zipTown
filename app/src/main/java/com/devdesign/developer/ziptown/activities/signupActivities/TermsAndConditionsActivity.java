package com.devdesign.developer.ziptown.activities.signupActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.devdesign.developer.ziptown.R;
import com.devdesign.developer.ziptown.activities.LandingPageActivity;
import com.devdesign.developer.ziptown.activities.NetworkIssuesActivity;

public class TermsAndConditionsActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_and_conds_layout);
        findViewById(R.id.btn_agree).setOnClickListener(this);
    }

    private void signUser(){
        //CreateUser createUser = new CreateUser(password, name, selectedItem, city, ccp.getSelectedCountryCode()+contact);
//                Map<String, Object> map = new HashMap<>();
//                map.put("type", new String("CreateUser"));
//                map.put("model", createUser);

        Toast.makeText(this, "User Created", Toast.LENGTH_SHORT).show();
        if(LandingPageActivity.isNetworkAvailable()){
            // LoginActivity.showProgress("Sign up", "Signing up User", SignUpActivity.this);
            //new ServerRequest(this).execute(map);
        }else {
            Intent intent = new Intent(this, NetworkIssuesActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_agree:
                signUser();
                break;
        }
    }
}

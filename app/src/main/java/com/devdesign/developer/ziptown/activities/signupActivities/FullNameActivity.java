package com.devdesign.developer.ziptown.activities.signupActivities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.devdesign.developer.ziptown.R;

public class FullNameActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText fName, lName;
    private String username;
    private FloatingActionButton fabNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullname_layout);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        fabNext = findViewById(R.id.fab_next);
        fabNext.setOnClickListener(this);
        fName = findViewById(R.id.edt_fname);
        lName = findViewById(R.id.edt_lname);

    }

    private void getUsername(){
        String firstName = fName.getText().toString().trim();
        String lastName = lName.getText().toString().trim();
        if(firstName.isEmpty()){
            fName.setError("name required");
        }else if(lastName.isEmpty()){
            lName.setError("name required");

        }else {
            username = firstName.toLowerCase() +" "+lastName.toLowerCase();
            goNext();
        }

    }
    private void goNext(){
        Intent intent = new Intent(this, CityActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_next:
                getUsername();
                break;
        }
    }
}

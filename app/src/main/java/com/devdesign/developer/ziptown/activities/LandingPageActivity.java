package com.devdesign.developer.ziptown.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.devdesign.developer.ziptown.cache.ZipCache;
import com.devdesign.developer.ziptown.R;

public class LandingPageActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String DATABASE_NAME = "ZipTownDB";
    public static SharedPreferences preferences;
    private static ConnectivityManager connectivityManager;
    private Button btnSignUp, btnSignIn;
    public static ZipCache zipCache;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        zipCache = ZipCache.getInstance();
        zipCache.init(openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null));
        connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        btnSignIn = findViewById(R.id.btn_sign_in);
        btnSignUp = findViewById(R.id.btn_sign_up);

        btnSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }
    public static boolean isNetworkAvailable() {
        if(connectivityManager == null){
            return true;
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
   }

    private void userCached(){
        if(MainActivity.getString("password").toString().contains("secret password")){
            Intent intentSignIn = new Intent(this, CurrentUserActivity.class);
            startActivity(intentSignIn);
        }else {
            Intent intentSignIn = new Intent(this, LoginActivity.class);
            startActivity(intentSignIn);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_sign_in:
                userCached();
                break;
            case R.id.btn_sign_up:
                Intent intentSignUp = new Intent(this, SignUpActivity.class);
                startActivity(intentSignUp);
                break;
        }
    }
}

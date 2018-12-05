package com.devdesign.developer.ziptown.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.devdesign.developer.ziptown.activities.profileActivities.ChatsActivity;
import com.devdesign.developer.ziptown.activities.profileActivities.MessengerActivity;
import com.devdesign.developer.ziptown.activities.signupActivities.NumberActivity;
import com.devdesign.developer.ziptown.adapters.SliderAdapter;
import com.devdesign.developer.ziptown.cache.ZipCache;
import com.devdesign.developer.ziptown.R;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LandingPageActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String DATABASE_NAME = "ZipTownDB";
    public static SharedPreferences preferences;
    private static ConnectivityManager connectivityManager;
    private Button btnSignUp, btnSignIn;
    public static ZipCache zipCache;


    List<String> colorName;

    ViewPager viewPager;
    TabLayout indicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);



        StatusBarUtil.setTransparent(this);


        zipCache = ZipCache.getInstance();
        zipCache.init(openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null));
        connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        btnSignIn = findViewById(R.id.btn_sign_in);
        btnSignUp = findViewById(R.id.btn_sign_up);

        btnSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);

        viewPager =  findViewById(R.id.viewPager);
        indicator = findViewById(R.id.indicator);


        colorName = new ArrayList<>();
        colorName.add("A Smart Way To Travel, With Lift Clubs.");
        colorName.add("ZipTown Is Safe Free, And Easy To Use Even Your Grandma Can Use It.");
        colorName.add("In Fact Share ZipTown With Her And All Your Friends.");

        viewPager.setAdapter(new SliderAdapter(this, colorName));
        indicator.setupWithViewPager(viewPager, true);

        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new SliderTimer(), 2000, 4000);
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
            Intent intentSignIn = new Intent(this, MessengerActivity.class);
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
                Intent intentSignUp = new Intent(this, ChatsActivity.class);
                startActivity(intentSignUp);
                break;
        }
    }


    private class SliderTimer extends TimerTask {

        private Handler handler = new Handler();
        private Runnable runnable;
        private void reverseSlider(){
            if (viewPager.getCurrentItem() > 0){
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            }else{
                viewPager.setCurrentItem(0);
            }


        }
        @Override
        public void run() {
            LandingPageActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() < colorName.size() - 1) {

                        handler.removeCallbacks(runnable);
                        handler.removeMessages(0);
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    } else {

                        handler.postDelayed( runnable = new Runnable() {
                            public void run() {
                                reverseSlider();
                                handler.postDelayed(runnable, 500);
                            }
                        }, 500);

                    }

                }

            });
        }
    }









}

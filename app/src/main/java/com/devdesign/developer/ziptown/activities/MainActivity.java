package com.devdesign.developer.ziptown.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.devdesign.developer.ziptown.R;
import com.devdesign.developer.ziptown.adapters.ViewPagerAdapter;
import com.devdesign.developer.ziptown.connection.ServerRequest;
import com.devdesign.developer.ziptown.fragments.OffersFragment;
import com.devdesign.developer.ziptown.fragments.RequestsFragment;
import com.devdesign.developer.ziptown.fragments.SearchFragment;
import com.devdesign.developer.ziptown.fragments.TimePickerFragment;

import java.util.Calendar;
import java.util.Map;

import static com.devdesign.developer.ziptown.activities.LandingPageActivity.isNetworkAvailable;
import static com.devdesign.developer.ziptown.activities.LandingPageActivity.preferences;
import static com.devdesign.developer.ziptown.adapters.OfferAdapter.MY_PERMISSIONS_REQUEST_CALL_PHONE;


public class MainActivity extends AppCompatActivity implements ServerRequest.OnTaskCompleted, TimePickerFragment.TimePickedListener,SearchFragment.DialogFragmentInteface {


    MenuItem prevMenuItem;
    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    SearchFragment dialogFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!isNetworkAvailable() ) {
            startErrorActivity();
        }
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        viewPager =  findViewById(R.id.viewpager);
        //Initializing the bottomNavigationView

        setOnNavigationItemSelectedListener();
        addOnPageChangeListener(viewPager);
        setupViewPager(viewPager);
        setToolBar("Offers");


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                if (permissions[0].equalsIgnoreCase
                        (Manifest.permission.CALL_PHONE)
                        && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted.
                    Log.d("WSX", "grant granted");

                    Toast.makeText(this, "call permission granted", Toast.LENGTH_SHORT).show();

                } else {
                    // Permission denied. Stop the app.
                    Log.d("WSX", "failed to get permission");
                    Toast.makeText(this, "call denied", Toast.LENGTH_SHORT).show();
                    // Disable the call button

                }
            }
        }
    }
    private void startErrorActivity() {
        Intent intent = new Intent(this, NetworkIssuesActivity.class);
        startActivity(intent);
    }

    public boolean getBoolean(String key) {
        return preferences.getBoolean(key, false);
    }

    public static void putBoolean(String key, boolean value) {
        preferences.edit().putBoolean(key, value).apply();
    }
    public static String getString(String key) {
        return preferences.getString(key, "0");
    }
    public static void putString(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }
    public static void clearSecret(){
        preferences.edit().remove("password").commit();
    }


    private void addOnPageChangeListener(ViewPager viewPager){
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                if(position == 0){
                    setToolBar("Offers");
                }else {
                    setToolBar("Requests");
                }
                Log.d("page", "onPageSelected: "+position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        OffersFragment offers = new OffersFragment();
        offers.setClickable(true);
        RequestsFragment requests = new RequestsFragment();
        requests.setClickable(true);
        adapter.addFragment(offers);
        adapter.addFragment(requests);
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case android.R.id.home:
                Intent intent = new Intent(this, CurrentUserActivity.class);
                startActivity(intent);
                break;
            case R.id.search:
                dialogFragment = new SearchFragment();
                dialogFragment.setContextListener(getApplicationContext(), this);
                dialogFragment.setSelectedItem(viewPager.getCurrentItem());
                Log.i("WSX", "onOptionsItemSelected: "+ viewPager.getCurrentItem());
                dialogFragment.setActivity(this);
                dialogFragment.show(getFragmentManager(), "SearchDialog");
                break;
        }

        return true;
    }
    @Override
    public void onTimePicked(Calendar time, int id) {
        dialogFragment.setTimesOnSearchFragment(time, id);
    }
    private void setToolBar(String title){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_user_profile);
        actionbar.setTitle(title);
    }
    private void setStatusBar(int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(color));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_main_search, menu);
        return true;
    }

    public void setOnNavigationItemSelectedListener() {
        bottomNavigationView = findViewById(R.id.nav_temp);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.offers:
                                viewPager.setCurrentItem(0);
                                setToolBar("Offers");
                                Log.i("WSX", "onCreateView: offers");
                                break;
                            case R.id.requests:
                                viewPager.setCurrentItem(1);
                                setToolBar("Requests");
                                Log.i("WSX", "onCreateView: request");
                                break;


                        }
                        return false;
                    }
                });
    }

    @Override
    public void onTaskCompleted() {
        Log.i("node", "onTaskCompleted: completes");
    }

    @Override
    public void onDataFetched(Map<String, Object> object) {

    }

    @Override
    public void onTaskFailed() {
        Intent intent = new Intent(this, NetworkIssuesActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCompleteListener() {
        Log.i("WSX", "onCompleteListener: ");
        //start search results activity

        Intent intent = new Intent(MainActivity.this, SearchResults.class);
        intent.putExtra("table", dialogFragment.getSelectedItem());
        startActivity(intent);

    }
}

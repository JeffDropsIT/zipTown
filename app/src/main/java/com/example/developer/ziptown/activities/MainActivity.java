package com.example.developer.ziptown.activities;

import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.developer.ziptown.R;
import com.example.developer.ziptown.adapters.ViewPagerAdapter;
import com.example.developer.ziptown.connection.ServerRequest;
import com.example.developer.ziptown.fragments.OffersFragment;
import com.example.developer.ziptown.fragments.RequestsFragment;
import com.example.developer.ziptown.fragments.SearchFragment;
import com.example.developer.ziptown.models.forms.CreateUser;

import java.util.HashMap;
import java.util.Map;

import static com.example.developer.ziptown.activities.LandingPageActivity.preferences;

public class MainActivity extends AppCompatActivity implements ServerRequest.OnTaskCompleted {


    MenuItem prevMenuItem;
    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        viewPager =  findViewById(R.id.viewpager);
        //Initializing the bottomNavigationView

        setOnNavigationItemSelectedListener();
        addOnPageChangeListener(viewPager);
        setupViewPager(viewPager);
        setToolBar("Offers");
        postUser();

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



    private void postUser(){
        Map<String, Object> map = new HashMap<>();
        CreateUser user = new CreateUser("password123", "S'mangele Pearl Ntuli", "Passenger", "eMpangeni", "276321664554");
        map.put("createUser", user);
        //new ServerRequest(this).execute(map);
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
                SearchFragment dialogFragment = new SearchFragment();
                dialogFragment.show(getFragmentManager(), "SearchDialog");
                break;
        }

        return true;
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

    }
}

package com.example.developer.ziptown.activities;

import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.developer.ziptown.R;
import com.example.developer.ziptown.adapters.ViewPagerAdapter;
import com.example.developer.ziptown.fragments.currentUserFragments.OffersFragment;
import com.example.developer.ziptown.fragments.currentUserFragments.RequestsFragment;
import com.example.developer.ziptown.models.mockerClasses.Offer;
import com.example.developer.ziptown.models.responses.UserSignInAndLoginResponse;


public class CurrentUserActivity extends AppCompatActivity {
    MenuItem prevMenuItem;
    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    private TextView ttvUsername, ttvUserType, ttvCity, ttvContact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_user);

        ttvUsername = findViewById(R.id.ttv_username);
        ttvUserType = findViewById(R.id.ttv_tittle);
        ttvCity = findViewById(R.id.ttv_user_city);
        ttvContact = findViewById(R.id.ttv_user_contact);

        Intent data = getIntent();
        if(data != null){
            UserSignInAndLoginResponse user = (UserSignInAndLoginResponse) data.getExtras().getSerializable("user");
            MainActivity.putString("username",titleCase(user.getUser().getFullName()));
            MainActivity.putString("city",(titleCase(user.getUser().getCity())));
            MainActivity.putString("contact",titleCase(user.getUser().getContact()));
            MainActivity.putString("userType",titleCase(user.getUser().getUserType()));
            MainActivity.putString("userId",titleCase(String.valueOf(user.getUser().getId())));
            ttvUsername.setText(titleCase(user.getUser().getFullName()));
            ttvUserType.setText(titleCase(user.getUser().getUserType()));
            ttvCity.setText(titleCase(user.getUser().getCity()));
            ttvContact.setText(user.getUser().getContact());
            Toast.makeText(this, " user  " + user.getUser().getFullName(), Toast.LENGTH_SHORT).show();

        }

        Log.i("WSX", "UserProfileActivity: received");
        viewPager =  findViewById(R.id.vpg_viewpager);
        //Initializing the bottomNavigationView
        setOnNavigationItemSelectedListener();
        addOnPageChangeListener(viewPager);
        setupViewPager(viewPager);
        setToolBar();

    }

    public static String titleCase(String string){
        String[] charArr = string.split(" ");
        for (int i = 0; i < charArr.length; i++){
            charArr[i] = Character.toUpperCase(charArr[i].charAt(0)) + charArr[i].substring(1);
        }
        String out = "";
        for(int i = 0; i<charArr.length; i++){
            out += charArr[i] + " ";
        }
        return out;
    }

    private void setToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case android.R.id.home:
                goHomeActivity();
                break;
            case R.id.settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
        }

        return true;
    }

    private void goHomeActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tool_bar_menu, menu);
        return true;
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        OffersFragment offers = new OffersFragment();
        RequestsFragment requests = new RequestsFragment();
        adapter.addFragment(offers);
        adapter.addFragment(requests);
        viewPager.setAdapter(adapter);
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
                Log.d("page", "onPageSelected: "+position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
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
                                break;
                            case R.id.requests:
                                viewPager.setCurrentItem(1);
                                break;
                        }
                        return false;
                    }
                });
    }
}

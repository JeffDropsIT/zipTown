package com.example.developer.ziptown.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.developer.ziptown.R;
import com.example.developer.ziptown.adapters.ViewPagerAdapter;
import com.example.developer.ziptown.fragments.OffersFragment;
import com.example.developer.ziptown.fragments.RequestsFragment;
import com.example.developer.ziptown.models.mockerClasses.Offer;


public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {
    MenuItem prevMenuItem;
    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Log.i("WSX", "UserProfileActivity: received");
        viewPager =  findViewById(R.id.vpg_viewpager);


        findViewById(R.id.img_profile).setOnClickListener(this);
        //Initializing the bottomNavigationView
        setOnNavigationItemSelectedListener();
        addOnPageChangeListener(viewPager);
        setupViewPager(viewPager);
        //Intent data = getIntent();
        //Offer offer = (Offer) data.getExtras().getSerializable("user");
        //Toast.makeText(this, " user  " + offer.getPublisher(), Toast.LENGTH_SHORT).show();

    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        OffersFragment offers = new OffersFragment();
        offers.setClickable(false);
        RequestsFragment requests = new RequestsFragment();
        requests.setClickable(false);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_profile:
                Intent intent = new Intent(this, CurrentUserActivity.class);
                Log.i("WSX", "onClick: swithced activities");
                startActivity(intent);
                break;
        }
    }
}

package com.devdesign.developer.ziptown.fragments.drawerFragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.devdesign.developer.ziptown.R;
import com.devdesign.developer.ziptown.activities.HomeActivity;
import com.devdesign.developer.ziptown.activities.MainActivity;
import com.devdesign.developer.ziptown.activities.NetworkIssuesActivity;
import com.devdesign.developer.ziptown.adapters.ViewPagerAdapter;
import com.devdesign.developer.ziptown.connection.ServerRequest;
import com.devdesign.developer.ziptown.fragments.currentUserFragments.OffersFragment;
import com.devdesign.developer.ziptown.fragments.currentUserFragments.RequestsFragment;

import java.util.HashMap;
import java.util.Map;

import static com.devdesign.developer.ziptown.activities.LandingPageActivity.isNetworkAvailable;
import static com.devdesign.developer.ziptown.activities.LandingPageActivity.zipCache;

public class CurrentUserFragment extends Fragment implements ServerRequest.OnTaskCompleted {
    MenuItem prevMenuItem;
    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    private TextView ttvUsername, ttvUserType, ttvCity, ttvContact;
    private AsyncTask serverRequest;
    View view;
    public CurrentUserFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_current_user, container, false);
        if (!isNetworkAvailable() ) {
            //startErrorActivity();
        }

        ttvUsername = view.findViewById(R.id.ttv_username);
        ttvUserType = view.findViewById(R.id.ttv_tittle);
        ttvCity = view.findViewById(R.id.ttv_user_city);
        ttvContact = view.findViewById(R.id.ttv_user_contact);

        updateUserData();

        Log.i("WSX", "UserProfileActivity: received");
        viewPager =  view.findViewById(R.id.vpg_viewpager);
        //Initializing the bottomNavigationView
        setOnNavigationItemSelectedListener();
        addOnPageChangeListener(viewPager);
        setupViewPager(viewPager);
        return view;
    }
    private void goHomeActivity(){
        Intent intent = new Intent(getContext(), HomeActivity.class);
        startActivity(intent);
    }
    private void updateUserData(){
        Map<String, Object> user = zipCache.getLocalUserData();
        if(user != null){

            MainActivity.putString("username",titleCase(user.get("fullName").toString()));
            MainActivity.putString("city",(titleCase(user.get("city").toString())));
            MainActivity.putString("contact",user.get("contact").toString());
            MainActivity.putString("userType",titleCase(user.get("userType").toString()));
            MainActivity.putString("userId",user.get("id").toString());
            MainActivity.putString("password",user.get("password").toString());
            ttvUsername.setText(titleCase(user.get("fullName").toString()));
            ttvUserType.setText(titleCase(user.get("userType").toString()));
            ttvCity.setText(titleCase(user.get("city").toString()));
            ttvContact.setText(user.get("contact").toString());
            Toast.makeText(getContext(), " user  " +user.get("contact").toString(), Toast.LENGTH_SHORT).show();

        }else {
            //get data from the cache

        }
    }

    @Override
    public void onResume() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "GetUser");


        if(isNetworkAvailable()){
            new ServerRequest(this, getContext()).execute(map);
        }else {
            Intent intent = new Intent(getContext(), NetworkIssuesActivity.class);
            startActivity(intent);
        }
        super.onResume();
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

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
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
        bottomNavigationView = view.findViewById(R.id.nav_temp);
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
    public void onTaskCompleted() {

    }

    @Override
    public void onDataFetched(Map<String, Object> object) {

        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                updateUserData();
                Toast.makeText(getContext(), "refreshing...", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onTaskFailed() {
        Intent intent = new Intent(getContext(), NetworkIssuesActivity.class);
        startActivity(intent);
    }
}

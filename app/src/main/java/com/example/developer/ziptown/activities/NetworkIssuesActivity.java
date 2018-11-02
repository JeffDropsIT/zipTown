package com.example.developer.ziptown.activities;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.developer.ziptown.R;

import static com.example.developer.ziptown.activities.LandingPageActivity.isNetworkAvailable;

public class NetworkIssuesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_issues);

        refreshLayout();
    }
    private void refreshLayout(){
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.srl_layout);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onNetworkFound();
                pullToRefresh.setRefreshing(false);
            }
        });

    }
    private void onNetworkFound(){
        if(isNetworkAvailable() ){
            startMainActivity();
        }
    }
    private void startMainActivity(){
        onBackPressed();
        finish();
    }
}

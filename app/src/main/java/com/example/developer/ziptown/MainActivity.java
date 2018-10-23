package com.example.developer.ziptown;

import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.developer.ziptown.adapters.OfferAdapter;
import com.example.developer.ziptown.models.Offer;
import com.example.developer.ziptown.models.Publisher;
import com.example.developer.ziptown.recylcler.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Offer> offersList = new ArrayList<>();
    private RecyclerView recyclerView;
    private OfferAdapter mOfferAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.rcl_offers);
        mOfferAdapter = new OfferAdapter(offersList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mOfferAdapter);

        recyclerView.addOnItemTouchListener( new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.i("WSX", "onClick: preseed");
                Offer offer = offersList.get(position);
                Toast.makeText(MainActivity.this, position+" "+offer.getPublisher(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                Log.i("WSX", "onClick: preseed");
            }
        }));
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);
        actionbar.setTitle("Offers");
        prepareOffersData();
    }
    private void setStatusBar(int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(color));
        }
    }
    private void prepareOffersData() {
        Publisher publisher = new Publisher("Phindile Sthah Ngobese", "+27710081789");

        Offer offer = new Offer("Sowetho", "Randburg", "8 PM To 16 PM", "Monday To Friday", "Pretoria", "2018/10/23", publisher);
        offersList.add(offer);

        for(int i = 0; i < 100 ; i++){
            offer = new Offer("Sowetho", "Randburg", "8 PM To 16 PM", "Monday To Friday", "Pretoria", "2018/10/23", publisher);
            offersList.add(offer);
        }
        mOfferAdapter.notifyDataSetChanged();
    }
}

package com.devdesign.developer.ziptown.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.devdesign.developer.ziptown.R;
import com.devdesign.developer.ziptown.adapters.OfferAdapter;
import com.devdesign.developer.ziptown.cache.ZipCache;
import com.devdesign.developer.ziptown.models.mockerClasses.Offer;
import com.devdesign.developer.ziptown.models.mockerClasses.Publisher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.devdesign.developer.ziptown.activities.LandingPageActivity.zipCache;

public class SearchResults extends AppCompatActivity {
    private List<Offer> offersList = new ArrayList<>();
    private RecyclerView recyclerView;
    private OfferAdapter mOfferAdapter;
    TextView ttvNoPosts;
    String tableName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        recyclerView = findViewById(R.id.rcl_offers);
        mOfferAdapter = new OfferAdapter(offersList, getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mOfferAdapter);

        setToolBar();
        Intent intent = getIntent();
        if(intent != null){
            int optionId = intent.getIntExtra("table", 0);

            if(optionId == 0){
                tableName = ZipCache.OFFERS_SEARCH;
            }else {
                tableName = ZipCache.REQUESTS_SEARCH;
            }
            prepareOffersData(tableName);
        }



    }

    @Override
    protected void onDestroy() {
        if(tableName != null){
            ZipCache.getInstance().clearTable(tableName);
        }

        super.onDestroy();
    }

    private void prepareOffersData(String table) {



        Map<String, Object> user = zipCache.getLocalUserData();
        Map<String,  Map<String, Object>> offers = zipCache.getLocalPost(table);
        Publisher publisher = new Publisher(user.get("fullName").toString(), user.get("contact").toString(), Integer.valueOf(user.get("id").toString()));
        Offer offer;


        Log.i("WSX", "prepareOffersData: offers: "+offers);
        Log.i("WSX", "prepareOffersData: user: "+user);
        if(offers.keySet().size() > 0)
            offersList.clear();
        for (String key : offers.keySet()) {
            Map<String, Object> offerTmp = offers.get(key);
            offer = new Offer(offerTmp.get("origin").toString(), offerTmp.get("destination").toString(), offerTmp.get("depatureTime").toString()+" To "+offerTmp.get("returnTime").toString(), offerTmp.get("days").toString(), offerTmp.get("city").toString(), offerTmp.get("created").toString(), publisher);
            offersList.add(offer);
        }
//        if(offers.keySet().size() == 0){
//            Log.i("WSX", "prepareOffersData: empty data");
//            ttvNoPosts.setVisibility(View.VISIBLE);
//
//        }else {
//            ttvNoPosts.setVisibility(View.GONE);
//        }
        mOfferAdapter.notifyDataSetChanged();

    }

    private void setToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar_container);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("Search Results");
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                finish();
                break;
        }
        return true;
    }
}

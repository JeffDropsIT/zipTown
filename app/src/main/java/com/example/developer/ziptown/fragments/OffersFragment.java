package com.example.developer.ziptown.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.developer.ziptown.R;
import com.example.developer.ziptown.activities.UserProfileActivity;
import com.example.developer.ziptown.adapters.OfferAdapter;
import com.example.developer.ziptown.cache.ZipCache;
import com.example.developer.ziptown.connection.ServerRequest;
import com.example.developer.ziptown.models.mockerClasses.Offer;
import com.example.developer.ziptown.models.mockerClasses.Publisher;
import com.example.developer.ziptown.recylcler.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.developer.ziptown.activities.LandingPageActivity.zipCache;

public class OffersFragment extends Fragment implements ServerRequest.OnTaskCompleted {
    private List<Offer> offersList = new ArrayList<>();
    private RecyclerView recyclerView;
    private OfferAdapter mOfferAdapter;
    private boolean isClickable;
    public OffersFragment(){

    }

    public void setClickable(boolean clickable) {
        isClickable = clickable;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.offer_fragment_layout, container, false);
        // Inflate the layout for this fragment
        recyclerView = view.findViewById(R.id.rcl_offers);
        mOfferAdapter = new OfferAdapter(offersList, getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mOfferAdapter);
        getPosts();
        prepareOffersData();

        return view;
    }




    private void prepareOffersData() {



        Map<String, Object> user = zipCache.getLocalUserData();
        Map<String,  Map<String, Object>> offers = zipCache.getLocalPost(ZipCache.OFFERS);
        Publisher publisher = new Publisher(user.get("fullName").toString(), user.get("contact").toString(), Integer.valueOf(user.get("id").toString()));
        Offer offer;


        Log.i("WSX", "prepareOffersData: offers: "+offers);
        Log.i("WSX", "prepareOffersData: user: "+user);

        for (String key : offers.keySet()) {
            Map<String, Object> offerTmp = offers.get(key);
            offer = new Offer(offerTmp.get("origin").toString(), offerTmp.get("destination").toString(), offerTmp.get("depatureTime").toString()+" To "+offerTmp.get("returnTime").toString(), offerTmp.get("days").toString(), offerTmp.get("city").toString(), offerTmp.get("created").toString(), publisher);
            offersList.add(offer);
        }

        mOfferAdapter.notifyDataSetChanged();

    }


    private void getPosts(){
        Map<String, Object> map = new HashMap<>();
        map.put("postType", "offers");
        map.put("type", "GetPost");
        map.put("city", "Pretoria");

        new ServerRequest(this).execute(map);

    }

    @Override
    public void onTaskCompleted() {
        prepareOffersData();
    }

    @Override
    public void onDataFetched(Map<String, Object> object) {

    }

    @Override
    public void onTaskFailed() {

    }
}

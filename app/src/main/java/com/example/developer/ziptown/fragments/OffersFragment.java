package com.example.developer.ziptown.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.developer.ziptown.R;
import com.example.developer.ziptown.activities.MainActivity;
import com.example.developer.ziptown.activities.NetworkIssuesActivity;
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

import static com.example.developer.ziptown.activities.LandingPageActivity.isNetworkAvailable;
import static com.example.developer.ziptown.activities.LandingPageActivity.zipCache;
import static com.example.developer.ziptown.adapters.OfferAdapter.MY_PERMISSIONS_REQUEST_CALL_PHONE;

public class OffersFragment extends Fragment implements ServerRequest.OnTaskCompleted {
    private List<Offer> offersList = new ArrayList<>();
    private RecyclerView recyclerView;
    private OfferAdapter mOfferAdapter;
    TextView ttvNoPosts;
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
        ttvNoPosts = view.findViewById(R.id.ttv_no_post);
        getPosts();
        prepareOffersData();

        return view;
    }


    @Override
    public void onResume() {
        getPosts();
        super.onResume();
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

                } else {
                    // Permission denied. Stop the app.
                    Log.d("WSX", "failed to get permission");
                    Toast.makeText(getContext(), "call denied", Toast.LENGTH_SHORT).show();
                    // Disable the call button

                }
            }
        }
    }

    private void prepareOffersData() {



        Map<String, Object> user = zipCache.getLocalUserData();
        Map<String,  Map<String, Object>> offers = zipCache.getLocalPost(ZipCache.OFFERS);

        Offer offer;


        Log.i("WSX", "prepareOffersData: ALL offers: "+offers);
        Log.i("WSX", "prepareOffersData:  ALL user: "+user);
        if(offers.keySet().size() > 0)
            offersList.clear();
        for (String key : offers.keySet()) {
            Map<String, Object> offerTmp = offers.get(key);
            Publisher publisher = new Publisher(offerTmp.get("publisher").toString(), offerTmp.get("contact").toString(), Integer.valueOf(offerTmp.get("publisherId").toString()));
            offer = new Offer(offerTmp.get("origin").toString(), offerTmp.get("destination").toString(), offerTmp.get("depatureTime").toString()+" To "+offerTmp.get("returnTime").toString(), offerTmp.get("days").toString(), offerTmp.get("city").toString(), offerTmp.get("created").toString(), publisher);
            offersList.add(offer);
        }
        if(offers.keySet().size() == 0){
            Log.i("WSX", "prepareOffersData: empty data");
            ttvNoPosts.setVisibility(View.VISIBLE);

        }else {
            ttvNoPosts.setVisibility(View.GONE);
        }
        mOfferAdapter.notifyDataSetChanged();

    }


    private void getPosts(){
        Map<String, Object> map = new HashMap<>();
        map.put("postType", "offers");
        map.put("type", "GetPost");
        map.put("city", MainActivity.getString("city"));


        if(isNetworkAvailable()){
            new ServerRequest(this, getContext()).execute(map);
        }else {
            Intent intent = new Intent(getContext(), NetworkIssuesActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void onTaskCompleted() {
        prepareOffersData();
    }

    @Override
    public void onDataFetched(Map<String, Object> object) {
        if(getActivity() == null){
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                prepareOffersData();
            }
        });
    }

    @Override
    public void onTaskFailed() {

    }
}

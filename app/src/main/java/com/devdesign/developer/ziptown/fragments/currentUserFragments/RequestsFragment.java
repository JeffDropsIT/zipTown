package com.devdesign.developer.ziptown.fragments.currentUserFragments;

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
import android.widget.TextView;
import android.widget.Toast;

import com.devdesign.developer.ziptown.activities.NetworkIssuesActivity;
import com.devdesign.developer.ziptown.adapters.currentUserAdpt.OfferAdapter;
import com.devdesign.developer.ziptown.cache.ZipCache;
import com.devdesign.developer.ziptown.models.mockerClasses.Offer;
import com.devdesign.developer.ziptown.models.mockerClasses.Publisher;
import com.devdesign.developer.ziptown.activities.AddGenericPostActivity;
import com.devdesign.developer.ziptown.R;
import com.devdesign.developer.ziptown.connection.ServerRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.devdesign.developer.ziptown.activities.LandingPageActivity.isNetworkAvailable;
import static com.devdesign.developer.ziptown.activities.LandingPageActivity.zipCache;

public class RequestsFragment extends Fragment implements View.OnClickListener, ServerRequest.OnTaskCompleted {
    private List<Offer> offersList = new ArrayList<>();
    private RecyclerView recyclerView;
    private OfferAdapter mOfferAdapter;
    TextView ttvNoPosts;
    public RequestsFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.private_user_requests_fragment, container, false);
        // Inflate the layout for this fragment
        recyclerView = view.findViewById(R.id.rcl_offers);
        mOfferAdapter = new OfferAdapter(offersList, getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mOfferAdapter);
        Log.i("WSX", "onCreateView: request");
        ttvNoPosts = view.findViewById(R.id.ttv_no_post);
        view.findViewById(R.id.fab_booking).setOnClickListener(this);

        prepareOffersData();
        return view;
    }

    private void prepareOffersData() {
        Map<String, Object> user = zipCache.getLocalUserData();
        Map<String,  Map<String, Object>> offers = zipCache.getLocalPost(ZipCache.USER_REQUESTS);
        Publisher publisher = new Publisher(user.get("fullName").toString(), user.get("contact").toString(), Integer.valueOf(user.get("id").toString()));
        Offer offer;

        Log.i("WSX", "prepareOffersData: requests: "+offers);
        Log.i("WSX", "prepareOffersData: user: "+user);
        if(offers.keySet().size() > 0)
            offersList.clear();
        for (String key : offers.keySet()) {
            Map<String, Object> offerTmp = offers.get(key);
            offer = new Offer(offerTmp.get("origin").toString(), offerTmp.get("destination").toString(), offerTmp.get("depatureTime").toString()+" To "+offerTmp.get("returnTime").toString(), offerTmp.get("days").toString(), offerTmp.get("city").toString(), offerTmp.get("created").toString(), publisher);
            offer.setPostType("request");
            offer.setPostId(offerTmp.get("id").toString());
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_booking:
                Toast.makeText(getContext(), "add request", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), AddGenericPostActivity.class);
                intent.putExtra("type", "request");
                startActivity(intent);
                break;

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

    @Override
    public void onTaskCompleted() {

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

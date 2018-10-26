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
import com.example.developer.ziptown.models.Offer;
import com.example.developer.ziptown.models.Publisher;
import com.example.developer.ziptown.recylcler.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

public class RequestsFragment extends Fragment {
    private List<Offer> offersList = new ArrayList<>();
    private RecyclerView recyclerView;
    private OfferAdapter mOfferAdapter;
    private boolean isClickable;

    public RequestsFragment(){

    }

    public void setClickable(boolean clickable) {
        isClickable = clickable;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.request_fragment_layout, container, false);
        // Inflate the layout for this fragment
        recyclerView = view.findViewById(R.id.rcl_offers);
        mOfferAdapter = new OfferAdapter(offersList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mOfferAdapter);
        Log.i("WSX", "onCreateView: request");
        if(isClickable){
            onItemClickRecycler();
        }
        prepareOffersData();
        return view;
    }

    private void onItemClickRecycler(){
        recyclerView.addOnItemTouchListener( new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.i("WSX", "onClick: preseed");
                Offer offer = offersList.get(position);
                Bundle bundle = new Bundle();
                Intent intent = new Intent(getContext(), UserProfileActivity.class);

                bundle.putSerializable("user", offer);
                intent.putExtras(bundle);

                startActivity(intent);

                Toast.makeText(getContext(), offer.getPublisherId()+" "+offer.getPublisher(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                Log.i("WSX", "onClick: preseed");
            }
        }));
    }
    private void prepareOffersData() {
        Publisher publisher = new Publisher("Phindile Sthah Ngobese", "+27710081789", 1);

        Offer offer = new Offer("Sowetho", "Randburg", "8 PM To 16 PM", "Monday To Friday", "Pretoria", "2018/10/23", publisher);
        offersList.add(offer);

        for(int i = 0; i < 100 ; i++){
            publisher = new Publisher("Phindile Sthah Ngobese", "+27710081789", i);
            offer = new Offer("Sowetho", "Randburg", "8 PM To 16 PM", "Monday To Friday", "Pretoria", "2018/10/23", publisher);
            offersList.add(offer);
        }
        mOfferAdapter.notifyDataSetChanged();
    }
}

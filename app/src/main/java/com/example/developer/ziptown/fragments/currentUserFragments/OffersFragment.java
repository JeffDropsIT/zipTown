package com.example.developer.ziptown.fragments.currentUserFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.developer.ziptown.AddGenericPostActivity;
import com.example.developer.ziptown.R;
import com.example.developer.ziptown.adapters.currentUserAdpt.OfferAdapter;
import com.example.developer.ziptown.models.Offer;
import com.example.developer.ziptown.models.Publisher;

import java.util.ArrayList;
import java.util.List;

public class OffersFragment extends Fragment implements View.OnClickListener {
    private List<Offer> offersList = new ArrayList<>();
    private RecyclerView recyclerView;
    private OfferAdapter mOfferAdapter;
    public OffersFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.private_user_offers_fragment, container, false);
        // Inflate the layout for this fragment
        recyclerView = view.findViewById(R.id.rcl_offers);
        mOfferAdapter = new OfferAdapter(offersList, getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mOfferAdapter);

        view.findViewById(R.id.fab_booking).setOnClickListener(this);
        prepareOffersData();
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_booking:
                Toast.makeText(getContext(), "add offer", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), AddGenericPostActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void prepareOffersData() {
        Publisher publisher = new Publisher("Phindile Sthah Ngobese", "+27710081789", 1);

        Offer offer = new Offer("Sowetho", "Randburg", "8 PM To 16 PM", "Monday To Friday", "Pretoria", "2018/10/23", publisher);
        offersList.add(offer);

        for(int i = 0; i < 8 ; i++){
            publisher = new Publisher("Phindile Sthah Ngobese", "+27710081789", i);
            offer = new Offer("Sowetho", "Randburg", "8 PM To 16 PM", "Monday To Friday", "Pretoria", "2018/10/23", publisher);
            offersList.add(offer);
        }
        mOfferAdapter.notifyDataSetChanged();
    }
}

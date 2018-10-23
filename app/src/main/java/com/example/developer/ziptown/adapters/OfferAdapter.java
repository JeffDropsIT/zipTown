package com.example.developer.ziptown.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.developer.ziptown.R;
import com.example.developer.ziptown.models.Offer;

import java.util.List;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferViewHolder> {
    private List<Offer> offersList;
    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offers_layout, parent, false);
        return new OfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferViewHolder holder, final int position) {
        Offer offer = offersList.get(position);
        holder.ttvCity.setText("City: "+offer.getCity());
        holder.ttvContact.setText("Contact: "+offer.getContact());
        holder.ttvCreated.setText("Posted on: "+offer.getCreated());
        holder.ttvDays.setText("Days: "+offer.getDays());
        holder.ttvOrigin.setText("Origin: "+offer.getOrigin());
        holder.ttvDestination.setText("Destination: "+offer.getDestination());
        holder.ttvPublisher.setText("Published by: "+offer.getPublisher());
        holder.ttvTime.setText("Time: "+offer.getTime());

    }

    @Override
    public int getItemCount() {
        return offersList.size();
    }

    public class OfferViewHolder extends RecyclerView.ViewHolder {
        public TextView ttvPublisher, ttvContact, ttvCreated, ttvDays, ttvTime, ttvCity, ttvDestination, ttvOrigin;
        public CardView crdView;
        public OfferViewHolder(View itemView) {
            super(itemView);

            ttvCity = itemView.findViewById(R.id.ttv_city);
            ttvDestination = itemView.findViewById(R.id.ttv_destination);
            ttvOrigin = itemView.findViewById(R.id.ttv_origin);
            ttvTime = itemView.findViewById(R.id.ttv_time);
            ttvDays = itemView.findViewById(R.id.ttv_days);
            ttvPublisher = itemView.findViewById(R.id.ttv_publisher);
            ttvContact = itemView.findViewById(R.id.ttv_contact);
            ttvContact = itemView.findViewById(R.id.ttv_contact);
            ttvCreated = itemView.findViewById(R.id.ttv_created);

        }
    }
    public OfferAdapter(List<Offer> offers){
        this.offersList = offers;
    }

}

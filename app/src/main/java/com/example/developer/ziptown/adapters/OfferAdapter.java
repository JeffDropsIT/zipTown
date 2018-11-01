package com.example.developer.ziptown.adapters;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.developer.ziptown.R;
import com.example.developer.ziptown.activities.CurrentUserActivity;
import com.example.developer.ziptown.activities.UserProfileActivity;
import com.example.developer.ziptown.models.mockerClasses.Offer;

import java.util.List;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferViewHolder> {
    private List<Offer> offersList;
    private Context context;

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offers_layout, parent, false);
        return new OfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferViewHolder holder, final int position) {
        final Offer offer = offersList.get(position);
        holder.ttvCity.setText("City: " + offer.getCity());
        holder.ttvContact.setText("Contact: " + offer.getContact());
        holder.ttvCreated.setText("Posted on: " + offer.getCreated());
        holder.ttvDays.setText("Days: " + offer.getDays());
        holder.ttvOrigin.setText("Origin: " + offer.getOrigin());
        holder.ttvDestination.setText("Destination: " + offer.getDestination());
        holder.ttvPublisher.setText("Published by: " + offer.getPublisher());
        holder.ttvTime.setText("Time: " + offer.getTime());
        holder.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("WSX", "onClick: on btn call");
//                Intent intent = new Intent(context, UserProfileActivity.class);
//                context.startActivity(intent);
                Intent intent = new Intent(Intent.ACTION_CALL);

                intent.setData(Uri.parse("tel:+" + offer.getContact()));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return offersList.size();
    }

    public class OfferViewHolder extends RecyclerView.ViewHolder {
        public TextView ttvPublisher, ttvContact, ttvCreated, ttvDays, ttvTime, ttvCity, ttvDestination, ttvOrigin;
        public CardView crdView;
        public Button btnCall;
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
            btnCall = itemView.findViewById(R.id.btn_call);

        }
    }
    public OfferAdapter(List<Offer> offers){
        this.offersList = offers;
    }
    public OfferAdapter(List<Offer> offers, Context context){
        this.offersList = offers;
        this.context = context;
    }
}

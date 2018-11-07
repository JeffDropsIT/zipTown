package com.example.developer.ziptown.adapters.currentUserAdpt;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.developer.ziptown.R;
import com.example.developer.ziptown.activities.LoginActivity;
import com.example.developer.ziptown.connection.ServerRequest;
import com.example.developer.ziptown.models.mockerClasses.Offer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.developer.ziptown.activities.LoginActivity.dismissProgress;
import static com.example.developer.ziptown.activities.LoginActivity.showProgress;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferViewHolder> implements ServerRequest.OnTaskCompleted {
    private final Context context;
    private List<Offer> offersList;
    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.private_user_offers, parent, false);
        return new OfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferViewHolder holder, final int position) {
        final Offer offer = offersList.get(position);
        holder.ttvCity.setText("City: "+offer.getCity());
        holder.ttvContact.setText("Contact: "+offer.getContact());
        holder.ttvCreated.setText("Posted on: "+offer.getCreated());
        holder.ttvDays.setText("Days: "+offer.getDays());
        holder.ttvOrigin.setText("Origin: "+offer.getOrigin());
        holder.ttvDestination.setText("Destination: "+offer.getDestination());
        holder.ttvPublisher.setText("Published by: "+offer.getPublisher());
        holder.ttvTime.setText("Time: "+offer.getTime());
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offersList.remove(position);
                Map<String, Object> map =  new HashMap<>();
                map.put("type", "DeletePost");
                map.put("postType", offer.getPostType());
                map.put("id", offer.getPostId());
                showProgress("Delete", "Deleting post "+offer.getPostId().toString()+" from server", context);
                new ServerRequest(OfferAdapter.this).execute(map);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return offersList.size();
    }

    @Override
    public void onTaskCompleted() {
        dismissProgress();
        notifyDataSetChanged();
    }

    @Override
    public void onDataFetched(Map<String, Object> object) {

    }

    @Override
    public void onTaskFailed() {

    }

    public class OfferViewHolder extends RecyclerView.ViewHolder {
        public TextView ttvPublisher, ttvContact, ttvCreated, ttvDays, ttvTime, ttvCity, ttvDestination, ttvOrigin;
        public Button btnDelete;
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
            btnDelete = itemView.findViewById(R.id.btn_delete);


        }
    }
    public OfferAdapter(List<Offer> offers, Context context){
        this.offersList = offers;
        this.context = context;
    }

}

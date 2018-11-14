package com.devdesign.developer.ziptown.activities.signupActivities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;

import com.devdesign.developer.ziptown.R;
import com.devdesign.developer.ziptown.activities.AddGenericPostActivity;
import com.devdesign.developer.ziptown.adapters.PlaceAutocompleteAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

public class CityActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private AutoCompleteTextView mSearchCity;
    private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    private FloatingActionButton fabNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_layout);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        fabNext = findViewById(R.id.fab_next);
        getCitySuggestions();
        fabNext.setOnClickListener(this);

    }

    private void validateCity(){
        String city = mSearchCity.getText().toString().trim().toLowerCase();
        if(city.isEmpty()) {
            mSearchCity.setError("city required");
        }else {
            goNext();
        }
    }

    private void goNext(){
        Intent intent = new Intent(this, DriverOrPassengerActivity.class);
        startActivity(intent);
    }
    private void getCitySuggestions(){
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        placeAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient,
                AddGenericPostActivity.LAT_LNG_BOUNDS, null);
        mSearchCity = findViewById(R.id.edt_city);
        mSearchCity.setAdapter(placeAutocompleteAdapter);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_next:
                validateCity();
                break;
        }
    }
}

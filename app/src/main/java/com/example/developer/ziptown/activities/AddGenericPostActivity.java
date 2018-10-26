package com.example.developer.ziptown.activities;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.developer.ziptown.R;
import com.example.developer.ziptown.adapters.PlaceAutocompleteAdapter;
import com.example.developer.ziptown.fragments.TimePickerFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddGenericPostActivity extends AppCompatActivity implements View.OnClickListener, TimePickerFragment.TimePickedListener, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "WSX";
    private TextView ttvMon, ttvTue, ttvWed, ttvThur, ttvFri, ttvSat, ttvSun;
    private List<TextView> textViewArrayList = new ArrayList<>();
    private List<String> daysSelected = new ArrayList<>();
    private TextView ttvSelectedDays;
    private AutoCompleteTextView mSearchOrigin, mSearchDestination, mSearchCity;
    private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    public static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_generic_post);


        getCitySuggestions();

        ttvSelectedDays = findViewById(R.id.ttv_selected_days);
        ttvMon = findViewById(R.id.ttv_mon);
        ttvTue = findViewById(R.id.ttv_tue);
        ttvWed = findViewById(R.id.ttv_wed);
        ttvThur = findViewById(R.id.ttv_thur);
        ttvFri = findViewById(R.id.ttv_fri);
        ttvSat = findViewById(R.id.ttv_sat);
        ttvSun = findViewById(R.id.ttv_sun);

        setOnClickListeners();
        disableFocus(false);


    }

    private void disableFocus(boolean b){
        findViewById(R.id.edt_pickup).setFocusable(b);
        findViewById(R.id.edt_depart).setFocusable(b);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ttv_mon:
                setSelectedDays(ttvMon, "Mon");

                break;
            case R.id.ttv_tue:
                setSelectedDays(ttvTue, "Tue");
                break;
            case R.id.ttv_wed:
                setSelectedDays(ttvWed, "Wed");
                break;
            case R.id.ttv_thur:
                setSelectedDays(ttvThur, "Thu");
                break;
            case R.id.ttv_fri:
                setSelectedDays(ttvFri, "Fri");
                break;
            case R.id.ttv_sat:
                setSelectedDays(ttvSat, "Sat");
                break;
            case R.id.ttv_sun:
                setSelectedDays(ttvSun, "Sun");
                break;
            case R.id.btn_finished:
                break;
            case R.id.edt_depart:
                showTimePickerDialog(0);
                break;
            case R.id.edt_pickup:
                showTimePickerDialog(1);
                break;

        }

    }
    public void showTimePickerDialog(int id) {
        DialogFragment newFragment = TimePickerFragment.newInstance(id);
        FragmentManager fm = getFragmentManager();
        newFragment.show(fm, "timePicker");
    }


    private void setOnClickListeners(){
        ttvSun.setOnClickListener(this);
        ttvSat.setOnClickListener(this);
        ttvFri.setOnClickListener(this);
        ttvThur.setOnClickListener(this);
        ttvWed.setOnClickListener(this);
        ttvTue.setOnClickListener(this);
        ttvMon.setOnClickListener(this);
        findViewById(R.id.btn_finished).setOnClickListener(this);
        findViewById(R.id.edt_depart).setOnClickListener(this);
        findViewById(R.id.edt_pickup).setOnClickListener(this);
    }
    private void setSelectedDays(TextView textView, String day){

        if(textViewArrayList.contains(textView)){
            textView.setTextColor(getResources().getColor(R.color.colorGrey));
            textView.setBackground(getDrawable(R.drawable.shape_text));
            textViewArrayList.remove(textView);
            daysSelected.remove(day);
        }else {
            textView.setTextColor(Color.WHITE);
            textView.setBackground(getDrawable(R.drawable.shape_text_selected));
            textViewArrayList.add(textView);
            daysSelected.add(day);
        }
        updateSelectedDays();


    }

    private void updateSelectedDays(){
        String allSelectedDays = "";
        for(int i = 0; i < daysSelected.size(); i++){

           if(daysSelected.size() - 1 == i ){
               allSelectedDays += daysSelected.get(i);
           }else {
               allSelectedDays += daysSelected.get(i) + ", ";
           }
        }
        if(daysSelected.size() == 7){
            ttvSelectedDays.setText("Every day");
        }else {
            ttvSelectedDays.setText(allSelectedDays);
        }

    }



    @Override
    public void onTimePicked(Calendar time, int id) {
        if(id == 0){
            EditText editText = findViewById(R.id.edt_depart);
            String AM_PM = String.valueOf(time.get(Calendar.AM_PM)).contains("0") ? "AM" : "PM";
            String minutes = String.valueOf(time.get(Calendar.MINUTE)).length() == 1 ? "0"+time.get(Calendar.MINUTE) : String.valueOf(time.get(Calendar.MINUTE));
            String pickedTime = time.get(Calendar.HOUR_OF_DAY) +" : "+minutes+ " " +AM_PM;
            editText.setText(pickedTime);
            editText.setHint("");
        }else {
            EditText editText = findViewById(R.id.edt_pickup);
            String AM_PM = String.valueOf(time.get(Calendar.AM_PM)).contains("0") ? "AM" : "PM";
            String minutes = String.valueOf(time.get(Calendar.MINUTE)).length() == 1 ? "0"+time.get(Calendar.MINUTE) : String.valueOf(time.get(Calendar.MINUTE));
            String pickedTime = time.get(Calendar.HOUR_OF_DAY) +" : "+minutes+ " " +AM_PM;
            editText.setText(pickedTime);
            editText.setHint("");
        }
        Toast.makeText(this, id+" id "+time.getTime(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void getCitySuggestions() {
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        placeAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient,
                LAT_LNG_BOUNDS, null);


        mSearchOrigin = findViewById(R.id.edt_origin);
        mSearchDestination = findViewById(R.id.edt_destination);
        mSearchCity = findViewById(R.id.edt_city);
        mSearchOrigin.setAdapter(placeAutocompleteAdapter);
        mSearchCity.setAdapter(placeAutocompleteAdapter);
        mSearchDestination.setAdapter(placeAutocompleteAdapter);
    }
}

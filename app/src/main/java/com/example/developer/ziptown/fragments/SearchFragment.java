package com.example.developer.ziptown.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.developer.ziptown.R;
import com.example.developer.ziptown.activities.MainActivity;
import com.example.developer.ziptown.adapters.PlaceAutocompleteAdapter;
import com.example.developer.ziptown.connection.ServerRequest;
import com.example.developer.ziptown.models.forms.CreateCustomSearch;
import com.example.developer.ziptown.models.forms.CreateOffer;
import com.example.developer.ziptown.models.forms.CreateRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchFragment  extends DialogFragment implements View.OnClickListener,TimePickerFragment.TimePickedListener, GoogleApiClient.OnConnectionFailedListener, ServerRequest.OnTaskCompleted {
    private LinearLayout linFilters;
    private TextView ttvMon, ttvTue, ttvWed, ttvThur, ttvFri, ttvSat, ttvSun;
    private List<TextView> textViewArrayList = new ArrayList<>();
    private List<String> daysSelected = new ArrayList<>();
    private TextView ttvSelectedDays;
    private AutoCompleteTextView mSearchOrigin, mSearchDestination, mSearchCity;
    private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    public static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));
    private GoogleApiClient mGoogleApiClient;
    String pickedPickUpTime, pickedDepartTime, type, allSelectedDays, city, origin, destination;
    private boolean visible;
    View view;
    DialogFragmentInteface listener;
    Context context;
    private FragmentActivity activity;
    private int selectedItem;

    public void setContextListener(Context context, DialogFragmentInteface listener){
        this.context = context;
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        visible = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.search_dialog_fragment,container, false );
        linFilters = view.findViewById(R.id.lin_filters);
        view.findViewById(R.id.lin_dropdown).setOnClickListener(this);
        getCitySuggestions(view);
        ttvSelectedDays = view.findViewById(R.id.ttv_selected_days);

        ttvMon = view.findViewById(R.id.ttv_mon);
        ttvTue = view.findViewById(R.id.ttv_tue);
        ttvWed = view.findViewById(R.id.ttv_wed);
        ttvThur = view.findViewById(R.id.ttv_thur);
        ttvFri = view.findViewById(R.id.ttv_fri);
        ttvSat = view.findViewById(R.id.ttv_sat);
        ttvSun = view.findViewById(R.id.ttv_sun);

        setOnClickListeners(view);
        disableFocus(false, view);
        return view;
    }

    public void showTimePickerDialog(int id) {
        DialogFragment newFragment = TimePickerFragment.newInstance(id);
        FragmentManager fm = getFragmentManager();
        newFragment.show(fm, "timePicker");
    }

    private void setSelectedDays(TextView textView, String day){

        if(textViewArrayList.contains(textView)){
            textView.setTextColor(getResources().getColor(R.color.colorGrey));
            textView.setBackground(context.getResources().getDrawable(R.drawable.shape_text));
            textViewArrayList.remove(textView);
            daysSelected.remove(day);
        }else {
            textView.setTextColor(Color.WHITE);
            textView.setBackground(context.getResources().getDrawable(R.drawable.shape_text_selected));
            textViewArrayList.add(textView);
            daysSelected.add(day);
        }
        updateSelectedDays();


    }

    private void updateSelectedDays(){
        allSelectedDays = "";
        for(int i = 0; i < daysSelected.size(); i++){

            if(daysSelected.size() - 1 == i ){
                allSelectedDays += daysSelected.get(i);
            }else {
                allSelectedDays += daysSelected.get(i) + ", ";
            }
        }
        if(daysSelected.size() == 7){
            allSelectedDays = "Every day";
            ttvSelectedDays.setText("Every day");
        }else {
            ttvSelectedDays.setText(allSelectedDays);
        }

    }
    private void validateLocationData(View view){
        city = mSearchCity.getText().toString().trim();
        origin = mSearchOrigin.getText().toString().trim();
        destination = mSearchDestination.getText().toString().trim();
        EditText returnTime = view.findViewById(R.id.edt_pickup);
        pickedPickUpTime = returnTime.getText().toString().trim();
        EditText departureTime =  view.findViewById(R.id.edt_depart);
        pickedDepartTime = departureTime.getText().toString().trim();

        allSelectedDays = ttvSelectedDays.getText().toString().trim();
        if(city.isEmpty() || city == null){
            mSearchCity.setError("city required");
        }else if(origin.isEmpty() || origin == null){
            mSearchOrigin.setError("origin required");
        }else if(destination.isEmpty() || destination == null){
            mSearchDestination.setError("destination required");
        }else if(pickedPickUpTime.isEmpty() || pickedPickUpTime == null){
            returnTime.setError("return time required");
        }else if(pickedDepartTime.isEmpty() || pickedDepartTime == null){
            departureTime.setError("departure time  required");
        }else if(allSelectedDays.isEmpty() || allSelectedDays == null){
            ttvSelectedDays.setError("select days required");
        }else {
            Map<String, Object> map = new HashMap<>();
            if(selectedItem == 0){
                CreateCustomSearch search = new CreateCustomSearch(city, pickedPickUpTime,destination, allSelectedDays,origin, pickedDepartTime );
                map.put("model",search);
                map.put("type","CreateOfferSearch");
                Log.i("WSX", "validateLocationData: search on offers");
            }else {
                CreateCustomSearch search = new CreateCustomSearch(city, pickedPickUpTime,destination, allSelectedDays,origin, pickedDepartTime );
                map.put("model",search);
                map.put("type","CreateRequestSearch");
                Log.i("WSX", "validateLocationData: search on search");
            }

            new ServerRequest(this).execute(map);
        }
    }

    public int getSelectedItem() {
        return selectedItem;
    }

    private void setOnClickListeners(View view){
        ttvSun.setOnClickListener(this);
        ttvSat.setOnClickListener(this);
        ttvFri.setOnClickListener(this);
        ttvThur.setOnClickListener(this);
        ttvWed.setOnClickListener(this);
        ttvTue.setOnClickListener(this);
        ttvMon.setOnClickListener(this);
        view.findViewById(R.id.btn_apply).setOnClickListener(this);
        view.findViewById(R.id.edt_depart).setOnClickListener(this);
        view.findViewById(R.id.edt_pickup).setOnClickListener(this);
    }
    private void disableFocus(boolean b, View view){
        view.findViewById(R.id.edt_pickup).setFocusable(b);
        view.findViewById(R.id.edt_depart).setFocusable(b);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onTaskCompleted() {

    }

    @Override
    public void onDataFetched(Map<String, Object> object) {
        if(getActivity() == null){
            return;
        }
        Log.i("WSX", "onDataFetched: "+object.get("object"));
        Log.i("WSX", "onDataFetched: "+object.get("response"));
        listener.onCompleteListener();
    }

    @Override
    public void onTaskFailed() {

    }

    @Override
    public void onTimePicked(Calendar time, int id) {

    }

    public void setTimesOnSearchFragment(Calendar time, int id){
        if(id == 0){
            EditText editText = view.findViewById(R.id.edt_depart);
            String AM_PM = String.valueOf(time.get(Calendar.AM_PM)).contains("0") ? "AM" : "PM";
            String minutes = String.valueOf(time.get(Calendar.MINUTE)).length() == 1 ? "0"+time.get(Calendar.MINUTE) : String.valueOf(time.get(Calendar.MINUTE));
            pickedDepartTime = time.get(Calendar.HOUR_OF_DAY) +" : "+minutes+ " " +AM_PM;
            editText.setText(pickedDepartTime);
            editText.setHint("");
        }else {
            EditText editText = view.findViewById(R.id.edt_pickup);
            String AM_PM = String.valueOf(time.get(Calendar.AM_PM)).contains("0") ? "AM" : "PM";
            String minutes = String.valueOf(time.get(Calendar.MINUTE)).length() == 1 ? "0"+time.get(Calendar.MINUTE) : String.valueOf(time.get(Calendar.MINUTE));
            pickedPickUpTime = time.get(Calendar.HOUR_OF_DAY) +" : "+minutes+ " " +AM_PM;
            editText.setText(pickedPickUpTime);
            editText.setHint("");
        }
        Toast.makeText(context, id+" id "+time.getTime(), Toast.LENGTH_SHORT).show();
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
    }

    public interface DialogFragmentInteface {
        void onCompleteListener();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lin_dropdown:
                //toggleVisibility();
                //dismiss();
                break;
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
            case R.id.btn_apply:
                validateLocationData(view);
                break;
            case R.id.edt_depart:
                showTimePickerDialog(0);
                break;
            case R.id.edt_pickup:
                showTimePickerDialog(1);
                break;

        }
    }
    public void setActivity(FragmentActivity activity){
        this.activity = activity;
    }

    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(activity);
        mGoogleApiClient.disconnect();
    }

    public void getCitySuggestions(View view) {
        try {
            mGoogleApiClient = new GoogleApiClient
                    .Builder(context)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .enableAutoManage(activity, SearchFragment.this)
                    .build();

            placeAutocompleteAdapter = new PlaceAutocompleteAdapter(context, mGoogleApiClient,
                    LAT_LNG_BOUNDS, null);

        }catch (Exception e){
            Log.i("WSX", "getCitySuggestions: "+e.getMessage());
        }

        mSearchOrigin = view.findViewById(R.id.edt_origin);
        mSearchDestination = view.findViewById(R.id.edt_destination);
        mSearchCity = view.findViewById(R.id.edt_city);
        mSearchCity.setText(MainActivity.getString("city"));
        mSearchOrigin.setAdapter(placeAutocompleteAdapter);
        mSearchCity.setAdapter(placeAutocompleteAdapter);
        mSearchDestination.setAdapter(placeAutocompleteAdapter);


    }
    private void toggleVisibility(){
        if(!visible){
            linFilters.setVisibility(View.VISIBLE);
            visible = true;
        }else {
            linFilters.setVisibility(View.GONE);
            visible = false;
            dismiss();
        }
    }
}

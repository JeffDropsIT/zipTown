package com.example.developer.ziptown.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.developer.ziptown.R;
import com.example.developer.ziptown.adapters.PlaceAutocompleteAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

import static com.example.developer.ziptown.activities.AddGenericPostActivity.LAT_LNG_BOUNDS;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {
    private TextView renewPasswrd;
    private EditText edtCurrentPasswrd, edtNewPassword;
    private boolean visibility;
    private AutoCompleteTextView mSearchCity;
    private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;


    private EditText  edtContact, edtFullName, edtCity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);






        visibility = false;
        renewPasswrd = findViewById(R.id.ttv_renew_password);
        edtCurrentPasswrd = findViewById(R.id.edt_password);
        edtNewPassword = findViewById(R.id.edt_password_new);
        getCitySuggestions();
        renewPasswrd.setOnClickListener(this);
        setToolBar();
        setSpinner();
    }

    private void setToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("Settings");
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    private void setSpinner(){
        Spinner spinner = findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.states_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ttv_renew_password:
               setVisibility();
        }
    }

    public void setVisibility() {

        if(!visibility){
            edtNewPassword.setVisibility(View.VISIBLE);
            edtCurrentPasswrd.setVisibility(View.VISIBLE);
            renewPasswrd.setText(R.string.hide_alert);
            this.visibility = true;
        }else {
            edtNewPassword.setVisibility(View.GONE);
            edtCurrentPasswrd.setVisibility(View.GONE);
            renewPasswrd.setText(R.string.show_alert);
            this.visibility = false;

        }
    }

    private void getCitySuggestions(){
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        placeAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient,
                LAT_LNG_BOUNDS, null);
        mSearchCity = findViewById(R.id.edt_city);
        mSearchCity.setAdapter(placeAutocompleteAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

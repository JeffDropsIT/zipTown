package com.example.developer.ziptown.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.developer.ziptown.R;
import com.example.developer.ziptown.adapters.PlaceAutocompleteAdapter;
import com.example.developer.ziptown.connection.ServerRequest;
import com.example.developer.ziptown.models.forms.UpdateUser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

import java.util.HashMap;
import java.util.Map;

import static com.example.developer.ziptown.activities.AddGenericPostActivity.LAT_LNG_BOUNDS;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, GoogleApiClient.OnConnectionFailedListener, ServerRequest.OnTaskCompleted {
    private TextView renewPasswrd;
    private EditText edtCurrentPasswrd, edtNewPassword;
    private boolean visibility;
    private AutoCompleteTextView mSearchCity;
    private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    String userType;
    Button buttonEditUser;

    private EditText  edtContact, edtFullName, edtCity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        buttonEditUser = findViewById(R.id.btn_save);
        buttonEditUser.setOnClickListener(this);

        edtCity = findViewById(R.id.edt_city);
        edtCity.setText(MainActivity.getString("city"));
        edtFullName = findViewById(R.id.edt_username);
        edtFullName.setText(MainActivity.getString("username"));
        edtContact = findViewById(R.id.edt_contact);
        edtContact.setText("+"+MainActivity.getString("contact"));
        edtContact.setEnabled(false);

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

    private void validateInputs(){
        String fullName, city, useType;
        fullName = edtFullName.getText().toString().trim();
        city = edtCity.getText().toString().trim();
        useType = userType.trim();

        if(fullName.isEmpty()){
            edtFullName.setText(MainActivity.getString("username"));
        }else if(city.isEmpty()){
            edtCity.setText(MainActivity.getString("city"));
        }else {
            UpdateUser user = new UpdateUser(fullName, city, useType);
            if(user.hasChanged()){
                //call api
                Map<String, Object> map = new HashMap<>();
                map.put("type", "UpdateUser");
                map.put("model", user);
                new ServerRequest(this).execute(map);
                Log.i("WSX", "validateInputs: call api");
            }else {
                Toast.makeText(this, "No changes made", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

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

        String type = MainActivity.getString("userType");
        int posti;
        if(type.contains("Driver")){
            posti = 1;
            Log.i("WSX", "setSpinner: selcted: "+posti+" item: "+(type == "Driver") + " "+type+" Driver");
        }else if(type.contains("Both")){
            posti = 0;
            Log.i("WSX", "setSpinner: selcted: "+posti+" item: "+(type == "Both") + " "+type+" Both");
        }else {
            posti = 2;
            Log.i("WSX", "setSpinner: selcted: "+posti+" item: "+(type == "Passenger") + " "+type+" Passenger");
        }


        spinner.setSelection(posti);

        spinner.setOnItemSelectedListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ttv_renew_password:
               setVisibility();
               break;
            case R.id.btn_save:
                validateInputs();
               break;
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
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(this);
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        userType = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onTaskCompleted() {
        Toast.makeText(this, "updated Details", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onDataFetched(Map<String, Object> object) {

    }

    @Override
    public void onTaskFailed() {
        Intent intent = new Intent(this, NetworkIssuesActivity.class);
        startActivity(intent);
    }
}

package com.example.developer.ziptown.activities;

import android.app.PendingIntent;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.developer.ziptown.R;
import com.example.developer.ziptown.adapters.PlaceAutocompleteAdapter;
import com.example.developer.ziptown.models.SignUpUser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

import static com.example.developer.ziptown.activities.AddGenericPostActivity.LAT_LNG_BOUNDS;

public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private AutoCompleteTextView mSearchCity;
    private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;

    String selectedItem;
    private Button btnSubmit;
    private EditText edtPassword, edtPasswordConfirm;
    private EditText edtContact, edtUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        edtPasswordConfirm = findViewById(R.id.edt_password_confirm);
        edtUsername = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_password);
        edtContact = findViewById(R.id.edt_contact);
        btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(this);

        getCitySuggestions();
        setToolBar();
        setSpinner();
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
    private void setToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("Sign Up");
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedItem = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_submit:
                collectAndVerify();
                break;
        }
    }
    private void validateInputs(){
        String password = edtPassword.getText().toString().replace(" ", "");
        String passwordConfirm = edtPasswordConfirm.getText().toString().replace(" ", "");
        String contact = edtContact.getText().toString().replace(" ", "");
        String name = edtUsername.getText().toString().replace(" ", "");
        String city = mSearchCity.getText().toString().replace(" ", "");
        if(password.isEmpty()){
            edtPassword.setError("Password required");

        }else if(passwordConfirm.isEmpty()){
            edtPasswordConfirm.setError("Password required");

        }else if(name.isEmpty()){
            edtUsername.setError("Name required");

        }else if(contact.isEmpty()) {
            edtContact.setError("Contact number required");

        }else if(city.isEmpty()) {
            mSearchCity.setError("city required");

        }else {
            if(checkPasswordMatch(password, passwordConfirm)){
                SignUpUser signUpUser = new SignUpUser(password, name, selectedItem, city, contact);
                //submit to api
                goHomeActivity();
                Toast.makeText(this, "Logging in as "+selectedItem, Toast.LENGTH_SHORT).show();
                //su
            }else{
                //throw error pass
                edtPassword.setError("Password doesn't match");
                edtPasswordConfirm.setError("Password doesn't match");
            }

        }

    }

    private void goHomeActivity(){
        // UserProfileObject userProfileObject
        Intent intent = new Intent(this, MainActivity.class);
        // Bundle bundle = new Bundle();
        // bundle.putSerializable("user", userProfileObject);
        //intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    private boolean checkPasswordMatch(String password, String passwordConfirm) {
        return password.equals(passwordConfirm);
    }

    private void collectAndVerify() {
        validateInputs();
    }
}

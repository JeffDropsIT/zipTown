package com.example.developer.ziptown;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private TextView renewPasswrd;
    private EditText edtCurrentPasswrd, edtNewPassword;
    private boolean visibility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        visibility = false;
        renewPasswrd = findViewById(R.id.ttv_renew_password);
        edtCurrentPasswrd = findViewById(R.id.edt_password);
        edtNewPassword = findViewById(R.id.edt_password_new);

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
        spinner.setOnItemClickListener(this);
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //handle picked state driver or passenger
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

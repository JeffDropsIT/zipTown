package com.example.developer.ziptown;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.developer.ziptown.fragments.TimePickerFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddGenericPostActivity extends AppCompatActivity implements View.OnClickListener, TimePickerFragment.TimePickedListener {
    private TextView ttvMon, ttvTue, ttvWed, ttvThur, ttvFri, ttvSat, ttvSun;
    private List<TextView> textViewArrayList = new ArrayList<>();
    private List<String> daysSelected = new ArrayList<>();
    private TextView ttvSelectedDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_entry_layout);


        ttvSelectedDays = findViewById(R.id.ttv_selected_days);
        ttvMon = findViewById(R.id.ttv_mon);
        ttvTue = findViewById(R.id.ttv_tue);
        ttvWed = findViewById(R.id.ttv_wed);
        ttvThur = findViewById(R.id.ttv_thur);
        ttvFri = findViewById(R.id.ttv_fri);
        ttvSat = findViewById(R.id.ttv_sat);
        ttvSun = findViewById(R.id.ttv_sun);

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
        findViewById(R.id.edt_pickup).setFocusable(false);
        findViewById(R.id.edt_depart).setFocusable(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ttv_mon:
                setSelected(ttvMon, "Mon");

                break;
            case R.id.ttv_tue:
                setSelected(ttvTue, "Tue");
                break;
            case R.id.ttv_wed:
                setSelected(ttvWed, "Wed");
                break;
            case R.id.ttv_thur:
                setSelected(ttvThur, "Thu");
                break;
            case R.id.ttv_fri:
                setSelected(ttvFri, "Fri");
                break;
            case R.id.ttv_sat:
                setSelected(ttvSat, "Sat");
                break;
            case R.id.ttv_sun:
                setSelected(ttvSun, "Sun");
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
    private void setSelected(TextView textView, String day){

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
            String pickedTime = time.get(Calendar.HOUR_OF_DAY) +" : "+time.get(Calendar.MINUTE)+ " " +AM_PM  ;
            editText.setText(pickedTime);
        }else {
            EditText editText = findViewById(R.id.edt_pickup);
            String AM_PM = String.valueOf(time.get(Calendar.AM_PM)).contains("0") ? "AM" : "PM";
            String pickedTime = time.get(Calendar.HOUR_OF_DAY) +" : "+time.get(Calendar.MINUTE)+ " " +AM_PM;
            editText.setText(pickedTime);
        }
        Toast.makeText(this, id+" id "+time.getTime(), Toast.LENGTH_SHORT).show();
    }
}

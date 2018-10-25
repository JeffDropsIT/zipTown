package com.example.developer.ziptown.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

public  class  TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private Activity mActivity;
    private int mId;
    private TimePickedListener mListener;

    public static TimePickerFragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt("picker_id", id);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);

        mActivity = context;
        try {
            mListener = (TimePickedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnTimeSetListener");
        }

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        mId = getArguments().getInt("picker_id");
        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(mActivity,  this, hour, minute,
                DateFormat.is24HourFormat(mActivity));
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        if(mListener != null){
            mListener.onTimePicked(c, mId);
        }
    }


    public static interface TimePickedListener {
        public void onTimePicked(Calendar time, int id);
    }
}

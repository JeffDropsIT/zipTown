package com.example.developer.ziptown.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.developer.ziptown.R;

public class SearchFragment  extends DialogFragment implements View.OnClickListener {
    private LinearLayout linFilters;
    private boolean visible;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        visible = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.search_dialog_fragment,container, false );
        linFilters = view.findViewById(R.id.lin_filters);
        view.findViewById(R.id.lin_dropdown).setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lin_dropdown:
                toggleVisibility();
                break;
        }
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

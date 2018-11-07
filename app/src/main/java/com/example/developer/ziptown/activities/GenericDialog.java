package com.example.developer.ziptown.activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.developer.ziptown.R;

public class GenericDialog extends DialogFragment {
    private Activity activity;
    private String error;
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.generic_alert_dialog_layout, container, false);
        if(error != null){
            TextView response = view.findViewById(R.id.ttv_msg_res);
            response.setText(error);
            TextView title = view.findViewById(R.id.ttv_msg_title);
            title.setText("Error");
        }
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(activity != null){
                    dismiss();
                    if(error == null){
                        activity.finish();
                    }

                }
            }
        });
        return view;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setError(String error) {
        this.error = error;
        if(view != null){
            TextView response = view.findViewById(R.id.ttv_msg_res);
            response.setText(error);
            TextView title = view.findViewById(R.id.ttv_msg_title);
            title.setText("Error");
        }

    }
}

package com.example.developer.ziptown.fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.developer.ziptown.R;

public class VerificationCodeFragment extends DialogFragment implements View.OnClickListener {

    View view;
    EditText edtCode;
    Context context;
    DialogCompleteListener listener;

    public void setContextListener(Context context, DialogCompleteListener listener){
        this.context = context;
        this.listener = listener;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.verification_code_layout, container, false);

        edtCode = view.findViewById(R.id.edt_code);
        view.findViewById(R.id.btn_send_code).setOnClickListener(this);

        return view;
    }


    public void close(){
        dismiss();
    }

    private void sendCode(){
        String code = edtCode.getText().toString().trim();
        if(code.isEmpty()){
            edtCode.setError("code required");
            return;
        }
        listener.onCodeReceived(code);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_send_code:
                sendCode();
                break;
        }
    }

    public interface DialogCompleteListener{
        void onCodeReceived(String pcode);
    }
}

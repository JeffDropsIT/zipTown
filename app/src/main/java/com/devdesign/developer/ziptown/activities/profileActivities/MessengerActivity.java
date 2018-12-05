package com.devdesign.developer.ziptown.activities.profileActivities;

import android.app.ActionBar;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.devdesign.developer.ziptown.R;
import com.devdesign.developer.ziptown.activities.MainActivity;
import com.devdesign.developer.ziptown.adapters.MessageAdapter;
import com.devdesign.developer.ziptown.models.Message;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessengerActivity extends AppCompatActivity implements View.OnClickListener {
    private List<Message> messageList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    public static final String TAG = "WSX";
    EditText edtTypeBox;
    CircleImageView btnSend;
    public static boolean exceeded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messanger);
        setToolBar();

        btnSend = findViewById(R.id.btn_send);
        btnSend.setOnClickListener(this);
        edtTypeBox = findViewById(R.id.edt_type_box);
        recyclerView = findViewById(R.id.rcl_messages);
        messageAdapter = new MessageAdapter(messageList, getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(messageAdapter);


        generateMessages();
        //typeBoxEffects();
        //findViewById(R.id.edt_type_box).setOnClickListener(this);
    }

    private void generateMessages() {
        boolean isSender;
        for(int i = 0; i < 30;  i++){
            if(i % 2 == 0)
            {
                isSender = true;
            }else {
                isSender = false;
            }

            Message message = new Message(MainActivity.getString("userId"), MainActivity.getString("userId") , "15:35" , getResources().getString(R.string.dummy_text),"sent", "http://wilkinsonschool.org/wp-content/uploads/2018/10/user-default-grey.png", MainActivity.getString("token"), isSender);

            messageList.add(message);
            Log.i(TAG, "generateMessages: "+i);
        }

        messageAdapter.notifyDataSetChanged();
    }

    private void setToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_back_arrow);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.call_menu, menu);
        return true;
    }
    private void typeBoxEffects() {
        KeyboardVisibilityEvent.setEventListener(this, new KeyboardVisibilityEventListener() {
            @Override
            public void onVisibilityChanged(boolean isOpen) {
                if(isOpen){
                    setMargin(0,0,0,0);
                    Log.i(TAG, "onVisibilityChanged: open");
                }else {
                    setMargin(convertDpIntoPx(getApplicationContext(),15),0,convertDpIntoPx(getApplicationContext(), 15),convertDpIntoPx(getApplicationContext(), 15));
                    Log.i(TAG, "onVisibilityChanged: not open");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edt_type_box:
                //setMargin();
                break;
            case R.id.btn_send:
                Log.i(TAG, "onClick: pressed");
                sendMessage();
                break;
        }
    }

    private void sendMessage() {
        String time = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+ ":"+Calendar.getInstance().get(Calendar.MINUTE);
        if(edtTypeBox.getText().toString().trim().isEmpty())
            return;
        Log.i(TAG, "onClick: pressed message");
        Message message = new Message(MainActivity.getString("userId"), MainActivity.getString("userId") , time , edtTypeBox.getText().toString(),"sent", "http://wilkinsonschool.org/wp-content/uploads/2018/10/user-default-grey.png", MainActivity.getString("token"), true);
        messageList.add(message);
        messageAdapter.notifyDataSetChanged();
        clearTypeBox();
        recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
    }

    private void clearTypeBox() {
        edtTypeBox.getText().clear();
        edtTypeBox.clearFocus();
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    private int convertDpIntoPx(Context mContext, float yourdpmeasure) {
        if (mContext == null) {
            return 0;
        }
        Resources r = mContext.getResources();
        int px = (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, yourdpmeasure, r.getDisplayMetrics());
        return px;
    }
    private void setMargin(int left, int top, int right, int bottom) {
        EditText editText = findViewById(R.id.edt_type_box);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, convertDpIntoPx(getApplicationContext(), 46) );
        lp.setMargins(left, top, right, bottom);
        editText.setLayoutParams(lp);
        Log.i(TAG, "removeMargin: ");
    }

}

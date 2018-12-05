package com.devdesign.developer.ziptown.activities.profileActivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.devdesign.developer.ziptown.R;
import com.devdesign.developer.ziptown.activities.MainActivity;
import com.devdesign.developer.ziptown.adapters.ChatsAdapter;
import com.devdesign.developer.ziptown.adapters.MessageAdapter;
import com.devdesign.developer.ziptown.models.Chat;
import com.devdesign.developer.ziptown.models.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatsActivity extends AppCompatActivity {
    private List<Chat> chatList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ChatsAdapter chatsAdapter;
    public static final String TAG = "WSX";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);
        setToolBar();

        recyclerView = findViewById(R.id.rcl_chats);
        chatsAdapter = new ChatsAdapter(chatList, getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(chatsAdapter);


        generateChats();
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
    private void generateChats() {

        for(int i = 0; i < 10; i++){
            Chat chat = new Chat("Phumzile Mathonsi", 64, "We love coding","15:38", MainActivity.getString("token"), "");
            chatList.add(chat);
        }
        chatsAdapter.notifyDataSetChanged();
    }
}

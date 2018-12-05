package com.devdesign.developer.ziptown.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.devdesign.developer.ziptown.R;
import com.devdesign.developer.ziptown.models.Chat;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatViewHolder> {
    private List<Chat> chatList;
    private Context context;

    public ChatsAdapter(List<Chat> chatList, Context context){
        this.context = context;
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.template_chat_layout, parent, false);
        return new ChatsAdapter.ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        final Chat chat = chatList.get(position);
        holder.ttvLastMessage.setText(chat.getLastMessage());
        holder.ttvUsername.setText(chat.getUsername());
        holder.ttvLastSeen.setText(chat.getLastSeen());

        holder.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, chat.getUsername(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        public TextView ttvUsername, ttvLastMessage, ttvLastSeen;
        public CircleImageView profileImage;
        public ChatViewHolder(View itemView) {
            super(itemView);
            ttvUsername = itemView.findViewById(R.id.ttv_username);
            ttvLastMessage = itemView.findViewById(R.id.ttv_last_msg);
            ttvLastSeen = itemView.findViewById(R.id.ttv_last_seen);
            profileImage = itemView.findViewById(R.id.img_profile);

        }
    }
}

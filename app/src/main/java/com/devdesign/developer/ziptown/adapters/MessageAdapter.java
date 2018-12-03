package com.devdesign.developer.ziptown.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devdesign.developer.ziptown.R;
import com.devdesign.developer.ziptown.models.Message;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Message> messageList;
    private Context context;

    public MessageAdapter(List<Message> messageList, Context context){
        this.context = context;
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_layout, parent, false);
        return new MessageAdapter.MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);
        if(message.isSender()){
            holder.receive.setVisibility(View.GONE);
            holder.sent.setVisibility(View.VISIBLE);
            holder.ttvSentMessage.setText(message.getPayload());
            holder.ttvSentStatus.setText(message.getStatus());
            holder.ttvSentTime.setText(message.getCreated());
        }else {
            holder.receive.setVisibility(View.VISIBLE);
            holder.sent.setVisibility(View.GONE);
            holder.ttvReceivedMessage.setText(message.getPayload());
            holder.ttvReceivedStatus.setText(message.getStatus());
            holder.ttvReceivedTime.setText(message.getCreated());
//            if(message.getProfileBitmap() != null){
//                holder.profileImage.setImageBitmap(message.getProfileBitmap());
//            }

        }

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }


    public class MessageViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout sent, receive;
        public TextView ttvSentMessage, ttvReceivedMessage, ttvSentTime, ttvReceivedTime,
                ttvSentStatus, ttvReceivedStatus;
        public CircleImageView profileImage;
        public MessageViewHolder(View itemView) {
            super(itemView);
            sent = itemView.findViewById(R.id.sent);
            receive = itemView.findViewById(R.id.received);
            ttvReceivedMessage = itemView.findViewById(R.id.ttv_message_received);
            ttvSentMessage = itemView.findViewById(R.id.ttv_message_sent);
            ttvSentTime = itemView.findViewById(R.id.ttv_time_sent);
            ttvReceivedTime = itemView.findViewById(R.id.ttv_time_received);
            ttvSentStatus = itemView.findViewById(R.id.ttv_read_status_sent);
            ttvReceivedStatus = itemView.findViewById(R.id.ttv_read_status_received);
            profileImage = itemView.findViewById(R.id.img_profile);

        }
    }
}

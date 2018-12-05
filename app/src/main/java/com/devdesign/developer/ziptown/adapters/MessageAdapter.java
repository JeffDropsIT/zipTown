package com.devdesign.developer.ziptown.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devdesign.developer.ziptown.R;
import com.devdesign.developer.ziptown.models.Message;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.devdesign.developer.ziptown.activities.profileActivities.MessengerActivity.TAG;
import static com.devdesign.developer.ziptown.activities.profileActivities.MessengerActivity.exceeded;

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

    private void formatTTV(TextView textView, int drawable, int drawableRenew){
        if (textView.getText().toString().split(" ").length >= 7 || textView.getText().toString().length() >= 35){
            Log.i(TAG, "formatTTV: "+textView.getText().toString().length());
            exceeded = true;
        }else {
            exceeded = false;
        }
        if(exceeded){
            Log.i(TAG, "formatTTV: true ");
            textView.setBackground(context.getResources().getDrawable(drawable));
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) textView.getLayoutParams();
            params.width = convertDpIntoPx(context, 250);
            textView.setLayoutParams(params);
        }else {
            Log.i(TAG, "formatTTV: false");
            textView.setBackground(context.getResources().getDrawable(drawableRenew));
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) textView.getLayoutParams();
            params.width = params.WRAP_CONTENT;
            textView.setLayoutParams(params);
        }
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
    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);

        if(message.isSender()){
            holder.receive.setVisibility(View.GONE);
            holder.sent.setVisibility(View.VISIBLE);

            holder.ttvSentMessage.setText(message.getMessage());
            holder.ttvSentStatus.setText(message.getStatus());
            holder.ttvSentTime.setText(message.getTimeSent());
            formatTTV(holder.ttvSentMessage, R.drawable.messenger_textview_shape_more, R.drawable.messenger_textview_shape);
            //formatTTV(holder.ttvReceivedMessage, R.drawable.messenger_textview_received_shape,  R.drawable.messenger_textview_received_shape_more );
        }else {
            holder.receive.setVisibility(View.VISIBLE);
            holder.sent.setVisibility(View.GONE);
            //formatTTV(holder.ttvSentMessage, R.drawable.messenger_textview_shape, R.drawable.messenger_textview_shape_more);
            holder.ttvReceivedMessage.setText(message.getMessage());
            holder.ttvReceivedStatus.setText(message.getStatus());
            holder.ttvReceivedTime.setText(message.getTimeSent());
            formatTTV(holder.ttvReceivedMessage, R.drawable.messenger_textview_received_shape_more,  R.drawable.messenger_textview_received_shape );
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

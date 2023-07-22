package com.example.whatsappclone.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.Models.Message;
import com.example.whatsappclone.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class chatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<Message> messages;
    Context context;
    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;
    String recId;

    public chatAdapter(ArrayList<Message> messages, Context context, String recieveId) {
        this.messages = messages;
        this.context = context;
        this.recId = recieveId;
    }

    public chatAdapter(ArrayList<Message> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == SENDER_VIEW_TYPE) {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_sender, parent, false);
            return new SenderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_reciever, parent, false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getuId().equals(FirebaseAuth.getInstance().getUid())) {
            return SENDER_VIEW_TYPE;
        } else {
            return RECEIVER_VIEW_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);

        if (holder.getItemViewType() == SENDER_VIEW_TYPE) {
            SenderViewHolder senderViewHolder = (SenderViewHolder) holder;
            senderViewHolder.sendmssg.setText(message.getMessage());
            senderViewHolder.sendtime.setText(String.valueOf(message.getTimestamp()));
        } else {
            ReceiverViewHolder receiverViewHolder = (ReceiverViewHolder) holder;
            receiverViewHolder.recievemssg.setText(message.getMessage());
            receiverViewHolder.recievetime.setText(String.valueOf(message.getTimestamp()));
        }
    }


    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder {

        TextView recievemssg, recievetime;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            recievemssg = itemView.findViewById(R.id.recieverText);
            recievetime = itemView.findViewById(R.id.receiverTime);
        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {

        TextView sendmssg, sendtime;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            sendmssg = itemView.findViewById(R.id.senderText);
            sendtime = itemView.findViewById(R.id.senderTime);
        }
    }
}


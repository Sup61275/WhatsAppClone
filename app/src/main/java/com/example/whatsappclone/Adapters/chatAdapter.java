package com.example.whatsappclone.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.Models.Message;
import com.example.whatsappclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    private OnMessageLongClickListener onMessageLongClickListener;

    public void setOnMessageLongClickListener(OnMessageLongClickListener listener) {
        this.onMessageLongClickListener = listener;
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

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int clickedPosition = holder.getAdapterPosition();
                if (clickedPosition != RecyclerView.NO_POSITION) {
                    Message clickedMessage = messages.get(clickedPosition);
                    if (clickedMessage.getuId().equals(FirebaseAuth.getInstance().getUid())) {
                        // If the message belongs to the current user, show delete options
                        showDeleteOptions(clickedMessage, clickedPosition);
                    }
                }
                return true;
            }
        });


        if (holder.getItemViewType() == SENDER_VIEW_TYPE) {
            ((SenderViewHolder) holder).sendmssg.setText(message.getMessage());
        } else {
            ((ReceiverViewHolder) holder).recievemssg.setText(message.getMessage());
        }
    }

    // Show the AlertDialog with delete options
    private void showDeleteOptions(Message message, int position) {
        CharSequence[] options = {"Delete for me", "Delete for everyone", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                // Delete for me
                                deleteMessageForMe(message, position);
                                break;
                            case 1:
                                // Delete for everyone
                                deleteMessageForEveryone(message, position);
                                break;
                            case 2:
                                // Cancel, do nothing
                                break;
                        }
                    }
                })
                .show();
    }

    // Backend implementation for deleting a message for the current user
    private void deleteMessageForMe(Message message, int position) {
        String messageId = message.getMessageId();
        String currentUserUid = FirebaseAuth.getInstance().getUid();

        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("chats").child(currentUserUid);
        messagesRef.child(messageId).removeValue();

        messages.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, messages.size());
    }

    // Backend implementation for deleting a message for everyone
    private void deleteMessageForEveryone(Message message, int position) {
        String messageId = message.getMessageId();
        String currentUserUid = FirebaseAuth.getInstance().getUid();
        String messageSenderUid = message.getuId();

        if (currentUserUid.equals(messageSenderUid)) {
            DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("chats");
            messagesRef.child(message.getuId()).child(messageId).removeValue();
            messagesRef.child(recId).child(messageId).removeValue();

            messages.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, messages.size());
        } else {
            Toast.makeText(context, "You don't have permission to delete this message for everyone.", Toast.LENGTH_SHORT).show();
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

    public interface OnMessageLongClickListener {
        void onMessageLongClick(Message message, int position);
    }
}

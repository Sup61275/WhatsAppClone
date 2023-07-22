package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.whatsappclone.Adapters.chatAdapter;
import com.example.whatsappclone.Models.Message;
import com.example.whatsappclone.databinding.ActivityChatPageBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Context;

import java.util.ArrayList;
import java.util.Date;

public class chatPage extends AppCompatActivity {

    ActivityChatPageBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    chatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        final String senderId = auth.getUid();
        String receiveId = getIntent().getStringExtra("user");
        String userName = getIntent().getStringExtra("userName");
        String profilePic = getIntent().getStringExtra("profilePic");

        binding.userName.setText(userName);
        Glide.with(this)
                .load(profilePic)
                .placeholder(R.drawable.avatar3) // Placeholder image while loading the profilePic
                .into(binding.profileImage);

        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(chatPage.this, MainActivity.class);
                startActivity(intent);
            }
        });

        final ArrayList<Message> messages = new ArrayList<>();
        chatAdapter = new chatAdapter(messages, this, receiveId);

        binding.chatRecyclerView.setAdapter(chatAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatRecyclerView.setLayoutManager(layoutManager);

        final String senderRoom = senderId + receiveId;
        final String receiverRoom = receiveId + senderId;

        database.getReference().child("chats").child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messages.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Message model = snapshot1.getValue(Message.class);
                            model.setMessageId(snapshot1.getKey());
                            messages.add(model);
                        }
                        chatAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.sendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = binding.sendmessage1.getText().toString();
                if (!message.isEmpty()) {
                    Message model = new Message(senderId, message);
                    model.setTimestamp(new Date().getTime());
                    binding.sendmessage1.setText("");

                    database.getReference().child("chats").child(senderRoom).push()
                            .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    // Message sent successfully
                                }
                            });
                }
            }
        });

        // Update the existing code for the chatAdapter click listeners
        chatAdapter.setOnMessageLongClickListener(new chatAdapter.OnMessageLongClickListener() {
            @Override
            public void onMessageLongClick(Message message, int position) {
                showDeleteOptions(message, senderRoom, receiverRoom, position);
            }
        });
    }


    // Inside your chatPage class, create a method to store the IDs of deleted messages
    private void saveDeletedMessageId(String messageId) {
        // Implement SharedPreferences or SQLite to store the deleted message ID
        // For example, using SharedPreferences:
        SharedPreferences preferences = getSharedPreferences("DeletedMessages", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(messageId, true);
        editor.apply();
    }


    // Show the AlertDialog with delete options
    private void showDeleteOptions(Message message, String senderRoom, String receiverRoom, int position) {
        CharSequence[] options = {"Delete for me", "Delete for everyone", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                // Delete for me
                                deleteMessageForMe(message, senderRoom, position);
                                break;
                            case 1:
                                // Delete for everyone
                                deleteMessageForEveryone(message, senderRoom, receiverRoom, position);
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
    private void deleteMessageForMe(Message message, String senderRoom, int position) {
        String messageId = message.getMessageId();
        String currentUserUid = FirebaseAuth.getInstance().getUid();

        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("chats").child(senderRoom);
        messagesRef.child(messageId).removeValue();

        chatAdapter.notifyItemRemoved(position);
        chatAdapter.notifyItemRangeChanged(position, chatAdapter.getItemCount());
    }

    // Backend implementation for deleting a message for everyone
    private void deleteMessageForEveryone(Message message, String senderRoom, String receiverRoom, int position) {
        String messageId = message.getMessageId();
        String currentUserUid = FirebaseAuth.getInstance().getUid();
        String messageSenderUid = message.getuId();

        if (currentUserUid.equals(messageSenderUid)) {
            DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("chats");
            messagesRef.child(senderRoom).child(messageId).removeValue();
            messagesRef.child(receiverRoom).child(messageId).removeValue();

            chatAdapter.notifyItemRemoved(position);
            chatAdapter.notifyItemRangeChanged(position, chatAdapter.getItemCount());
        } else {
            // Show an error message or toast indicating the current user doesn't have permission to delete for everyone
            Toast.makeText(this, "You don't have permission to delete this message for everyone.", Toast.LENGTH_SHORT).show();
        }
    }
}

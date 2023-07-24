package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.whatsappclone.Adapters.chatAdapter;
import com.example.whatsappclone.Models.Message;
import com.example.whatsappclone.databinding.ActivityChatPageBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class chatPage extends AppCompatActivity {

    ActivityChatPageBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;

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
        // Add this line after getting the receiveId
        String senderName = auth.getCurrentUser().getDisplayName(); // or retrieve sender's name from wherever it's stored
      final  chatAdapter chatAdapter = new chatAdapter(messages, this, receiveId, senderId, senderName);





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
    }
}
package com.example.whatsappclone.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whatsappclone.Models.User;
import com.example.whatsappclone.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder>{

    ArrayList<User>list;

    public UsersAdapter(ArrayList<User> list, Context context) {
        this.list = list;
        this.context = context;
    }

    Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate((R.layout.users_show),parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = list.get(position);
        // Populate the user profile image
        Glide.with(context)
                .load(user.getProfilePic())
                .placeholder(R.drawable.avatar3)
                .into(holder.profileImage);

        // Set the user name
        holder.UserName.setText(user.getUserName());

        // Retrieve the last message and its read status from Firebase
        DatabaseReference lastMessageRef = FirebaseDatabase.getInstance().getReference()
                .child("lastMessages")
                .child(user.getUserid());

        lastMessageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String lastMessage = snapshot.child("message").getValue(String.class);
                    boolean isRead = snapshot.child("isRead").getValue(Boolean.class);

                    // Set the last message text
                    holder.LastMessage.setText(lastMessage);

                    // Set the tick icon based on the read status
                    if (isRead) {
                        holder.TickImage.setImageResource(R.drawable.blue_tick);
                    } else {
                        holder.TickImage.setImageResource(R.drawable.grey_tick);
                    }
                } else {
                    // If no last message exists, set appropriate defaults
                    holder.LastMessage.setText("");
                    holder.TickImage.setImageResource(R.drawable.grey_tick);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled if needed
            }
        });

        // Set the last message date (you need to retrieve it from Firebase as well)
        holder.Time.setText("");
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView profileImage,TickImage;
        TextView LastMessage,UserName,Time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage=itemView.findViewById(R.id.profile_image);
            TickImage=itemView.findViewById(R.id.lastMessageTickIcon);
            LastMessage=itemView.findViewById(R.id.lastMessage);
            UserName=itemView.findViewById(R.id.userNameList);
            Time=itemView.findViewById(R.id.LastMessageDate);
        }
    }
}

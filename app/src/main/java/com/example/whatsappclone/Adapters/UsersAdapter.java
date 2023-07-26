package com.example.whatsappclone.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.whatsappclone.Models.User;
import com.example.whatsappclone.R;
import com.example.whatsappclone.chatPage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

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
      View view=LayoutInflater.from(context).inflate(R.layout.users_show,parent,false);
      return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
 User users=list.get(position);
// Picasso.get().load(users.getProfilePic()).placeholder(R.drawable.avatar3).into(holder.image);
//
        // Check if the image path is not empty or null before loading with Picasso
        if (users.getProfilePic()!= null && !users.getProfilePic().isEmpty()) {
            Picasso.get().load(users.getProfilePic()).into(holder.image);
        } else {
            // If the image path is invalid, you can set a default image or hide the ImageView
            holder.image.setImageResource(R.drawable.avatar);
        }
        holder.userName.setText(users.getUserName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the chatPage activity when the user's list item is clicked
                Intent intent = new Intent(v.getContext(), chatPage.class);
                intent.putExtra("userId", users.getUserId()); // Replace 'getUserId()' with the actual method to get the user's ID
                intent.putExtra("userName", users.getUserName()); // Replace 'getUserName()' with the actual method to get the user's name
                intent.putExtra("profilePic", users.getProfilePic()); // Replace 'getProfilePic()' with the actual method to get the user's profile picture
                v.getContext().startActivity(intent);
            }
        });

        // Other data binding and view updates for the user's information in the RecyclerView item
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
  ImageView image;
  TextView userName, lastMessage;

      public ViewHolder(@NonNull View itemView) {
          super(itemView);
          image=itemView.findViewById(R.id.profile_image);
          userName=itemView.findViewById(R.id.userNameList);
          lastMessage=itemView.findViewById(R.id.lastMessage);

      }
  }
}
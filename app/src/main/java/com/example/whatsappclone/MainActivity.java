package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.whatsappclone.Adapters.FragmentsAdapter;
import com.example.whatsappclone.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ActivityMainBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();

        // Set the custom toolbar as the action bar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        binding.viewPager.setAdapter(new FragmentsAdapter(getSupportFragmentManager()));
        binding.tabLayout.setupWithViewPager(binding.viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu); // Use the menu.xml layout for the menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.settings) {
            // Handle the "Settings" menu item click
            Toast.makeText(this, "Settings is clicked", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.group_chat) {
            // Handle the "Group Chat" menu item click
            Toast.makeText(this, "Group Chat is Clicked", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.starred_message) {
            // Handle the "Starred Message" menu item click
            Toast.makeText(this, "Starred Message is Clicked", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.logout) {
            // Handle the "Logout" menu item click
            auth.signOut();
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(intent);
            finish(); // Close the main activity
            Toast.makeText(this, "Logout successful", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}







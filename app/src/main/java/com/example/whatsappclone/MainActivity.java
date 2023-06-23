package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.whatsappclone.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        final int SETTINGS_MENU_ID = 1;
        final int GROUP_CHAT_MENU_ID = 2;
        final int STARRED_MESSAGE_MENU_ID = 3;
        final int LOGOUT_MENU_ID = 4;

        switch (itemId) {
            case SETTINGS_MENU_ID:
                // Handle the "Settings" menu item click
                openSettingsActivity();
                return true;
            case GROUP_CHAT_MENU_ID:
                // Handle the "Group Chat" menu item click
                openGroupChatActivity();
                return true;
            case STARRED_MESSAGE_MENU_ID:
                // Handle the "Starred Message" menu item click
                openStarredMessageActivity();
                return true;
            case LOGOUT_MENU_ID:
                // Handle the "Logout" menu item click
                logoutUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logoutUser() {
        auth.signOut();
        Intent intent = new Intent(MainActivity.this, SignInActivity.class);
        startActivity(intent);
        finish(); // Close the main activity
        Toast.makeText(this, "Logout successful", Toast.LENGTH_SHORT).show();
    }

    private void openSettingsActivity() {
        // Implement the logic to open the Settings activity
    }

    private void openGroupChatActivity() {
        // Implement the logic to open the Group Chat activity
    }

    private void openStarredMessageActivity() {
        // Implement the logic to open the Starred Message activity
    }

}

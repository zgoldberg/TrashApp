package edu.upenn.cis350.project;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        makeButtons();

        GlobalInfo infoOuter = GlobalInfo.getInstance();
        Log.i("Variable Value: ", "In HomeActivity, variable is: " + infoOuter.getCreateAccountUsername());

    }

    private void makeButtons() {

        Button userButton = (Button) findViewById(R.id.userButton);
        Button postButton = (Button) findViewById(R.id.post_button);
        Button searchPostButton = (Button) findViewById(R.id.search_post_button);
        Button searchUserButton = (Button) findViewById(R.id.search_user_button);
        Button darkButton = (Button) findViewById(R.id.dark);
        Button mapButton = (Button) findViewById(R.id.mapp);
        Button feedButton = (Button) findViewById(R.id.feed);
        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAcc();
            }
        });
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post();
            }
        });
        searchPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPost();
            }
        });
        darkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                darkMode();
            }
        });
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapSet();
            }
        });
        searchUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchUser();
            }
        });
        feedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feed();
            }
        });

    }
    private void mapSet(){
        Intent intent = new Intent(this, MapsMarkerActivity.class);
        startActivity(intent);
    }
    private void darkMode() {
        int currentNightMode = getResources().getConfiguration().uiMode
            & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
                break;
        }
    }
    private void userAcc() {
        Intent intent = new Intent(this, ViewAccountActivity.class);
        startActivity(intent);
    }

    private void post() {
        Intent intent = new Intent(this, CreatePostActivity.class);
        startActivity(intent);
    }

    private void searchPost() {
        Intent intent = new Intent(this, SearchPostActivity.class);
        startActivity(intent);
    }

    private void searchUser() {
        Intent intent = new Intent(this, SearchUserActivity.class);
        startActivity(intent);
    }

    private void feed() {
        Intent intent = new Intent(this, FeedActivity.class);
        startActivity(intent);
    }

}

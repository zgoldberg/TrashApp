package edu.upenn.cis350.project;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class AdminHomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        makeButtons();

    }

    private void makeButtons() {

        Button userButton = (Button) findViewById(R.id.userButton);
        Button postButton = (Button) findViewById(R.id.post_button);
        Button searchPostButton = (Button) findViewById(R.id.search_post_button);
        Button darkButton = (Button) findViewById(R.id.dark);
        Button mapButton = (Button) findViewById(R.id.mapp);
        Button banButton = (Button) findViewById(R.id.ban);
        Button statsButton = (Button) findViewById(R.id.admin_stats);
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
        banButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                banner();
            }
        });
        statsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adminStats();
            }
        });

    }
    private void adminStats(){
        Intent intent = new Intent(this, AdminStatsActivity.class);
        startActivity(intent);
    }
    private void banner(){
        Intent intent = new Intent(this, BanActivity.class);
        startActivity(intent);
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
}

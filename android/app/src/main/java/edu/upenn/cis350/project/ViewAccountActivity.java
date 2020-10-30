package edu.upenn.cis350.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_account);

        GlobalInfo info = GlobalInfo.getInstance();
        User currentSearchedUser = info.getLoggedInUser();

        TextView usernameField = (TextView) findViewById(R.id.username);
        TextView followersField = (TextView) findViewById(R.id.followers);
        TextView followingField = (TextView) findViewById(R.id.following);
        TextView karmaField = (TextView) findViewById(R.id.karma);
        ImageView pictureField = (ImageView) findViewById(R.id.profilePicture);

        usernameField.setText(info.getCreateAccountUsername());
       // followersField.setText(String.format("%d", currentSearchedUser.getFollowers()));
      //  followingField.setText(String.format("%d", currentSearchedUser.getFollowing()));
       /// karmaField.setText(String.format("%d", currentSearchedUser.getKarma()));
        // need to set photo here too

        makeButtons();

        GlobalInfo infoOuter = GlobalInfo.getInstance();
        Log.i("Variable Value: ", "In ViewAccountActivity, variable is: " + infoOuter.getCreateAccountUsername());

    }
    private void makeButtons() {

        Button postButton = (Button) findViewById(R.id.see_posts_button);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPosts();
            }
        });

        Button editButton = (Button) findViewById(R.id.edit_account_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAccountPress();
            }
        });

        Button viewButton = (Button) findViewById(R.id.edit_account_button);
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPostsPress();
            }
        });

        Button statsButton = (Button) findViewById(R.id.view_stats_button);
        statsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myStats();
            }
        });

    }

    public  void myPosts() {
        Intent intent = new Intent(this, PostsActivity.class);
        startActivity(intent);

    }

    public void myStats() {
        Intent intent = new Intent(this, StatsActivity.class);
        startActivity(intent);
    }


    private void editAccountPress() {
        //TODO edit account
    }

    private void viewPostsPress() {
        //TODO view posts. Just go to search with query

    }

}

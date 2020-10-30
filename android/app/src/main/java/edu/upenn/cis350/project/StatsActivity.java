package edu.upenn.cis350.project;

import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class StatsActivity extends AppCompatActivity {

    private Stats stats;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        updateStats();

        header();
        values1();
        values2();
        values3();
        values4();

    }

    public void updateStats() {
        username = GlobalInfo.getInstance().getCreateAccountUsername();
        Log.i("Variable Value: ", "In StatsActivity, variable is: " + username);

        stats = ParseStats.getUserCurrentStats();
    }

    public void header() {
        TextView header = (TextView) findViewById(R.id.usernameStats);
        header.setText(username);
        // Update this image below:
        ImageView pictureField = (ImageView) findViewById(R.id.profilePictureStats);
    }

    public void labels1() {

    }

    public void values1() {
        TextView karma = (TextView) findViewById(R.id.karmaStats);
        karma.setText("" + stats.getKarma());
    }

    public void labels2() {

    }

    public void values2() {
        TextView posts = (TextView) findViewById(R.id.posts);
        posts.setText(""+ stats.posts());
        TextView tags = (TextView) findViewById(R.id.tags);
        tags.setText(""+ stats.trendingTags());
        TextView comments = (TextView) findViewById(R.id.comments);
        comments.setText(""+ stats.getCommentsPosted());
    }

    public void labels3() {

    }

    public void values3() {
        TextView totalVotes = (TextView) findViewById(R.id.votes);
        totalVotes.setText(""+ (stats.getDownVotes() + stats.getUpVotes()));
        TextView upVotes = (TextView) findViewById(R.id.upvotes);
        upVotes.setText(""+ stats.getUpVotes());
        TextView downVotes = (TextView) findViewById(R.id.downvotes);
        downVotes.setText(""+ stats.getDownVotes());
    }

    public void labels4() {

    }

    public void values4() {
        TextView vpt = (TextView) findViewById(R.id.votes_per_tag);
        vpt.setText(stats.avgVotesString());
        TextView uvp = (TextView) findViewById(R.id.upvote_percentage);
        uvp.setText(stats.percentString() + "%");
//        TextView totalTags = (TextView) findViewById(R.id.total);
//        totalTags.setText(""+ stats.trendingTags());
    }



}

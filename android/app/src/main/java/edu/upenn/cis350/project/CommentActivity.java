package edu.upenn.cis350.project;

import android.content.Intent;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class CommentActivity extends AppCompatActivity {


    private HttpURLConnection conn;

    private final String PATH_DB = "http://10.0.2.2:3000";
    private final String PATH_POSTS = "/getComment?id=";

    private List<String> tags;
    private String area;

    private Geocoder geocoder;

    List<JSONObject> commentJson;
    private List<PostData> posts;


    private EditText editTextTags;
    private EditText editTextArea;

    private Spinner spinnerSort;

    private RecyclerView recycler;
    private RecyclerAdapterPost recyclerAdapter;
    private RecyclerView.LayoutManager recyclerLayoutManager;
    Intent intent2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        initViews();
        postPosts();
        makeButtons();
        intent2 = new Intent(this, CommentActivity.class);

    }
    private void makeButtons() {

        Button commentButton = (Button) findViewById(R.id.comment_button);
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createComment();
            }
        });

        Button tagButton = (Button) findViewById(R.id.tag_button);
        tagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagUsers();
            }
        });

        Button upButton = (Button) findViewById(R.id.upvote_button);
        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upVote();
            }
        });

        Button downButton = (Button) findViewById(R.id.downvote_button);
        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downVote();
            }
        });

    }

    private void upVote() {
        EditText textField = (EditText) findViewById(R.id.comment_box);
        String taggedUser = textField.getText().toString(); // Username of User tagged
        User user = ParseStats.getUser(taggedUser);
        if (user != null) {
            Log.i("Execution: ", "in upVote() of CommentActivity updating 'user' stats");
            user.updateStats(0, 0, 1, 0, 0, 0);
        }
    }

    private void downVote() {
        EditText textField = (EditText) findViewById(R.id.comment_box);
        String taggedUser = textField.getText().toString(); // Username of User tagged
        User user = ParseStats.getUser(taggedUser);
        Log.i("Execution: ", "in downVote() of CommentActivity updating 'user' stats");
        if (user != null) {
            Log.i("Execution: ", "in downVote() of CommentActivity updating 'user' stats");
            user.updateStats(0,0,0,1,0,0);
        }
    }


    private void tagUsers() {

        Log.i("Execution: ", "tagUser() in CommentActivity: ");
        NotifierSingleton n = NotifierSingleton.getInstance();
//        TextView tagField = (TextView) findViewById(R.id.tag_field);
        TextView commentField = (TextView) findViewById(R.id.viewTextSearchPostHeader);

        // should get number from usernames here

        String message = "You have been tagged in a comment!\n\n";
        message += (String) commentField.getText();
        n.sendNotification("2675677098", message);

        EditText textField = (EditText) findViewById(R.id.comment_box);
        String taggedUser = textField.getText().toString(); // Username of User tagged
        Log.i("Variable: ", "taggedUser: " + taggedUser);
        User user = ParseStats.getUser(taggedUser);
        if (user != null) {
            Log.i("Execution: ", "in tagUsers() of CreateCommentActivity updating 'user' stats");
            user.updateStats(0,1,0,0,0,0);
        }
    }
    int lat;
    int log;
    String text;


    void createComment() {
        User currUser = ParseStats.getUser(GlobalInfo.getInstance().getCreateAccountUsername());
        if (currUser != null) {
            Log.i("Execution: ", "in createComment() of CommentActivity updating 'currUser' stats");
            currUser.updateStats(0,0,0,0,1,0);
        }
        EditText textField = (EditText) findViewById(R.id.comment_box);
        text = textField.getText().toString();
        class AccessWebTask extends AsyncTask<URL, String, String> {
            protected String doInBackground(URL... urls) {
                try {
                    URL url = urls[0];
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Content-Type", "application/json");
                    //conn.setRequestProperty("Accept", "application/json");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);


                    JSONObject js = new JSONObject();
                    GlobalInfo info = GlobalInfo.getInstance();
                    String user = info.getCreateAccountUsername();
                    js.put("user", user);
                    js.put("text", text);
                    js.put("lat", lat);
                    js.put("long", log);
                    String id = getIntent().getStringExtra("POST_ID");
                    js.put("id", id);

                    OutputStream os = conn.getOutputStream();
                    os.write(js.toJSONString().getBytes());
                    os.flush();
                    os.close();
                    int responseCode = conn.getResponseCode();
                    Log.i("ss","Sending 'POST' request to URL : " + url);

                    return "good";
                }
                catch (Exception e) {
                    Log.i("dfs",e.toString());
                    return e.toString();}
            }
        }
        try{

            URL url = new URL("http://10.0.2.2:3000/addComment");
            AccessWebTask task = new AccessWebTask();
            task.execute(url);
            task.get();
            postPosts();

        } catch (Exception e){
            Log.i("dfs",e.toString());
        }
    }
    private RecyclerAdapterPost.ClickListener listen = new RecyclerAdapterPost.ClickListener() {
        @Override
        public void onItemClick(int position, View v) {

            try{
                String id = (String) commentJson.get(position).get("id");
            } catch (Exception e) {

                //startActivity(intent2);
            }
//            initViews();
//            postPosts();
//            makeButtons();

        }
    };
    @Override
    public void onBackPressed(){
        startActivity( new Intent(this, FeedActivity.class) );
        finish();
    }

    private void initViews() {

        commentJson = new ArrayList<JSONObject>();
        posts = new ArrayList<PostData>();

        tags = new ArrayList<String>();
        area = "";

        recycler = (RecyclerView) findViewById(R.id.viewRecyclerPost3);
        recycler.setHasFixedSize(true);
        recyclerLayoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(recyclerLayoutManager);
        recyclerAdapter = new RecyclerAdapterPost(posts);
        recycler.setAdapter(recyclerAdapter);
        recyclerAdapter.setOnItemClickListener(listen);
        recycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeComment(v);
            }
        });

    }

    public void makeComment(View v){
        Log.i("fda", v.getTransitionName());
    }

    private void connect() {
        class AccessWebTask extends AsyncTask<URL, String, String> {
            protected String doInBackground(URL...urls) {
                try {
                    URL url = urls[0];
                    conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();
                    Scanner in = new Scanner(url.openStream());
                    JSONParser parser = new JSONParser();
                    String toParse = "";
                    while(in.hasNext()){
                        toParse += " " + in.next();
                    }
                        JSONArray o = (JSONArray) parser.parse(toParse);
                        Iterator<JSONObject> persons = o.iterator();
                        while (persons.hasNext()) {
                            JSONObject person = (JSONObject) persons.next();
                            Log.i("dfs", person.toJSONString());

                            commentJson.add(person);

                    }
                    conn.getResponseCode();
                    return "good";
                }
                catch (Exception e) {
                    Log.i("PRINTOUT1", e.toString());
                }
                return "bad";
            }
        }
        try{
            String id = getIntent().getStringExtra("POST_ID");
            Log.i("Comment Id", id);
            URL url = new URL(PATH_DB + PATH_POSTS + id);
            AccessWebTask task = new AccessWebTask();
            task.execute(url);
            task.get();
        } catch (Exception e){
            Log.i("PRINTOUT", "BAD Connection COMMENTS");
        }
    }

    void postPosts(){
        commentJson.clear();
        if (!posts.isEmpty()) {
            int num = posts.size();
            for (int i = 0; i < num; i++) {
                posts.remove(0);
                recyclerAdapter.notifyItemRemoved(0);
            }
        }
        getPosts();
        Log.i("PRINTOUT", commentJson.size() + "");
        for(int i = 0; i < commentJson.size(); i++){
            PostData p1 = new PostData();
            p1.setText((String)commentJson.get(i).get("text"));
            posts.add(p1);
            recyclerAdapter.notifyItemInserted(posts.size());
        }

    }


    void getPosts() {
        connect();

    }
}

package edu.upenn.cis350.project;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class FeedActivity extends Activity implements AdapterView.OnItemSelectedListener {

    private HttpURLConnection conn;

    private final String PATH_DB = "http://10.0.2.2:3000";
    private final String PATH_POSTS = "/allPosts";

    private List<JSONObject> postsJson;
    private List<PostData> posts;

    private ComparatorPost sorter;

    private RecyclerView recycler;
    private RecyclerAdapterPost recyclerAdapter;
    private RecyclerView.LayoutManager recyclerLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        initViews();
        refresh();
    }

    private void initViews() {

        postsJson = new ArrayList<JSONObject>();
        posts = new ArrayList<PostData>();

        sorter = new ComparatorPost();

        recycler = (RecyclerView) findViewById(R.id.viewRecyclerFeed);
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

    private RecyclerAdapterPost.ClickListener listen = new RecyclerAdapterPost.ClickListener() {
        @Override
        public void onItemClick(int position, View v) {
            Log.i("ON ITEM CLICK", "TRYING TO GO TO COMMENTS");
            String id = (String) postsJson.get(position).get("_id");
            Intent intent = new Intent(getBaseContext(), CommentActivity.class);
            intent.putExtra("POST_ID", id);
            startActivity(intent);
            finish();

        }
    };

    public void clickSearch(View v) {

        refresh();

    }

    private void refresh() {

        postPosts();

        filterPosts();

        Collections.sort(posts, sorter);

    }

    private void postPosts(){
        postsJson.clear();
        if (!posts.isEmpty()) {
            int num = posts.size();
            for (int i = 0; i < num; i++) {
                posts.remove(0);
                recyclerAdapter.notifyItemRemoved(0);
            }
        }
        getPosts();
        for(int i = 0; i < postsJson.size(); i++){
            PostData p1 = new PostData();
            p1.setText((String)postsJson.get(i).get("text"));
            p1.image = (String)postsJson.get(i).get("image");
            p1.setUser((String)postsJson.get(i).get("user"));
            posts.add(p1);
            recyclerAdapter.notifyItemInserted(posts.size());
        }

    }

    private void getPosts() {

        connect();
        Log.i("PRINTOUT", postsJson.size() + "");

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
                    Log.i("BeforeLoop", toParse);

                    JSONArray o = (JSONArray) parser.parse(toParse);
                    Iterator<JSONObject> persons = o.iterator();
                    while (persons.hasNext()) {
                            JSONObject person = (JSONObject) persons.next();
                            Log.i("POSTS TO BE SHOWN", person.toJSONString());
                            postsJson.add(person);
                    }
                    in.close();
                    conn.disconnect();
                    return "good";
                }
                catch (Exception e) {
                    Log.i("PRINTOUT FEED ACTIVITY LOOP", e.toString());
                }
                return "bad";
            }
        }
        try{
            URL url = new URL(PATH_DB + PATH_POSTS);
            AccessWebTask task = new AccessWebTask();
            task.execute(url);
            task.get();
        } catch (Exception e){
            Log.i("PRINTOUT FEED", "Connection Cannot be made");
        }
    }

    public void filterPosts() {



    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    private class ComparatorPost<PostData> implements Comparator<PostData> {

        @Override
        public int compare(PostData p1, PostData p2) {

            return 0;

        }

    }

}

package edu.upenn.cis350.project;

import android.location.Geocoder;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.List;
import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.mongodb.util.JSON;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


import java.net.URL;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.StringTokenizer;
import android.content.Intent;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import org.json.simple.JSONObject;


public class PostsActivity extends AppCompatActivity {


    private final String PATH_DB = "http://10.0.2.2:3000";
    private final String PATH_POSTS = "/findUserPost";

    private List<String> tags;
    private String area;

    private Geocoder geocoder;

    List<JSONObject> postsJson;
    private List<PostData> posts;

    private ComparatorPost sorter;

    private EditText editTextTags;
    private EditText editTextArea;

    private Spinner spinnerSort;

    private RecyclerView recycler;
    private RecyclerAdapterPost recyclerAdapter;
    private RecyclerView.LayoutManager recyclerLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        initViews();
        postPosts();
    }

    private RecyclerAdapterPost.ClickListener listen = new RecyclerAdapterPost.ClickListener() {
        @Override
        public void onItemClick(int position, View v) {
            String id = (String) postsJson.get(position).get("_id");
            Intent intent = new Intent(getBaseContext(), CommentActivity.class);
            intent.putExtra("POST_ID", id);
            startActivity(intent);

        }
    };


    private void initViews() {

        postsJson = new ArrayList<JSONObject>();
        posts = new ArrayList<PostData>();

        tags = new ArrayList<String>();
        area = "";

        geocoder = new Geocoder(this, Locale.getDefault());

        sorter = new ComparatorPostDate();

        recycler = (RecyclerView) findViewById(R.id.viewRecyclerPost2);
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
        GlobalInfo info = GlobalInfo.getInstance();
        final String username =  info.getCreateAccountUsername();
        class AccessWebTask extends AsyncTask<URL, String, String> {
            protected String doInBackground(URL...urls) {
                try {
                    URL url = urls[0];
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Content-Type", "application/json");
                    //conn.setRequestProperty("Accept", "application/json");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);


                    JSONObject js = new JSONObject();

                    js.put("name", username);
                    js.put("password", "");
                    js.put("fullname", "");
                    js.put("zipcode",-1);
                    Log.i("fasdfads", js.toJSONString().toString());
                    OutputStream os = conn.getOutputStream();


                    os.write(js.toJSONString().getBytes());
                    Scanner in = new Scanner(conn.getInputStream());
                    os.flush();
                    int responseCode = conn.getResponseCode();
                    os.close();

                    JSONParser parser = new JSONParser();
                    String toParse = "";
                    while(in.hasNext()){
                        toParse += " " + in.next();
                    }

                        JSONArray o = (JSONArray) parser.parse(toParse);
                        Iterator<JSONObject> persons = o.iterator();
                        while (persons.hasNext()) {
                            JSONObject person = (JSONObject) persons.next();
                            Log.i("loooper", person.toJSONString() + postsJson.size());

                            postsJson.add(person);

                    }

                    in.close();
                    conn.disconnect();

                    return "good";
                }
                catch (Exception e) {
                    Log.i("PRINTOUT1", e.toString());
                }
                return "bad";
            }
        }
        try{
            String ur = PATH_DB + PATH_POSTS;
            URL url = new URL(ur);
            AccessWebTask task = new AccessWebTask();
            task.execute(url);
            task.get();
        } catch (Exception e){
            Log.i("PRINTOUT POST ACTIVITY", "BAD CONNEffffCTION");
        }
    }

    void postPosts(){
        postsJson.clear();
        if (!posts.isEmpty()) {
            int num = posts.size();
            for (int i = 0; i < num; i++) {
                posts.remove(0);
                recyclerAdapter.notifyItemRemoved(0);
            }
        }
        getPosts();
        Log.i(" SIZE", postsJson.size() + "");
        for(int i = 0; i < postsJson.size(); i++){
            PostData p1 = new PostData();
            p1.setText((String)postsJson.get(i).get("text"));
            posts.add(p1);
            recyclerAdapter.notifyItemInserted(posts.size());
        }

    }

    void getPosts() {
        connect();

    }
    private interface ComparatorPost extends Comparator<PostData> {}

    private class ComparatorPostDate implements ComparatorPost {

        public int compare(PostData a, PostData b) {

            return 0;

        }

    }

    private class ComparatorPostLikes implements ComparatorPost {

        public int compare(PostData a, PostData b) {

            return b.getKarma() - a.getKarma();

        }

    }


}

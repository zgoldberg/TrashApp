package edu.upenn.cis350.project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.StringTokenizer;

public class SearchPostActivity extends Activity implements AdapterView.OnItemSelectedListener {

    private HttpURLConnection conn;

    private final String PATH_DB = "http://10.0.2.2:3000";
    private final String PATH_POSTS = "/allPosts";

    private List<String> tags;
    private String area;

    private Geocoder geocoder;

    private List<JSONObject> postsJson;
    private List<PostData> posts;

    private ComparatorPost sorter;

    private EditText editTextTags;
    private EditText editTextArea;

    private Spinner spinnerSort;

    private RecyclerView recycler;
    private RecyclerAdapterPost recyclerAdapter;
    private RecyclerView.LayoutManager recyclerLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_post);
        initViews();
    }

    private void initViews() {

        postsJson = new ArrayList<JSONObject>();
        posts = new ArrayList<PostData>();

        tags = new ArrayList<String>();
        area = "";

        editTextTags = findViewById(R.id.viewTextEditSearchPostTags);
        editTextArea = findViewById(R.id.viewTextEditSearchPostArea);

        geocoder = new Geocoder(this, Locale.getDefault());

        sorter = new ComparatorPostDate();

        spinnerSort = (Spinner) findViewById(R.id.spinnerSearchPostSort);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.searchPostSort, android.R.layout.simple_spinner_item);
        spinnerSort.setAdapter(adapter);
        spinnerSort.setOnItemSelectedListener(this);

        recycler = (RecyclerView) findViewById(R.id.viewRecyclerPost);
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
            String id = (String) postsJson.get(position).get("_id");
            Intent intent = new Intent(getBaseContext(), CommentActivity.class);
            intent.putExtra("POST_ID", id);
            startActivity(intent);

        }
    };

    public void clickSearch(View v) {

        updateSearchSettings();

    }

    @Override
    public void onBackPressed(){
        startActivity( new Intent(this, HomeActivity.class) );
        finish();
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
                        postsJson.add(person);
                    }
                    in.close();
                    conn.disconnect();
                    return "good";
                }
                catch (Exception e) {
                    Log.i("PRINTOUT", e.toString());
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
            Log.i("PRINTOUT SEARCH POST ACTIVITY", "BAD CONNEffffCTION");
        }
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
        updateTags();
        updateArea();
        getPosts();
        for(int i = 0; i < postsJson.size(); i++){
            PostData p1 = new PostData();
            p1.setUser((String)postsJson.get(i).get("user"));
            p1.setText((String)postsJson.get(i).get("text"));
            p1.setLong((long)postsJson.get(i).get("long"));
            p1.setLat((long)postsJson.get(i).get("lat"));
            Log.d("PRINTOUT", p1.getLong() + ", " + p1.getLat());
            posts.add(p1);
            recyclerAdapter.notifyItemInserted(posts.size());
        }

    }

    private void updateSearchSettings() {

        postPosts();

        filterPosts();

        Collections.sort(posts, sorter);

    }

    private void updateTags() {

        tokenizeString(editTextTags.getText().toString(), tags);

    }

    private void updateArea() {

        area = editTextArea.getText().toString();

    }

    private void tokenizeString(String s, List<String> l) {

        l.clear();

        StringTokenizer tokenizer = new StringTokenizer(s);

        while (tokenizer.hasMoreTokens()) {

            l.add(tokenizer.nextToken());

        }

    }

    private void getPosts() {

        connect();
        Log.i("PRINTOUT", postsJson.size() + "");

    }

    private void filterPosts() {

        List<PostData> postsGood = new ArrayList<PostData>();
        postsGood.addAll(posts);

        filterPostsTags(postsGood);
        filterPostsArea(postsGood);

        posts.clear();
        posts.addAll(postsGood);

    }

    private void filterPostsTags(List<PostData> postsGood) {

        if (tags.isEmpty()) {
            return;
        }

        for (PostData p : posts) {

            boolean in = false;

            List<String> textTokenized = new ArrayList<String>();
            String text = (String) p.getText();

            tokenizeString(text, textTokenized);

            for (String tag : tags) {
                if (textTokenized.contains(tag)) {
                    in = true;
                    break;
                }
            }

            if (tags.contains(p.getUser())) {
                in = true;
            }

            if (!in) {
                postsGood.remove(p);
            }

        }

    }

    private void filterPostsArea(List<PostData> postsGood) {

        if (area.equals("")) {
            return;
        }

        List<Address> addresses;



        try {

            addresses = geocoder.getFromLocationName(area, 10);

            for (PostData p : posts) {

                boolean in = false;

                double pLong = p.getLong();
                double pLat = p.getLat();

                for (Address a : addresses) {

                    double aLong = a.getLongitude();
                    double aLat = a.getLatitude();

                    if (Math.abs(pLat - aLat) <= 1 && Math.abs(pLong - aLong) <= 1) {
                        in = true;
                        break;
                    }

                }

                if (!in) {

                    postsGood.remove(p);

                }

            }

        } catch (IOException e) {
            Log.d("PRINTOUT", e.getMessage());
            Context context = getApplicationContext();
            CharSequence text = "grpc failed! Please restart device!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        ((RecyclerAdapterPost) recyclerAdapter).setOnItemClickListener(new RecyclerAdapterPost
                .ClickListener() {
            @Override
            public void onItemClick(int position, View v) { }
        });
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        String item = (String) parent.getItemAtPosition(pos);
        switch (item) {
            case "Likes":
                sorter = new ComparatorPostLikes();
                break;
            case "Date":
                sorter = new ComparatorPostDate();
                break;
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {}

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

package edu.upenn.cis350.project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

public class SearchUserActivity extends Activity {

    private HttpURLConnection conn;

    private final String PATH_DB = "http://10.0.2.2:3000";
    private final String PATH_USERS = "/allUsers";

    private String name;
    private String area;

    private Geocoder geocoder;

    private List<JSONObject> usersJson;
    private List<UserData> users;

    private EditText editTextName;
    private EditText editTextArea;

    private RecyclerView recycler;
    private RecyclerAdapterUser recyclerAdapter;
    private RecyclerView.LayoutManager recyclerLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        initViews();
    }

    private void initViews() {

        usersJson = new ArrayList<JSONObject>();
        users = new ArrayList<UserData>();

        name = "";
        area = "";

        editTextName = findViewById(R.id.viewTextEditSearchPostTags);
        editTextArea = findViewById(R.id.viewTextEditSearchPostArea);

        geocoder = new Geocoder(this, Locale.getDefault());

        recycler = (RecyclerView) findViewById(R.id.viewRecyclerPost);
        recycler.setHasFixedSize(true);
        recyclerLayoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(recyclerLayoutManager);
        recyclerAdapter = new RecyclerAdapterUser(users);
        recycler.setAdapter(recyclerAdapter);

    }

    public void clickSearch(View v) {

        updateSearchSettings();

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
                    String toParse = "";
                    while(in.hasNext()){
                        toParse += " " + in.next();
                    }

                    JSONParser parser = new JSONParser();
                    JSONArray o = (JSONArray) parser.parse(toParse);
                    Iterator<JSONObject> persons = o.iterator();
                    while (persons.hasNext()) {
                        JSONObject person = (JSONObject) persons.next();
                        Log.i("dfs", person.toJSONString());
                        usersJson.add(person);
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
            URL url = new URL(PATH_DB + PATH_USERS);
            AccessWebTask task = new AccessWebTask();
            task.execute(url);
            task.get();
        } catch (Exception e){
            Log.i("PRINTOUT", "BAD CONNEffffCTION");
        }
    }

    private void postUsers(){
        usersJson.clear();
        if (!users.isEmpty()) {
            int num = users.size();
            for (int i = 0; i < num; i++) {
                users.remove(0);
                recyclerAdapter.notifyItemRemoved(0);
            }
        }
        updateName();
        updateArea();
        getUsers();
        for(int i = 0; i < usersJson.size(); i++){
            UserData u = new UserData();
            u.setUsername((String)usersJson.get(i).get("username"));
            u.setName((String)usersJson.get(i).get("fullname"));
            u.setZip(Math.round((long)usersJson.get(i).get("zipcode")));
            users.add(u);
            recyclerAdapter.notifyItemInserted(users.size());
        }

    }

    private void updateSearchSettings() {

        postUsers();
        filterUsers();

        Collections.sort(users, new Comparator<UserData>() {
            @Override
            public int compare(UserData u1, UserData u2) {
                return u1.getUsername().compareTo(u2.getUsername());
            }
        });

    }

    private void updateName() {

        name = editTextName.getText().toString();

    }

    private void updateArea() {

        area = editTextArea.getText().toString();

    }

    private void getUsers() {

        connect();
        Log.i("PRINTOUT", usersJson.size() + "");

    }

    private void filterUsers() {

        List<UserData> usersGood = new ArrayList<UserData>();
        usersGood.addAll(users);

        filterUsersName(usersGood);
        filterUsersArea(usersGood);

        users.clear();
        users.addAll(usersGood);

    }

    private void filterUsersName(List<UserData> usersGood) {

        if (name.equals("")) {
            return;
        }

        for (UserData u : users) {

            String uName = u.getName().toUpperCase();
            String uUserName = u.getUsername().toUpperCase();
            String query = name.toUpperCase();

            if (!uName.contains(query) && !uUserName.contains(query)) {

                usersGood.remove(u);

            }

        }

    }

    private void filterUsersArea(List<UserData> usersGood) {

        if (area.equals("")) {
            return;
        }

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocationName(area, 10);

            for (UserData u : users) {

                boolean in = false;

                for (Address a : addresses) {

                    if (u.getZip() == Integer.valueOf(a.getPostalCode().substring(0, 5))) {
                        in = true;
                        break;
                    }

                }

                if (!in) {

                    usersGood.remove(u);

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
        ((RecyclerAdapterUser) recyclerAdapter).setOnItemClickListener(new RecyclerAdapterUser
                .ClickListener() {
            @Override
            public void onItemClick(int position, View v) { }
        });
    }

    public void onNothingSelected(AdapterView<?> parent) {}

}

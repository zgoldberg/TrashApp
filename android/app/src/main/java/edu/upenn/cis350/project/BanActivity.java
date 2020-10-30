package edu.upenn.cis350.project;
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

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BanActivity extends AppCompatActivity {

    private final String PATH_DB = "http://10.0.2.2:3000";
    private final String PATH_POSTS = "/delete";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ban_user);
        makeButtons();
    }

    private void makeButtons() {
        Button createButton = (Button) findViewById(R.id.ban_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                banUser();
            }
        });
    }
    int responseCode =0;
    private void banUser(){
        EditText name = (EditText) findViewById(R.id.ban_username);
        final String username =  name.getText().toString();

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
                    js.put("name", username);
                    js.put("password", "");
                    js.put("fullname", "");
                    js.put("zipcode",-1);
                    OutputStream os = conn.getOutputStream();
                    os.write(js.toJSONString().getBytes());
                    os.flush();
                    os.close();
                    Log.i("dfs","good");
                    responseCode = conn.getResponseCode();
                    Log.i("dfs","Response Code : " + responseCode);

                    conn.disconnect();

                    return "good";
                }
                catch (Exception e) {
                    Log.i("dfs",e.toString());
                    return e.toString();}
            }
        }
        try{
            String ur = PATH_DB + PATH_POSTS;
            URL url = new URL(ur);
            AccessWebTask task = new AccessWebTask();
            task.execute(url);
            TextView banMessage = (TextView) findViewById(R.id.ban_message);
            TextView banMessage2 = (TextView) findViewById(R.id.ban_message2);

            if(responseCode == 200){
                Log.i("fd", responseCode+"");
                banMessage2.setVisibility(View.INVISIBLE);
                banMessage.setVisibility(View.VISIBLE);


            } else {
                    banMessage.setVisibility(View.INVISIBLE);
                    banMessage2.setVisibility(View.VISIBLE);
            }
            responseCode = 0;
        } catch (Exception e){
        }
        name.setText("");
    }
}

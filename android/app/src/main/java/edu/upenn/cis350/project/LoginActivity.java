package edu.upenn.cis350.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.Manifest;

import android.util.Log;
import android.os.AsyncTask;
import java.util.Iterator;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import android.util.Log;

public class LoginActivity extends AppCompatActivity {
    int admin =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i("Running : ", "onCreate()");
        admin  = 1;
        makeButtons();

        NotifierSingleton n = NotifierSingleton.getInstance();
//        n.sendNotification("2675677098", "hello admin");
        n.sendNotification("2675677098", "comMENT");

    }

    private void makeButtons() {

        Button loginButton = (Button) findViewById(R.id.login_button);
        Button createButton = (Button) findViewById(R.id.create_button);
        Log.i("Running : ", "makeButtons()");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginPress();
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPress();
            }
        });

    }

    private void loginPress() {
        Log.i("Running : ", "loginPresss()");
        if(ContextCompat.checkSelfPermission(LoginActivity.this,
            Manifest.permission.INTERNET)
            != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(LoginActivity.this,
                new String[]{Manifest.permission.INTERNET},
                1);

        }

        EditText usernameField = (EditText) findViewById(R.id.username_field);
        EditText passwordField = (EditText) findViewById(R.id.password_field);

        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        // Jason added this b/c there seemed to be an error with updating data in GlobalInfo
        GlobalInfo infoOuter = GlobalInfo.getInstance();
        infoOuter.setCreateAccountUsername(username);
        Log.i("Variable Value: ", "In loginPress(), variable is: " + infoOuter.getCreateAccountUsername());

        boolean validLogin = getUserInfo(username, password);

        if (validLogin) {

            GlobalInfo info = GlobalInfo.getInstance();
            info.setCreateAccountUsername(username);
            Intent intent;
            int admin = 0;
            String ad = "admin";
            Log.i("fd", username);
            if(ad.equals(username)){
                admin = 1;
            } else{
                admin = 0;
            }

            if(admin == 1){
                intent = new Intent(this, AdminHomeActivity.class);
            } else {
                intent = new Intent(this, HomeActivity.class);
            }
            intent.putExtra("Username", username);
            startActivity(intent);

        } else {

            TextView loginMessage = (TextView) findViewById(R.id.login_message);

            if (loginMessage.getVisibility() != View.VISIBLE) {
                loginMessage.setVisibility(View.VISIBLE);
            }

            passwordField.setText("");

        }

    }

    private boolean getUserInfo(String username, final String password) {
        String pas = null;
        if(username.equals("")){
            return  false;
        }
        class AccessWebTask extends AsyncTask<URL, String, String> {
            protected String doInBackground(URL... urls) {
                try {
                    URL url = urls[0];
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();
                    Scanner in = new Scanner(url.openStream());
                    String msg = in.nextLine();
                    JSONParser parser = new JSONParser();
                    JSONObject jo = (JSONObject) parser.parse(msg);

                    GlobalInfo info = GlobalInfo.getInstance();
                    User user = new User((String) jo.get("name"), (String) jo.get("password"), (String) jo.get("fullname"),  ((Long) jo.get("zipcode")).intValue());
                    String pas = (String) jo.get("password");

                    if(user.password == null){
                        return pas;
                    }
                    info.setViewAccountUser(user);
                    info.setLoggedInUser(user);

                    // Jason commented this out because it replaced a correct variable with null
//                    info.setCreateAccountUsername(user.username);     // NOTE THIS LINE
                    Log.i("Variable Value: ", "In getUserInfo(), variable is: " + info.getCreateAccountUsername());


                    Log.i("dfsf", jo.toJSONString());
                    conn.disconnect();
                    return pas;
                }
                catch (Exception e) {
                    Log.i("dfasdf", e.toString());
                    return null;}
            }
        }
        try{
            URL url = new URL("http://10.0.2.2:3000/api?name=" + username);
            AccessWebTask task = new AccessWebTask();
            task.execute(url);
            pas = task.get();
        } catch (Exception e){
            Log.i("dfasdf", e.toString());
            return false;
        }
        if(password == null || pas == null){
            return false;
        }
        return password.equals(pas);

    }


    private void createPress() {

        EditText usernameField = (EditText) findViewById(R.id.username_field);
        String username = usernameField.getText().toString();

        GlobalInfo info = GlobalInfo.getInstance();
        info.setCreateAccountUsername(username); // NOTE THIS LINE TOO

        Intent intent = new Intent(this, CreateAccountActivity.class);
        intent.putExtra("Username", username);

        startActivity(intent);

    }

}

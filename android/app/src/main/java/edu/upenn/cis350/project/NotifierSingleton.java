package edu.upenn.cis350.project;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static android.provider.Settings.System.getString;
import static androidx.core.content.ContextCompat.getSystemService;

//Example of use:
//        NotifierSingleton n = NotifierSingleton.getInstance();
//                n.sendNotification("2675677098", "hi");

public class NotifierSingleton {

    private NotifierSingleton() {

    }

    private static NotifierSingleton instance = new NotifierSingleton();

    public static NotifierSingleton getInstance() {
        return instance;
    }

    public boolean sendNotification(final String number, final String message) {


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


                    String json = "";
                    JSONObject js = new JSONObject();
                    js.put("number", number);
                    js.put("message", message);
                    Log.i("ss","Sending 'POST' request to URL : " + url);

                    OutputStream os = conn.getOutputStream();
                    os.write(js.toJSONString().getBytes());
                    os.flush();
                    os.close();
                    int responseCode = conn.getResponseCode();
                    Log.i("ss","Sending 'POST' request to URL : " + url);
                    Log.i("dfs","Response Code : " + json);
                    Log.i("dfs","Response Code : " + responseCode);
                    return "good";
                }
                catch (Exception e) {
                    Log.i("dfs",e.toString());
                    return e.toString();}
            }
        }
        try{
            String s = "http://10.0.2.2:3000/sendtext?number=" + number + "&message=" + message;
            URL url = new URL(s);
            AccessWebTask task = new AccessWebTask();
            task.execute(url);
            task.get();
            return true;
        } catch (Exception e){
            return  false;
        }

    }


}

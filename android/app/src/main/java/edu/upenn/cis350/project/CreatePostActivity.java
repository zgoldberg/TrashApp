package edu.upenn.cis350.project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.graphics.Color;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.TextView;
import android.util.Log;

import org.json.simple.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CreatePostActivity extends AppCompatActivity implements LocationListener{
    public static Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        // set username to whatever was typed in last activity
        bitmap = null;
        makeButtons();
    }


    private void makeButtons() {

        Button createButton = (Button) findViewById(R.id.post_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPost();
            }
        });
        Button imageButton = (Button) findViewById(R.id.get_image);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });

    }

    private void getImage() {
        Intent intent = new Intent(getBaseContext(), PicActivity.class);
        startActivity(intent);
    }
    int lat;
    int log;
    String text;

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    private void createPost() {

    // Commented this out for the purpose of testing
        User currUser = ParseStats.getUser(GlobalInfo.getInstance().getCreateAccountUsername());
        if (currUser != null) {
            Log.i("Execution: ", "in createPost() updating currUser stats");
            currUser.updateStats(1, 0, 0, 0, 0, 0);
        }
        EditText textField = (EditText) findViewById(R.id.post_text_field);
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
                    if(bitmap != null){
                        js.put("image", BitMapToString(bitmap));
                    } else {
                        js.put("image", " ");
                    }

                    OutputStream os = conn.getOutputStream();
                    os.write(js.toJSONString().getBytes());
                    os.flush();
                    os.close();
                    int responseCode = conn.getResponseCode();
                    Log.i("ss","Sending 'POST' request to URL : " + url);
                    Log.i("dfs","Json: " + js.toJSONString());
                    Log.i("dfs","Response Code : " + responseCode);
                    return "good";
                }
                catch (Exception e) {
                    Log.i("dfs",e.toString());
                    return e.toString();}
            }
        }
        try{

            URL url = new URL("http://10.0.2.2:3000/createPost");
            AccessWebTask task = new AccessWebTask();
            task.execute(url);
            task.get();
            finish();
        } catch (Exception e){
            Log.i("dfs",e.toString());
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        lat = (int) location.getLatitude();
        log = (int) location.getLongitude();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }
}



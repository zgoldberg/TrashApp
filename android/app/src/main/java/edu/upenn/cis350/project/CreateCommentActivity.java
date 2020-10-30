package edu.upenn.cis350.project;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.simple.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.acl.NotOwnerException;

public class CreateCommentActivity extends AppCompatActivity {

//    private void makeButtons() {
//
//        Button createButton = (Button) findViewById(R.id.post_button);
//        createButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                createComment();
//            }
//        });
//
//        Button tagButton = (Button) findViewById(R.id.tag_button);
//        tagButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tagUsers();
//            }
//        });
//
//        Button upButton = (Button) findViewById(R.id.upvote_button);
//        tagButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                upVote();
//            }
//        });
//
//        Button downButton = (Button) findViewById(R.id.downvote_button);
//        tagButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                downVote();
//            }
//        });
//
//    }
//
//    private void upVote() {
//        EditText textField = (EditText) findViewById(R.id.comment_box);
//        String taggedUser = textField.getText().toString(); // Username of User tagged
//        User user = ParseStats.getUser(taggedUser);
//        if (user != null) {
//            Log.i("Execution: ", "in upVote() of CreateCommentActivity updating 'user' stats");
//            user.updateStats(0, 0, 1, 0, 0, 0);
//        }
//    }
//
//    private void downVote() {
//        EditText textField = (EditText) findViewById(R.id.comment_box);
//        String taggedUser = textField.getText().toString(); // Username of User tagged
//        User user = ParseStats.getUser(taggedUser);
//        if (user != null) {
//            Log.i("Execution: ", "in downVote() of CreateCommentActivity updating 'user' stats");
//            user.updateStats(0,0,0,1,0,0);
//        }
//    }
//
//
//    private void tagUsers() {
//
//        Log.i("Execution: ", "tagUser() in CreateCommentActivity: ");
//        NotifierSingleton n = NotifierSingleton.getInstance();
////        TextView tagField = (TextView) findViewById(R.id.tag_field);
//        TextView commentField = (TextView) findViewById(R.id.viewTextSearchPostHeader);
//
//        // should get number from usernames here
//
//        String message = "You have been tagged in a comment!\n\n";
//        message += (String) commentField.getText();
//        n.sendNotification("2675677098", message);
//
//        EditText textField = (EditText) findViewById(R.id.comment_box);
//        String taggedUser = textField.getText().toString(); // Username of User tagged
//        Log.i("Variable: ", "taggedUser: " + taggedUser);
//        User user = ParseStats.getUser(taggedUser);
//        if (user != null) {
//            Log.i("Execution: ", "in tagUsers() of CreateCommentActivity updating 'user' stats");
//            user.updateStats(0,1,0,0,0,0);
//        }
//    }

    int lat;
    int log;
    String text;
    private void createComment() {
        EditText textField = (EditText) findViewById(R.id.post_text_field);
        text = textField.getText().toString();
        class AccessWebTask extends AsyncTask<URL, String, String> {
            protected String doInBackground(URL... urls) {
                try {
                    URL url = urls[0];
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("POST");
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
                    Log.i("ss","Sending 'POST' request to URL : " + url);

                    OutputStream os = conn.getOutputStream();
                    os.write(js.toJSONString().getBytes());
                    os.flush();
                    os.close();
                    int responseCode = conn.getResponseCode();
                    Log.i("dfs","Response Code : " + js.toJSONString());
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
        } catch (Exception e){
            Log.i("dfs",e.toString());
        }
    }

}

package edu.upenn.cis350.project;
import android.os.AsyncTask;
import android.util.Log;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class User {

    String username;
    String password;
    String fullName;
    int zipCode;
    public String userId;
    public ArrayList<Post> posts;
    private int karma; // AVOID USING THIS, instead use Stats.getKarma() method
    private int followers;
    private int following;
    private Stats stats;
    private String phoneNumber;


    // For testing
    public String toString() {
        return username + password + fullName + zipCode;
    }

    public User(String username, String password, String fullName, int zipCode) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.zipCode = zipCode;
        this.phoneNumber = "2675677098";
        this.userId = userId;
        this.posts = posts;
        this.stats = new Stats();
    }

    public void replaceStats(Stats stats) {
        this.stats = stats;
    }


    // Should we implement a similar method below, but for Post??
    // Would only really get used if we choose to display upVotes next to each post in the feed




    // Update user object in MongoDB database and also updates Java User object in place
    // Before calling, make sure to find Java user User (maybe using ParseStats?)
    // @params include increases in everything (nPosts for "new Posts"), rather than total values
    public boolean updateStats(int nPosts, int nTags,
                               int nUp, int nDown, int nCPost, int nCTag) {

//        this.username = newUsername; Commented this out and removed @param bc shouldn't change
        stats.posts += nPosts;
        stats.tags += nTags;
        stats.upVotes += nUp;
        stats.downVotes += nDown;
        this.karma = stats.getKarma();
        stats.commentsPosted += nCPost;
        stats.commentsTagged += nCTag;

        class AccessWebTask extends AsyncTask<URL, String, String> {
            protected String doInBackground(URL... urls) {
                try {
                    URL url = urls[0];
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
//                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoInput(true);
                    //conn.setDoOutput(true);
                    conn.connect();



                    //String json = "\"name\":" + "\"" + user.username + "\"" +  ",\"" + "password\":" + "\"" + user.password +"\",\"fullname\":" +  "\"" +user.fullName + "\",\"zipcode\":" + user.zipCode  +  "";
                    JSONObject js = new JSONObject();
                    js.put("name", username);
                    js.put("posts",  stats.posts);
                    js.put("tags",  stats.tags);
                    js.put("up", stats.upVotes);
                    js.put("down",  stats.downVotes);
                    js.put("cPost",stats.commentsPosted);
                    js.put("cTag", stats.commentsTagged);


                    OutputStream os = conn.getOutputStream();
                    os.write(js.toJSONString().getBytes());
                    os.flush();
                    os.close();
                    Log.i("ss","Sending 'POST' request to URL : " + url);
                    Log.i("USER STATS UPDATE:","UPDATE SUCESS");
                    //int responseCode = conn.getResponseCode();
//
//
//                    //String json = "\"name\":" + "\"" + user.username + "\"" +  ",\"" + "password\":" + "\"" + user.password +"\",\"fullname\":" +  "\"" +user.fullName + "\",\"zipcode\":" + user.zipCode  +  "";
//                    JSONObject js = new JSONObject();
//                    js.put("name", user.username);
//                    js.put("password", user.password);
//                    js.put("fullname", user.fullName);
//                    js.put("zipcode", user.zipCode);
//                    Log.i("ss","Sending 'POST' request to URL : " + url);
//
//                    OutputStream os = conn.getOutputStream();
//                    os.write(js.toJSONString().getBytes());
//                    os.flush();s
//                    os.close();
                    int responseCode = conn.getResponseCode();
//                    Log.i("ss","Sending 'POST' request to URL : " + url);
                    Log.i("SENT JS UPDATE","" + js.toJSONString());
                    Log.i("RESPONSE CODE UPDATE STATS","Response Code : " + responseCode);
                    conn.disconnect();
                    return "good";
                }
                catch (Exception e) {
                    Log.i("ERROR IN UPDATE USER STATS",e.toString());
                    return e.toString();}
            }
        }
        try{

            Log.i("Execution: ", "User updateStats() executes");
//            String query = "?name=" + username + "&posts=" + stats.posts + "&tags=" +
//                    stats.tags +"&up=" + stats.upVotes + "&down=" + stats.downVotes  + "&cPost=" +
//                    stats.commentsPosted + "&cTag=" + stats.commentsTagged + "";
            String urlStr = "http://10.0.2.2:3000/updateUserStats" ;

            URL url = new URL(urlStr);
            AccessWebTask task = new AccessWebTask();
            task.execute(url);
            task.get();
            Log.i("Execution: ", "Tried to access Mongo via: " + urlStr);
            return true;
        } catch (Exception e){
            return  false;
        }
    }


    public Stats getStats() {
        return stats;
    }

    public String getUsername() {
        return username;
    }

    public int getFollowers() {
        return followers;
    }

    public int getFollowing() {
        return following;
    }

    public int getKarma() {
        return karma;
    }

    public String getFullName() { return fullName; }

    public String getZip() { return Integer.toString(zipCode); }

    public String getPhoneNumber() { return this.phoneNumber; }

}

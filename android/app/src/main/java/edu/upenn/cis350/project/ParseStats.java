package edu.upenn.cis350.project;

import android.os.AsyncTask;
import android.util.Log;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class ParseStats {



    public static String[] getLeaders() {
        List<User> allUsers = getAllUsers();
        int numUsers = allUsers.size();
        if (numUsers == 0) {
            return null;
        }
        User currPost = null;
        User currTag = null;
        User currHigh = null;
        User currLow =  null;
        int postNum = -1;
        int tagNum = -1;
        double highNum = -1.0;
        double lowNum = 2.0;

        for (User user: allUsers) {
            Stats userStats = user.getStats();
            if (userStats.posts() > postNum) {
                currPost = user;
                postNum = userStats.posts();
            }
            if (userStats.tags > tagNum) {
                currTag = user;
                tagNum = userStats.tags;
            }
            if (userStats.getPercent() > highNum) {
                currHigh = user;
                highNum = userStats.getPercent();
            }
            if (userStats.getPercent() < lowNum) {
                currLow = user;
                lowNum = userStats.getPercent();
            }
        }
        String strPost = currPost.getUsername() + ": " + postNum;
        String strTag = currTag.getUsername() + ": " + tagNum;
        String strHigh = currHigh.getUsername() + ": " + currHigh.getStats().percentString() + "%";
        String strLow = currLow.getUsername() + ": " + currLow.getStats().percentString() + "%";
        return new String[] {strPost, strTag, strHigh, strLow};
    }


    // Compute aggregate stats over entire MongoDB via addition
    // Return User object with numerical fields representing entire network
    public static Stats getAdminStats() {

        int posts = 0; // Times this user has posted
        int tags = 0; // Times this user has been tagged in a post
        int upVotes = 0; // Total upVotes among posts this User was tagged in
        int downVotes = 0; // Total downVotes among posts this User was tagged in
        int commentsPosted = 0;
        int commentsTagged = 0;

        List<User> listAllUsers = getAllUsers();
        for (User user: listAllUsers) {
            Stats userStats = user.getStats();
            posts += userStats.posts();
            tags += userStats.trendingTags();
            upVotes += userStats.getUpVotes();
            downVotes += userStats.getDownVotes();
            commentsPosted += userStats.getCommentsPosted();
            commentsTagged += userStats.getCommentsTagged();
        }
        return new Stats(posts, tags, upVotes, downVotes, commentsPosted, commentsTagged);
    }
    static String currUser;
    // Gets Stats object from MongoDB for current user from MongoDB
    public static Stats getUserCurrentStats() {

        // I got rid of these for now due to an error with GlobalInfo.java
//        User currUser = GlobalInfo.getInstance().getViewAccountUser();
//        String currUsername = currUser.getUsername();
        String currUsername = GlobalInfo.getInstance().getCreateAccountUsername();
        Log.i("Variable Value: ", "curr Username is: " + currUsername);

        User currUserDB = getUser(currUsername);
        Log.i("Variable Value: ", "curr User object is: " + currUserDB);

        return currUserDB.getStats();
//        currUser = currUsername;
//        getAllUsers();
//        return GlobalInfo.getInstance().stats;
    }

    // Get User object from MongoDB for specified username
    public static User getUser(String username) {
        List<User> listAllUsers = getAllUsers();
        for (User user: listAllUsers) {

            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public static int numUsers() {
        return getAllUsers().size();
    }
//      Commented out because it includes some of Abel's outdated modifications from a git pull
//    // Get List of all users as displayed in MongoDB
//    public static List<User> getAllUsers() {
//
//        final List<User> list = new ArrayList<User>();
//        class AccessWebTask extends AsyncTask<URL, String, String> {
//            protected String doInBackground(URL... urls) {
//                try {
//                    URL url = urls[0];
//                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
//                    conn.setRequestMethod("GET");
//                    conn.connect();
//                   Scanner in = new Scanner(url.openStream());
//
//                    JSONParser parser = new JSONParser();
////                    String toParse = "";
////                    while(in.hasNext()){
////                        toParse += " " + in.next();
////                    }
////                    conn.setDoInput(false);
////                    Scanner in = new Scanner(conn.getInputStream());
////                    String msg = in.nextLine();
////                    JSONObject jo = (JSONObject) parser.parse(msg);
////                    JSONObject js = new JSONObject();
////                    conn.setDoOutput(true);
////                    int responseCode = conn.getResponseCode();
////                    GlobalInfo info = GlobalInfo.getInstance();
//                    String inputLine;
//                    while (in.hasNext())
//                        JSONArray o = (JSONArray) parser.parse(toParse);
//                        Iterator<JSONObject> persons = o.iterator();
//
//                        Log.i("Users", currUser);
//                        while (persons.hasNext()) {
//                            JSONObject jo = (JSONObject) persons.next();
//
//                            Log.i("Users", jo.toString());
//                            if(currUser.equals((String) jo.get("username"))){
//
//                                JSONtoUser(jo);
//
//                                in.close(); // Keep this whether Scanner or Reader
//                                conn.disconnect();
//                                return "good";
//                            }
//                        }
//
//                    in.close(); // Keep this whether Scanner or Reader
//                    conn.disconnect();
//
//                    return "good";
//                }
//                catch (Exception e) {
//                    Log.i("dfasdf", e.toString());
//                    return null;}
//            }
//        }
//        try{
////            String json = "?name=" + user.username + "&password=" + user.password +"&fullname=" + user.fullName + "&zipcode=" + user.zipCode  +  "";
//            URL url = new URL("http://10.0.2.2:3000/allUsers");
//            AccessWebTask task = new AccessWebTask();
//            task.execute(url);
//            task.get();
//            return list;
//        } catch (Exception e){
//            return  list;
//        }
//
//    }

    // Restored from previous git commit
    // Get List of all users as displayed in MongoDB
    public static List<User> getAllUsers() {

        Log.i("Error: ", "getAllUsers() has begun executing");

        final List<User> list = new ArrayList<User>();
        class AccessWebTask extends AsyncTask<URL, String, String> {
            protected String doInBackground(URL... urls) {
                try {
                    URL url = urls[0];
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();
//                    Scanner in = new Scanner(url.openStream());
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(url.openStream()));

                    JSONParser parser = new JSONParser();
//                    conn.setDoInput(false);
//                    Scanner in = new Scanner(conn.getInputStream());
//                    String msg = in.nextLine();
//                    JSONObject jo = (JSONObject) parser.parse(msg);
//                    JSONObject js = new JSONObject();
//                    conn.setDoOutput(true);
//                    int responseCode = conn.getResponseCode();
//                    GlobalInfo info = GlobalInfo.getInstance();
                    String inputLine;
//                    while (in.hasNext()) {
                    while ((inputLine = in.readLine()) != null) {
                        JSONArray o = (JSONArray) parser.parse(inputLine);
                        Iterator<JSONObject> persons = o.iterator();
                        while (persons.hasNext()) {
                            JSONObject jo = (JSONObject) persons.next();
                            User user = JSONtoUser(jo);
                            list.add(user);
                        }
                    }

                    in.close(); // Keep this whether Scanner or Reader
                    conn.disconnect();

                    return "good";
                }
                catch (Exception e) {
                    Log.i("dfasdf", e.toString());
                    return null;}
            }
        }
        try{
//            String json = "?name=" + user.username + "&password=" + user.password +"&fullname=" + user.fullName + "&zipcode=" + user.zipCode  +  "";
            URL url = new URL("http://10.0.2.2:3000/allUsers");
            AccessWebTask task = new AccessWebTask();
            task.execute(url);
            task.get();
            return list;
        } catch (Exception e){
            return  list;
        }

    }


    // Returns Java User formulation of this person's JSONObject
    // If the person had empty stats fields, then fill them in with 0's
    // Should work now, but may be buggy bc of back and forth function calls?
    // Best practice to simply ensure all fields are existent in MongoDB
    public static User JSONtoUser(JSONObject jo) {

        User user = new User((String) jo.get("username"), (String) jo.get("password"),
                (String) jo.get("fullname"), (Math.toIntExact(((long) jo.get("zipcode")))));
        Stats userStats = null;
        try {
            int postsMade = Math.toIntExact((long) jo.get("postsMade"));
            int postsTagged = Math.toIntExact((long) jo.get("postsTagged"));
            int upVotes = Math.toIntExact((long) jo.get("upVotes"));
            int downVotes = Math.toIntExact((long) jo.get("downVotes"));
            int commentsMade = Math.toIntExact((long) jo.get("commentsMade"));
            int commentsTagged = Math.toIntExact((long) jo.get("commentsTagged"));
            userStats = new Stats(postsMade, postsTagged, upVotes,
                    downVotes, commentsMade, commentsTagged);
            user.replaceStats(userStats);
            Log.i("Execution: ", "JSONtoUser try block executed successfully");
        } catch (NullPointerException e) {
            Log.i("Execution: ", "JSONtoUser catch block execution begins");
            userStats = new Stats(0, 0, 0, 0, 0, 0);
            Log.i("Exception: ", "Admin found to be: " + jo);
            user.replaceStats(userStats);
            user.updateStats(0,0,0,0,0,0);
            Log.i("Execution: ", "JSONtoUser catch block execution begins");

        }
        user.replaceStats(userStats);
        return user;
//        int postsMade = Math.toIntExact((long) jo.get("postsMade"));
//        int postsTagged = Math.toIntExact((long) jo.get("postsTagged"));
//        int upVotes = Math.toIntExact((long) jo.get("upVotes"));
//        int downVotes = Math.toIntExact((long) jo.get("downVotes"));
//        int commentsMade = Math.toIntExact((long) jo.get("commentsMade"));
//        int commentsTagged = Math.toIntExact((long) jo.get("commentsTagged"));
//        Stats userStats = new Stats(postsMade, postsTagged, upVotes,
//                downVotes, commentsMade, commentsTagged);
//        GlobalInfo info = GlobalInfo.getInstance();
//        info.stats = userStats;
//        return null;
    }

}

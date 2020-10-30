package edu.upenn.cis350.project;

import android.util.Log;

import org.json.JSONException;
import org.json.simple.JSONObject;

public class PostData {

    private String user;

    private String text;
    String image;

    private int votesUp;
    private int votesDown;

    private double lon;
    private double lat;

    private int karma;

    public PostData() {

        text = "Hello World!";

    }

    public PostData(JSONObject o) {
        user = (String) o.get("user");
        text = (String) o.get("text");
        image = (String) o.get("image");

        votesUp = (int) o.get("votesUp");
        votesDown = (int) o.get("votesDown");

        lon = (double) o.get("long");
        lat = (double) o.get("lat");

        karma = votesUp - votesDown;

    }

    public String getUser() {

        return user;

    }

    public void setUser(String u) {

        user = u;

    }

    public String getText() {

        return text;

    }

    public String getImage() {

        return image;

    }

    public int getKarma() {

        return karma;

    }

    public double getLong() {

        return lon;

    }

    public void setLong(double longitude) {

        lon = longitude;

    }

    public double getLat() {

        return lat;

    }

    public void setLat(double latitude) {

        lat = latitude;

    }

    public void setKarma(int k) {

        karma = k;

    }

    public void setText(String t) {

        text = t;

    }

}
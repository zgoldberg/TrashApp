package edu.upenn.cis350.project;

import android.content.Intent;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Comment {
	private String text;
	private String image;

	String com;
	int upVotes;
	int downVotes;
	User commenter;
	User[] tags;

	int votesUp;
	int votesDown;
	int karma;
	String user;

	public Comment(String com, int upVotes, int downVotes) {
		this.com = com;
		this.upVotes = upVotes;
		this.downVotes = downVotes;
	}

	public Comment() {

	}

	public Comment(JSONObject o) {
		user = (String) o.get("user");
		text = (String) o.get("text");
		//image = (String) o.get("image");
	}


	public void setText(String t) {

		text = t;
	}
}
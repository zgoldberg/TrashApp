package edu.upenn.cis350.project;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import org.json.simple.JSONObject;
import com.mongodb.Cursor;
import com.mongodb.MongoClientURI;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ParallelScanOptions;
import com.mongodb.ServerAddress;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import org.json.simple.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.List;
import android.app.Activity;
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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mongodb.util.JSON;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


import java.net.URL;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.StringTokenizer;
import android.content.Intent;
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


public class PicActivity  extends AppCompatActivity {

  private final String PATH_DB = "http://10.0.2.2:3000";
  private final String PATH_POSTS = "/delete"; //SOMETHING
  Mongo mongo;
  ImageView imageView;
  Uri imageUri;
  private static final int PICK_IMAGE = 100;
    {
        try {
            mongo = new Mongo("localhost", 27017);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    DB db = mongo.getDB("imagedb");
  DBCollection collection = db.getCollection("images");

  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_pics);
      makeButtons();
  }

  private  void makeButtons(){

      Button createButton = (Button) findViewById(R.id.add_image);
      imageView = (ImageView) findViewById(R.id.imageView);
      createButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              add_image();
          }
      });
      Button imageButton = (Button) findViewById(R.id.find_image);
      imageButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              openGallery();
          }
      });
  }

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    private  void add_image(){
//
//	        File imageFile = new File(filepath);
//	        GridFS gfsPhoto = new GridFS(db, "photo");
//	        GridFSInputFile gfsFile = gfsPhoto.createFile(imageFile);
//	        gfsFile.setFilename(filename);
//	        gfsFile.save();

          finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            Log.i("gadsg", imageUri+"");
            try {
                CreatePostActivity.bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageView.setImageURI(imageUri);
        }
    }

//    public void savePicToDB(Post post, String fd){
//
//
//        File imageFile = new File(fd);
//        GridFS gfsPhoto = new GridFS(db, "photo" + post.coordX.toString()
//         + post.coordY.toString() +user);
//
//  			// get image file from local drive
//  			GridFSInputFile gfsFile = gfsPhoto.createFile(imageFile);
//
//  			// set a new filename for identify purpose
//  			gfsFile.setFilename(newFileName);
//
//  			// save the image file into mongoDB
//  			gfsFile.save();
//      }
////
//      void printList(){
//        DBCursor cursor = gfsPhoto.getFileList();
//  			while (cursor.hasNext()) {
//  				System.out.println(cursor.next());
//  			}
//      }
//
//      void RmPic(Post post, String fd){
//        GridFSDBFile imageForOutput = gfsPhoto.findOne(fd);
//        gfsPhoto.remove(gfsPhoto.findOne(fd));
//
//      }


}

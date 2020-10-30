package edu.upenn.cis350.project;

import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.graphics.Color;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * An activity that displays a Google map with a marker (pin) to indicate a particular location.
 */
public class MapsMarkerActivity extends AppCompatActivity
    implements OnMapReadyCallback {

    private HttpURLConnection conn;

    private final String PATH_DB = "http://10.0.2.2:3000";
    private final String PATH_POSTS = "/allPosts";

    private List<String> tags;
    private String area;

    private Geocoder geocoder;

    List<JSONObject> postsJson;
    private List<PostData> posts;


    private EditText editTextTags;
    private EditText editTextArea;

    private Spinner spinnerSort;

    private RecyclerView recycler;
    private RecyclerAdapterPost recyclerAdapter;
    private RecyclerView.LayoutManager recyclerLayoutManager;
    private MapView mMapView;
    private  GoogleMap map;
    Location curr;
    FusedLocationProviderClient fusedLocationsProviderClient;
    private static final int REQUEST_CODE = 101;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        //setContentView(R.layout.activity_maps);
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        setContentView(R.layout.activity_map);
        initViews();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
         mapFragment.getMapAsync(this);
    }
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user receives a prompt to install
     * Play services inside the SupportMapFragment. The API invokes this method after the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
            .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    private void initViews() {

        postsJson = new ArrayList<JSONObject>();
        posts = new ArrayList<PostData>();

        tags = new ArrayList<String>();
        area = "";
    }
    private void connect() {
        class AccessWebTask extends AsyncTask<URL, String, String> {
            protected String doInBackground(URL... urls) {
                try {
                    URL url = urls[0];
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();
                    Scanner in = new Scanner(url.openStream());
                    JSONParser parser = new JSONParser();
                    while (in.hasNext()) {
                        JSONArray o = (JSONArray) parser.parse(in.next());
                        Iterator<JSONObject> persons = o.iterator();
                        while (persons.hasNext()) {
                            JSONObject person = (JSONObject) persons.next();
                            Log.i("dfs", person.toJSONString());

                            postsJson.add(person);
                        }

                    }

                    return "good";
                } catch (Exception e) {
                    Log.i("PRINTOUT1", e.toString());
                }
                return "bad";
            }
        }
        try {
            URL url = new URL(PATH_DB + PATH_POSTS);
            AccessWebTask task = new AccessWebTask();
            task.execute(url);
            task.get();
        } catch (Exception e) {
            Log.i("PRINTOUT", "BAD CONNEffffCTION");
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
//        // Add a marker for the post
//        // and move the map's camera to the same location.
        LatLng posstLoc = new LatLng(0,0);
        googleMap.addMarker(new MarkerOptions().position(posstLoc));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(posstLoc));
        connect();
        try {
            PostsActivity posts = new PostsActivity();
            connect();
            Log.i("Posts", postsJson.size() +"");
            for(int i = 0; i < postsJson.size(); i++){
                Log.i("dfs", postsJson.get(i).toJSONString());
                Long l1 = (Long) postsJson.get(i).get("lat");
                Long l2 = (Long) postsJson.get(i).get("long");
                Double d1 = l1.doubleValue();
                Double d2 = l2.doubleValue();
                LatLng postLoc = new LatLng(d1 , d2);
                googleMap.addMarker(new MarkerOptions().position(postLoc)
                    .title("Post by " +postsJson.get(i).get("user")));
            }
        }
        catch (Exception e) {
            Log.i("PRINTOUTMAPSMARKER", e.getMessage());
        }
        for(Post post : Post.Posts){

        }

   }


}

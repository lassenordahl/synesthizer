package edu.uci.ics.fabflixmobile;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListViewActivity extends Activity {

    private EditText searchText;
    private Button searchButton;
    private Button prevButton;
    private Button nextButton;
    private String url;
    private ArrayList<Track> tracks = new ArrayList<>();
    private int offset = 0;
    private int limit = 20;
//    private String sortBy = "name";
    private String searchMode = "name";
//    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);

        searchText = findViewById(R.id.searchText);
        prevButton = findViewById(R.id.previousButton);
        nextButton = findViewById(R.id.nextButton);
        searchButton = findViewById(R.id.searchButton);

        url = "http://10.0.2.2:8080/unnamed/api/";

        //this should be retrieved from the database and the backend server
        final ArrayList<Movie> movies = new ArrayList<>();
        movies.add(new Movie("The Terminal", (short) 2004));
        movies.add(new Movie("The Final Season", (short) 2007));

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchTracks();
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (offset >= 20) {
                    offset -= 20;
                    searchTracks();
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                offset += 20;
                searchTracks();
            }
        });
    }

    // Initialize a new tracks adapter when we pull data
    public void setTracksAdapter(ArrayList<Track> tracks) {
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(null);

        MovieListViewAdapter adapter = new MovieListViewAdapter(tracks, this);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Track track = tracks.get(position);
                String message = String.format("Clicked on position: %d, name: %s", position, track.getName());
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void searchTracks() {
        String queryText = searchText.getText().toString();

        System.out.println(queryText);

        final RequestQueue queue = NetworkManager.sharedManager(this).queue;

        String getUrl = url + "tracks" + String.format("?offset=%d&limit=%d&searchMode=%s&search=%s&browseMode=Search+Mode", offset, limit, searchMode, searchText.getText().toString());

        //request type is POST
        final StringRequest loginRequest = new StringRequest(Request.Method.GET, getUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //TODO should parse the json response to redirect to appropriate functions.
                Log.d("search.success", response);
                Gson gson = new Gson();

                SongsResponse songsResponse = gson.fromJson(response, SongsResponse.class);
                tracks = songsResponse.getSongs();

                setTracksAdapter(tracks);
            }
        }, new Response.ErrorListener()            {
            @Override
            public void onErrorResponse(VolleyError error) {
                String hello = error.toString();
                Log.d("search.error", hello);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Post request form data
                final Map<String, String> params = new HashMap<>();
                params.put("offset", offset + "");
                params.put("limit", limit + "");
                params.put("searchMode", searchMode);
                params.put("search", searchText.getText().toString());
                Log.v("PARAMS", params.toString());
                return params;
            }
        };

        // !important: queue.add is where the login request is actually sent
        queue.add(loginRequest);
    }
}
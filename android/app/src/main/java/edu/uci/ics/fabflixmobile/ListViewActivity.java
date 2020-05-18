package edu.uci.ics.fabflixmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListViewActivity extends Activity {

    private EditText searchText;
    private Button searchButton;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);

        searchText = findViewById(R.id.searchText);
        searchButton = findViewById(R.id.searchButton);
        url = "http://10.0.2.2:8080/unnamed/api/";

        //this should be retrieved from the database and the backend server
        final ArrayList<Movie> movies = new ArrayList<>();
        movies.add(new Movie("The Terminal", (short) 2004));
        movies.add(new Movie("The Final Season", (short) 2007));

        final ArrayList<Track> tracks = new ArrayList<>();

        MovieListViewAdapter adapter = new MovieListViewAdapter(movies, this);

        ListView listView = findViewById(R.id.list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = movies.get(position);
                String message = String.format("Clicked on position: %d, name: %s, %d", position, movie.getName(), movie.getYear());
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchTracks();
            }
        });
    }

    public void searchTracks() {
        String queryText = searchText.getText().toString();

        System.out.println(queryText);

        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        //request type is POST
        final StringRequest loginRequest = new StringRequest(Request.Method.GET, url + "tracks", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //TODO should parse the json response to redirect to appropriate functions.
                Log.d("login.success", response);
                Gson gson = new Gson();

//                    JSONObject responseObject = new JSONObject(response);
                SongsResponse songsResponse = gson.fromJson(response, SongsResponse.class);

                //initialize the activity(page)/destination
//                Intent listPage = new Intent(Login.this, ListViewActivity.class);
                //without starting the activity/page, nothing would happen
//                startActivity(listPage);
            }
        }, new Response.ErrorListener()            {
            @Override
            public void onErrorResponse(VolleyError error) {
                String hello = error.toString();
                Log.d("login.error", hello);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Post request form data
                final Map<String, String> params = new HashMap<>();
//                params.put("username", username.getText().toString());
//                params.put("password", password.getText().toString());
                params.put("search", searchText.getText().toString());
//
//                return params;
                return null;
            }

            @Override
            public byte[] getBody() {
//                JSONObject params = new JSONObject();
//                try {
//                    params.put("email", username.getText().toString());
//                    params.put("password", password.getText().toString());
//                    params.put("appType", "android");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    return null;
//                }
//                try {
//                    return params == null ? null : params.toString().getBytes("utf-8");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                    return null;
//                }
                return null;
            }
        };

        // !important: queue.add is where the login request is actually sent
        queue.add(loginRequest);
    }
}
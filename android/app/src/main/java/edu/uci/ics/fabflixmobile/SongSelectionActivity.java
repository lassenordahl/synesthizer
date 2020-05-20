package edu.uci.ics.fabflixmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;


public class SongSelectionActivity extends Activity {

    TrackMeta trackMeta;
    Track track;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songselection);

        Intent intent = getIntent();
        String trackId = intent.getExtras().getString("trackId");
        String trackName = intent.getExtras().getString("trackName");
        String artistName = intent.getExtras().getString("artistName");
        String albumName = intent.getExtras().getString("albumName");

        getTrackMeta(trackId);

        TextView trackNameText = findViewById(R.id.trackName);
        trackNameText.setText(trackName);
        TextView trackIdText = findViewById(R.id.trackId);
        trackIdText.setText(trackId);
        TextView artistNameText = findViewById(R.id.artistName);
        artistNameText.setText(artistName);
        TextView albumNameText = findViewById(R.id.albumName);
        albumNameText.setText(albumName);
    }

    private void getTrack() {

    }

    private void getTrackMeta(String trackId) {
        String url = "http://10.0.2.2:8080/unnamed/api/track/meta?id=" + trackId;
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;

        final StringRequest metaRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //TODO should parse the json response to redirect to appropriate functions.
                Log.d("search.success", response);
                Gson gson = new Gson();
                trackMeta = gson.fromJson(response, TrackMeta.class);
                showTrackMeta(trackMeta);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String hello = error.toString();
                Log.d("search.error", hello);
            }
        });

        queue.add(metaRequest);
    }

    private void showTrackMeta(TrackMeta meta) {
        TextView danceability = findViewById(R.id.danceability);
        danceability.setText("Danceability: " + meta.getDanceability() + "");

        TextView duration_ms = findViewById(R.id.duration_ms);
        duration_ms.setText("Duration MS: " + meta.getDuration_ms() + "");

        TextView energy = findViewById(R.id.energy);
        energy.setText("Energy: " + meta.getEnergy() + "");

        TextView liveliness = findViewById(R.id.liveliness);
        liveliness.setText("Liveliness: " + meta.getLiveness() + "");

        TextView loudness = findViewById(R.id.loudness);
        loudness.setText("Loudness: " + meta.getLoudness() + "");

        TextView note = findViewById(R.id.note);
        note.setText("Note: " + meta.getNote() + "");

        TextView speechiness = findViewById(R.id.speechiness);
        speechiness.setText("Speechiness: " + meta.getSpeechiness() + "");

        TextView tempo = findViewById(R.id.tempo);
        tempo.setText("Tempo: " + meta.getTempo() + "");

        TextView time_signature = findViewById(R.id.time_signature);
        time_signature.setText("Time Signature: " + meta.getTime_signature() + "");
    }
}

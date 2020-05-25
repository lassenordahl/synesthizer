package edu.uci.ics.fabflixmobile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class MovieListViewAdapter extends ArrayAdapter<Track> {

    private ArrayList<Track> tracks;

    public MovieListViewAdapter(ArrayList<Track> tracks, Context context) {
        super(context, R.layout.row, tracks);
        this.tracks = tracks;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.row, parent, false);

        Track track = tracks.get(position);

        TextView nameView = view.findViewById(R.id.name);
        TextView artistNameView = view.findViewById(R.id.artistName);
        TextView albumNameView = view.findViewById(R.id.albumName);
        TextView popularityView = view.findViewById(R.id.popularity);
        TextView trackNumberView = view.findViewById(R.id.trackNumber);
//        TextView durationMsView = view.findViewById(R.id.durationMs);

        nameView.setText(track.getName());
        artistNameView.setText(track.getArtists().get(0).getName());// need to cast the year to a string to set the label
        albumNameView.setText(track.getAlbum().getName());
        popularityView.setText("Popularity: " + track.getPopularity());
        trackNumberView.setText("Track Number: " + track.getTrack_number());
//        durationMsView.setText(track.getDuration_ms() + "");

        return view;
    }
}
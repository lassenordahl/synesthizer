package com.cs122b.service;

import com.cs122b.client.Query;
import com.cs122b.client.SQLClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.naming.NamingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GenreService {

    public static List<String> fetchGenres() throws SQLException, NamingException {
        SQLClient db = new SQLClient();

        // Don't need to
        Query query = db.query("SELECT DISTINCT genre FROM artist_in_genre ORDER BY genre");

        List<String> genres = new ArrayList<String>();
        ResultSet result = query.getResult();
        while (result.next()) {
            genres.add(result.getString("genre"));
        }

        query.closeQuery();
        db.closeConnection();
        return genres;
    }
}
package com.cs122b.service;

import com.cs122b.client.SQLClient;
import com.cs122b.client.Query;
import com.cs122b.model.DatabaseMeta;
import com.cs122b.model.DatabaseTable;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseService {

    public static DatabaseMeta getDatabaseMeta() throws SQLException {
        SQLClient db = new SQLClient();
        DatabaseMeta databaseMeta = new DatabaseMeta();

        Query query = db.query("SELECT (SELECT COUNT(*) FROM track) as track_count, (SELECT COUNT(*) FROM album) as album_count, (SELECT COUNT(*) FROM artist) as artist_count");
        ResultSet result = query.getResult();
        result.next();

        ArrayList<DatabaseTable> dbTables = new ArrayList<DatabaseTable>();

        for (String tableName: db.getTables()) {
            DatabaseTable table = new DatabaseTable();
            table.setName(tableName);
            dbTables.add(table);
        }

        for (DatabaseTable dbTable: dbTables) {
            ArrayList<String> attributes = db.getColumns(dbTable.getName());
            dbTable.setAttributes(attributes);
        }

        databaseMeta.setTables(dbTables);

        databaseMeta.setTrack_count(result.getInt("track_count"));
        databaseMeta.setAlbum_count(result.getInt("album_count"));
        databaseMeta.setArtist_count(result.getInt("artist_count"));

        return databaseMeta;
    }
}
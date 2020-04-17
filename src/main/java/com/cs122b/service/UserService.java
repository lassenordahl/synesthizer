package com.cs122b.service;

import com.cs122b.client.SQLClient;
import com.cs122b.client.Query;
import com.cs122b.model.User;
import com.google.gson.JsonObject;

import java.sql.*;

public class UserService {
    private static SQLClient db;

    private static void setUserAttrs(User user, ResultSet result) throws SQLException {
        user.setId(result.getInt("id"));
        user.setFirst_name(result.getString("first_name"));
        user.setLast_name(result.getString("last_name"));
        user.setAddress(result.getString("address"));
        user.setEmail(result.getString("email"));
        user.setPassword(result.getString("password"));
    }

    private static void insertUser(User user) throws SQLException {
        db = new SQLClient();

        Query insertQuery = db.query(String.format("INSERT INTO user VALUES(0,'%s','%s','%s','%s','%s')",
                user.getFirst_name(), user.getLast_name(), user.getAddress(), user.getEmail(), user.getPassword()));

        insertQuery.closeQuery();

        Query createdUserQuery = db.query(String.format("SELECT *  FROM user WHERE email='%s'", user.getEmail()));

        ResultSet result = createdUserQuery.getResult();

        result.next();

        user.setId(result.getInt("id"));
    }

    public static User authenticateUser(String email, String password) throws SQLException {
        db = new SQLClient();

        Query query = db
                .query(String.format("SELECT *  FROM user WHERE email='%s' AND password='%s'", email, password));

        User user = new User();
        ResultSet result = query.getResult();

        if (result.next() == false) {
            return null;
        }

        setUserAttrs(user, result);

        query.closeQuery();
        db.closeConnection();
        return user;
    }

    public static User createUser(JsonObject userJson) throws SQLException {
        db = new SQLClient();

        String email = userJson.get("email").getAsString();
        // Check if exists (return null if exists)
        Query query = db.query(String.format("SELECT *  FROM user WHERE email='%s'", email));
        ResultSet result = query.getResult();
        if (result.next() == false) {
            return null;
        }

        query.closeQuery();

        // Create user
        User user = new User();

        user.setFirst_name(userJson.get("first_name").getAsString());
        user.setLast_name(userJson.get("last_name").getAsString());
        user.setAddress(userJson.get("address").getAsString());
        user.setEmail(userJson.get("email").getAsString());
        user.setPassword(userJson.get("password").getAsString());

        insertUser(user);

        db.closeConnection();
        return user;
    }

    public static User fetchUser(int id) throws SQLException {
        db = new SQLClient();

        Query query = db.query(String.format("SELECT *  FROM user WHERE id='%d'", id));

        User user = new User();
        ResultSet result = query.getResult();

        if (result.next() == false) {
            return null;
        }

        setUserAttrs(user, result);

        query.closeQuery();
        db.closeConnection();
        return user;
    }
}
package com.cs122b.service;

import com.cs122b.client.SQLClient;
import com.cs122b.client.Query;
import com.cs122b.model.User;
import com.google.gson.JsonObject;
import org.jasypt.util.password.PasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;

import java.sql.*;

public class UserService {

    private static void setUserAttrs(User user, ResultSet result) throws SQLException {
        user.setId(result.getInt("id"));
        user.setFirst_name(result.getString("first_name"));
        user.setLast_name(result.getString("last_name"));
        user.setAddress(result.getString("address"));
        user.setEmail(result.getString("email"));
        user.setPassword(result.getString("password"));
    }

    private static void insertUser(SQLClient db, User user) throws SQLException {
        String insertQuery = "INSERT INTO user(id, first_name, last_name, address, email, password) "
                + "VALUES(DEFAULT,?,?,?,?,?);";

        PreparedStatement pstmt = db.getConnection().prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);

        pstmt.setString(1, user.getFirst_name());
        pstmt.setString(2, user.getLast_name());
        pstmt.setString(3, user.getAddress());
        pstmt.setString(4, user.getEmail());
        pstmt.setString(5, user.getPassword());

        int affectedRows = pstmt.executeUpdate();

        if (affectedRows > 0) {
            // get the ID back
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                user.setId(rs.getInt(1));
            }
        }

        pstmt.close();
    }

    public static User createUser(JsonObject userJson) throws SQLException {
        SQLClient db = new SQLClient();

        String email = userJson.get("email").getAsString();
        // Check if exists (return null if exists)
        Query query = db.query(String.format("SELECT *  FROM user WHERE email='%s'", email));
        ResultSet result = query.getResult();
        if (result.next() != false) {
            db.closeConnection();
            return null;
        }

        query.closeQuery();

        // Create user
        User user = new User();

        user.setFirst_name(userJson.get("first_name").getAsString());
        user.setLast_name(userJson.get("last_name").getAsString());
        user.setAddress(userJson.get("address").getAsString());
        user.setEmail(userJson.get("email").getAsString());

        // Need to encrypt the password
        PasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        String encryptedPassword = passwordEncryptor.encryptPassword(userJson.get("password").getAsString());
        user.setPassword(encryptedPassword);

        insertUser(db, user);

        db.closeConnection();
        return user;
    }

    public static User updateUser(JsonObject userJson) throws SQLException {
        SQLClient db = new SQLClient();

        // Create user object
        User user = new User();

        user.setId(userJson.get("user_id").getAsInt());
        user.setFirst_name(userJson.get("first_name").getAsString());
        user.setLast_name(userJson.get("last_name").getAsString());
        user.setAddress(userJson.get("address").getAsString());
        user.setEmail(userJson.get("email").getAsString());

        String email = user.getEmail();
        // Check if exists (return null if exists)
        Query query = db.query(String.format("SELECT * FROM user WHERE email='%s' AND id <> %d", email, user.getId()));
        ResultSet result = query.getResult();
        if (result.next() != false) {
            db.closeConnection();
            return null;
        }

        query.closeQuery();

        if (userJson.get("password").getAsString().length() > 0) {
            // Encrypt password
            PasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
            String encryptedPassword = passwordEncryptor.encryptPassword(userJson.get("password").getAsString());
            user.setPassword(encryptedPassword);
        }

        StringBuilder updateQuery = new StringBuilder();
        updateQuery.append("UPDATE user SET first_name = ?, last_name = ?, address = ?, email = ? ");

        if (user.getPassword() != null) {
            updateQuery.append(", password = ? ");
        }

        updateQuery.append("WHERE id = " + user.getId() + ";");

        PreparedStatement pstmt = db.getConnection().prepareStatement(updateQuery.toString(),
                Statement.RETURN_GENERATED_KEYS);

        pstmt.setString(1, user.getFirst_name());
        pstmt.setString(2, user.getLast_name());
        pstmt.setString(3, user.getAddress());
        pstmt.setString(4, user.getEmail());

        if (user.getPassword() != null) {
            pstmt.setString(5, user.getPassword());
        }

        int affectedRows = pstmt.executeUpdate();

        if (affectedRows > 0) {
            // get the ID back
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                user.setId(rs.getInt(1));
            }
        }

        pstmt.close();
        db.closeConnection();
        return user;
    }

    public static User authenticateUser(String email, String password) throws SQLException {
        SQLClient db = new SQLClient();

        // Need to authenticate for encrypted passwords now, (no previous passwords will work until we encrypt them)
        PasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();

        Query query = db
                .query(String.format("SELECT *  FROM user WHERE email='%s'", email));

        User user = new User();
        ResultSet result = query.getResult();

        if (result.next() == false) {
            db.closeConnection();
            return null;
        }

        String encryptedPassword = result.getString("password");
        if (!passwordEncryptor.checkPassword(password, encryptedPassword)) {
            db.closeConnection();
            return null;
        }

        setUserAttrs(user, result);

        query.closeQuery();
        db.closeConnection();
        return user;
    }

    public static User fetchUser(int id) throws SQLException {
        SQLClient db = new SQLClient();

        Query query = db.query(String.format("SELECT * FROM user WHERE id='%d'", id));

        User user = new User();
        ResultSet result = query.getResult();

        if (result.next() == false) {
            db.closeConnection();
            return null;
        }

        setUserAttrs(user, result);

        query.closeQuery();
        db.closeConnection();
        return user;
    }
}
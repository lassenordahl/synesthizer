package com.cs122b.service;

import com.cs122b.client.SQLClient;
import com.cs122b.client.Query;
import com.cs122b.model.User;
import com.google.gson.JsonObject;
import org.jasypt.util.password.PasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;

import javax.naming.NamingException;
import java.sql.*;

public class UserService {

    private static void setUserAttrs(User user, ResultSet result) throws SQLException, NamingException {
        user.setId(result.getInt("id"));
        user.setFirst_name(result.getString("first_name"));
        user.setLast_name(result.getString("last_name"));
        user.setAddress(result.getString("address"));
        user.setEmail(result.getString("email"));
        user.setPassword(result.getString("password"));
    }

    private static void insertUser(SQLClient db, User user) throws SQLException, NamingException {
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

    public static User createUser(JsonObject userJson) throws SQLException, NamingException {
        SQLClient db = new SQLClient();

        String email = userJson.get("email").getAsString();

        String query = "SELECT * FROM user WHERE email=?";
        PreparedStatement pstmt = db.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, email);

        String query2 = "SELECT * FROM employee WHERE email=?";
        PreparedStatement pstmt2 = db.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        pstmt2.setString(1, email);

        ResultSet rs = pstmt.executeQuery();
        ResultSet rs2 = pstmt2.executeQuery();
        if (rs.next() != false || rs2.next() != false) {
            db.closeConnection();
            pstmt.close();
            pstmt2.close();
            return null;
        }

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

        pstmt.close();
        pstmt2.close();
        db.closeConnection();
        return user;
    }

    public static User updateUser(JsonObject userJson) throws SQLException, NamingException {
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
        String query = "SELECT * FROM user, employee WHERE (user.email=? AND user.id <> ?) OR employee.email=?";
        PreparedStatement pstmt = db.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, email);
        pstmt.setInt(2, user.getId());
        pstmt.setString(3, email);

        ResultSet rs = pstmt.executeQuery();

        if (rs.next() != false) {
            pstmt.close();
            db.closeConnection();
            return null;
        }

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

        updateQuery.append("WHERE id = ?;");

        PreparedStatement pstmt2 = db.getConnection().prepareStatement(updateQuery.toString(),
                Statement.RETURN_GENERATED_KEYS);
        pstmt2.setInt(1, user.getId());
        pstmt2.setString(2, user.getFirst_name());
        pstmt2.setString(3, user.getLast_name());
        pstmt2.setString(4, user.getAddress());
        pstmt2.setString(5, user.getEmail());

        if (user.getPassword() != null) {
            pstmt2.setString(6, user.getPassword());
        }

        int affectedRows = pstmt2.executeUpdate();

        if (affectedRows > 0) {
            // get the ID back
            ResultSet rs2 = pstmt.getGeneratedKeys();
            if (rs2.next()) {
                user.setId(rs2.getInt(1));
            }
        }

        pstmt.close();
        pstmt2.close();
        db.closeConnection();
        return user;
    }

    public static User authenticateUser(String email, String password) throws SQLException, NamingException {
        SQLClient db = new SQLClient();

        // Need to authenticate for encrypted passwords now, (no previous passwords will
        // work until we encrypt them)
        PasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();

        String authQuery = "SELECT * FROM user WHERE email=?";
        PreparedStatement pstmt = db.getConnection().prepareStatement(authQuery, Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, email);

        ResultSet result = pstmt.executeQuery();
        if (result.next() == false) {
            pstmt.close();
            db.closeConnection();
            return null;
        }

        User user = new User();

        String encryptedPassword = result.getString("password");
        if (!passwordEncryptor.checkPassword(password, encryptedPassword)) {
            db.closeConnection();
            return null;
        }

        setUserAttrs(user, result);

        pstmt.close();
        db.closeConnection();
        return user;
    }

    public static User fetchUser(int id) throws SQLException, NamingException {
        SQLClient db = new SQLClient();

        String query = "SELECT * FROM user WHERE id=?";

        PreparedStatement pstmt = db.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        pstmt.setInt(1, id);

        ResultSet result = pstmt.executeQuery();
        if (result.next() == false) {
            pstmt.close();
            db.closeConnection();
            return null;
        }

        User user = new User();

        setUserAttrs(user, result);

        pstmt.close();
        db.closeConnection();
        return user;
    }
}
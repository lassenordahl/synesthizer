package com.cs122b.service;

import com.cs122b.client.SQLClient;
import com.cs122b.client.Query;
import com.cs122b.model.Employee;
import com.google.gson.JsonObject;
import org.jasypt.util.password.PasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;

import javax.naming.NamingException;
import java.sql.*;

public class EmployeeService {

    private static void setEmployeeAttrs(Employee employee, ResultSet result) throws SQLException, NamingException {
        employee.setId(result.getInt("id"));
        employee.setFirst_name(result.getString("first_name"));
        employee.setLast_name(result.getString("last_name"));
        employee.setEmail(result.getString("email"));
        employee.setPassword(result.getString("password"));
    }

    private static void insertEmployee(SQLClient db, Employee employee) throws SQLException, NamingException {
        String insertQuery = "INSERT INTO employee(id, first_name, last_name, email, password) "
                + "VALUES(DEFAULT,?,?,?,?);";

        PreparedStatement pstmt = db.getConnection().prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);

        pstmt.setString(1, employee.getFirst_name());
        pstmt.setString(2, employee.getLast_name());
        pstmt.setString(3, employee.getEmail());
        pstmt.setString(4, employee.getPassword());

        int affectedRows = pstmt.executeUpdate();

        if (affectedRows > 0) {
            // get the ID back
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                employee.setId(rs.getInt(1));
            }
        }

        pstmt.close();
    }

    public static Employee createEmployee(JsonObject employeeJson) throws SQLException, NamingException {
        SQLClient db = new SQLClient();

        String email = employeeJson.get("email").getAsString();
        // Check if exists (return null if exists)

        String query = "SELECT * FROM user WHERE email=?";
        PreparedStatement pstmt = db.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, email);

        String query2 = "SELECT * FROM employee WHERE email=?";
        PreparedStatement pstmt2 = db.getConnection().prepareStatement(query2, Statement.RETURN_GENERATED_KEYS);
        pstmt2.setString(1, email);

        ResultSet result = pstmt.executeQuery();
        ResultSet result2 = pstmt2.executeQuery();
        if (result.next() != false || result2.next() != false) {
            db.closeConnection();
            pstmt.close();
            pstmt2.close();
            return null;
        }

        // Create employee
        Employee employee = new Employee();

        employee.setFirst_name(employeeJson.get("first_name").getAsString());
        employee.setLast_name(employeeJson.get("last_name").getAsString());
        employee.setEmail(employeeJson.get("email").getAsString());

        // Need to encrypt the password
        PasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        String encryptedPassword = passwordEncryptor.encryptPassword(employeeJson.get("password").getAsString());
        employee.setPassword(encryptedPassword);

        insertEmployee(db, employee);
        pstmt.close();
        pstmt2.close();
        db.closeConnection();
        return employee;
    }

    public static Employee authenticateEmployee(String email, String password) throws SQLException, NamingException {
        SQLClient db = new SQLClient();

        PasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();

        String authQuery = "SELECT * FROM employee WHERE email=?";

        PreparedStatement pstmt = db.getConnection().prepareStatement(authQuery, Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, email);

        ResultSet result = pstmt.executeQuery();

        if (result.next() == false) {
            pstmt.close();
            db.closeConnection();
            return null;
        }

        Employee employee = new Employee();

        String encryptedPassword = result.getString("password");
        if (!passwordEncryptor.checkPassword(password, encryptedPassword)) {
            db.closeConnection();
            return null;
        }

        setEmployeeAttrs(employee, result);

        pstmt.close();
        db.closeConnection();
        return employee;
    }

    public static Employee fetchEmployee(int id) throws SQLException, NamingException {
        SQLClient db = new SQLClient();

        String query = "SELECT * FROM employee WHERE id=?";

        PreparedStatement pstmt = db.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        pstmt.setInt(1, id);

        ResultSet result = pstmt.executeQuery();
        if (result.next() == false) {
            pstmt.close();
            db.closeConnection();
            return null;
        }

        Employee employee = new Employee();

        setEmployeeAttrs(employee, result);

        pstmt.close();
        db.closeConnection();
        return employee;
    }
}
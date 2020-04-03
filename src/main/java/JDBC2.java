//JDBC Example - deleting a record

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class JDBC2 {
    public static void main(String[] arg) throws Exception {
        // Incorporate mySQL driver
        Class.forName("com.mysql.jdbc.Driver").newInstance();

        // Connect to the test database
        Connection connection = DriverManager.getConnection("jdbc:" + Parameters.dbtype + ":///" + Parameters.dbname + "?autoReconnect=true&useSSL=false",
                Parameters.username, Parameters.password);

        if (connection != null) {
            System.out.println("Connection established!!");
            System.out.println();
        }

        // create update DB statement -- deleting second record of table; return status
        Statement update = connection.createStatement();

        String deleteQuery = "delete from stars where id='755011'";

        int retID = update.executeUpdate(deleteQuery);

        // returns: either (1) the row count for SQL Data Manipulation Language (DML) statements or (2) 0 for SQL statements that return nothing
        System.out.println("retID = " + retID);
    }
}
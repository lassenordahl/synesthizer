import java.sql.*;

public class JDBC1 implements Parameters {

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

        // Create an execute an SQL statement to select all of table"Stars" records
        Statement select = connection.createStatement();
        String query = "Select * from stars";
        ResultSet result = select.executeQuery(query);

        // Get metatdata from stars; print # of attributes in table
        System.out.println("The results of the query");
        ResultSetMetaData metadata = result.getMetaData();
        System.out.println("There are " + metadata.getColumnCount() + " columns");

        // Print type of each attribute
        for (int i = 1; i <= metadata.getColumnCount(); i++)
            System.out.println("Type of column " + i + " is " + metadata.getColumnTypeName(i));

        // print table's contents, field by field
        while (result.next()) {
            System.out.println("Id = " + result.getString("id"));
            System.out.println("Name = " + result.getString("name"));
            System.out.println("birthYear = " + result.getInt("birthYear"));
            System.out.println();
        }

        // should close the connection when the program exits
    }
}
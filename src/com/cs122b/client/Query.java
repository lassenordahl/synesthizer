package com.cs122b.client;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Query {
    private Statement statement;
    private ResultSet result;

    public Query(Statement statement, String query) throws SQLException {
        this.statement = statement;
        this.result = this.statement.executeQuery(query);
    }

    public ResultSet getResult() {
        return result;
    }

    public void closeQuery() throws SQLException {
        statement.close();
        result.close();
    }
}

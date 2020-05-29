package com.cs122b.web;

import com.google.gson.Gson;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.cs122b.model.DatabaseMeta;
import com.cs122b.service.DatabaseService;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(name = "DatabaseMetaServlet", urlPatterns = { "/database/meta" })
public class DatabaseMetaServlet extends HttpServlet {
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        DatabaseMeta dbMeta = null;
        try {
            dbMeta = DatabaseService.getDatabaseMeta();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        PrintWriter out = response.getWriter();

        if (dbMeta == null) {
            response.setStatus(404);
            out.print("{ \"message\": \"resource not found\"}");
        } else {
            String dbResponse = this.gson.toJson(dbMeta);
            out.print("{ \"meta\": " + dbResponse + " }");
        }
    }

}

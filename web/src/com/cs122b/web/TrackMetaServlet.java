package com.cs122b.web;

import com.cs122b.model.TrackMeta;
import com.cs122b.service.TrackService;
import com.google.gson.Gson;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import static com.cs122b.service.TrackService.*;

@WebServlet(name = "TrackMetaServlet", urlPatterns = { "/track/meta" })
public class TrackMetaServlet extends HttpServlet {
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        TrackMeta trackMeta = null;
        try {
            trackMeta = fetchTrackMeta(request.getParameter("id"));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }

        PrintWriter out = response.getWriter();

        if (trackMeta == null) {
            response.setStatus(404);
            out.print("{ \"message\": \"resource not found\"}");
        } else {
            String trackResponse = this.gson.toJson(trackMeta);
            out.print(trackResponse);
        }
    }
}

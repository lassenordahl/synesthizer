package com.cs122b.web;

import com.cs122b.model.Track;
import com.cs122b.service.TrackService;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "TrackServlet", urlPatterns = {"/tracks"})
public class TrackServlet extends HttpServlet {
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");


        List<Track> tracks = null;
        try {
            tracks = TrackService.fetchTracks(0, 20);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String tracksResponse = this.gson.toJson(tracks);

        PrintWriter out = response.getWriter();
        out.print(tracksResponse);
    }
}

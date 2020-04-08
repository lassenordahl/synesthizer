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

import static com.cs122b.service.TrackService.*;

@WebServlet(name = "TrackServlet", urlPatterns = { "/tracks" })
public class TracksServlet extends HttpServlet {
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String offset = request.getParameter("offset");
        String limit = request.getParameter("limit");
        String sortBy = request.getParameter("sortBy");


        List<Track> tracks = null;
        try {
            tracks = fetchTracks(offset != null ? Integer.parseInt(offset):  0,
                                        limit != null ? Integer.parseInt(limit) : 30,
                                        sortBy != null ? sortBy : "name");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        PrintWriter out = response.getWriter();

        if (tracks.size() > 0) {
            String tracksResponse = this.gson.toJson(tracks);
            out.print("{ \"songs\": " + tracksResponse + " }");
        } else {
            response.setStatus(404);
            out.print("{ \"message\": \"resource not found\"}");
        }
    }
}

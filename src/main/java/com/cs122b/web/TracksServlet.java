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
        String searchMode = request.getParameter("searchMode");
        String search = request.getParameter("search");

        List<Track> tracks = null;
        try {
            tracks = fetchTracks(offset != null && offset != "" ? Integer.parseInt(offset) : 0,
                    limit != null && limit != "" ? Integer.parseInt(limit) : 20,
                    sortBy != null && sortBy != "" ? sortBy : "popularity desc",
                    searchMode != null && searchMode != "" ? searchMode : null,
                    search != null && search != "" ? search : null);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        PrintWriter out = response.getWriter();

        String tracksResponse = this.gson.toJson(tracks);
        out.print("{ \"songs\": " + tracksResponse + " }");
    }
}

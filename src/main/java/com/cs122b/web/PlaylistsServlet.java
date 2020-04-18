package com.cs122b.web;

import com.cs122b.model.Playlist;
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

import com.cs122b.service.PlaylistService;

@WebServlet(name = "PlaylistsServlet", urlPatterns = { "/playlists" })
public class PlaylistsServlet extends HttpServlet {
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        int userId = (Integer) request.getSession().getAttribute("user_id");

        String offset = request.getParameter("offset");
        String limit = request.getParameter("limit");

        List<Playlist> playlists = null;
        try {
            playlists = PlaylistService.fetchPlaylists(userId, offset != null ? Integer.parseInt(offset) : 0,
                    limit != null ? Integer.parseInt(limit) : 30);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        PrintWriter out = response.getWriter();

        if (playlists.size() > 0) {
            String tracksResponse = this.gson.toJson(playlists);
            out.print("{ \"playlists\": " + tracksResponse + " }");
        } else {
            response.setStatus(404);
            out.print("{ \"message\": \"resource not found\"}");
        }
    }
}

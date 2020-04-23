package com.cs122b.web;

import com.cs122b.service.PlaylistService;
import com.cs122b.model.Playlist;
import com.cs122b.utils.JsonParse;

import com.google.gson.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "PlaylistServlet", urlPatterns = "/playlist")
public class PlaylistServlet extends HttpServlet {
    private Gson gson = new Gson();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // DEV
//        int userId = (Integer) request.getSession().getAttribute("user_id");

        JsonObject jsonRequestBody = JsonParse.toJson(request.getReader());

        PrintWriter out = response.getWriter();

        Playlist playlist = null;
        try {
            //            DEV
            playlist = PlaylistService.createPlaylist(jsonRequestBody, 1);

//            playlist = PlaylistService.createPlaylist(jsonRequestBody, userId);
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(400);
            out.write("{ \"message\": \"resource not created\"}");
            return;
        }

        String userResponse = this.gson.toJson(playlist);
        out.write(userResponse);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        int userId = (Integer) request.getSession().getAttribute("user_id");
        // Need to add checking for ownership
        int playlistId = Integer.parseInt(request.getParameter("id"));

        Playlist user = null;
        try {
            user = PlaylistService.fetchPlaylist(playlistId, userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        PrintWriter out = response.getWriter();

        if (user == null) {
            response.setStatus(404);
            out.print("{ \"message\": \"resource not found\"}");
        } else {
            String trackResponse = this.gson.toJson(user);
            out.print(trackResponse);
        }
    }
}
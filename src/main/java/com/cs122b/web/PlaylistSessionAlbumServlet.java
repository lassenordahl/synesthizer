package com.cs122b.web;

import com.cs122b.model.Playlist;
import com.cs122b.model.Album;
import com.cs122b.service.AlbumService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.sql.SQLException;

@WebServlet(name = "PlaylistSessionAlbumServlet", urlPatterns = { "/playlist/session/album" })
public class PlaylistSessionAlbumServlet extends HttpServlet {
    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        JsonObject jsonRequestBody = com.cs122b.utils.JsonParse.toJson(request.getReader());

        Album album = null;

        try {
            album = AlbumService.fetchAlbum(jsonRequestBody.get("id").getAsString());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        HttpSession session = request.getSession();
        Playlist sessionPlaylist = (Playlist) session.getAttribute("sessionPlaylist");

        if (album == null || sessionPlaylist == null) {
            response.setStatus(404);
            out.print("{ \"message\": \"Resource could not be added\"}");
        } else {
            sessionPlaylist.addAlbum(album);
            response.setStatus(200);
            out.print("{ \"message\": \"Track added to session\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        Playlist sessionPlaylist = (Playlist) session.getAttribute("sessionPlaylist");

        if (sessionPlaylist == null) {
            response.setStatus(404);
            out.print("{ \"message\": \"Resource could not be removed\"}");
        } else {
            sessionPlaylist.removeAlbum(request.getParameter("id"));
            response.setStatus(200);
            out.print("{ \"message\": \"Track removed from session\"}");
        }
    }
}

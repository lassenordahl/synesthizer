package com.cs122b.web;

import com.cs122b.model.Playlist;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "PlaylistSessionServlet", urlPatterns = { "/playlist/session" })
public class PlaylistSessionServlet extends HttpServlet {
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        // Get a instance of current session on the request
        HttpSession session = request.getSession();

        Playlist sessionPlaylist = (Playlist) session.getAttribute("sessionPlaylist");

        if (sessionPlaylist == null) {
            session.setAttribute("sessionPlaylist", new Playlist());
        }

        // If we don't have an available session playlist, we send a blank one instead
        if (sessionPlaylist == null) {
            String playlistResponse = this.gson.toJson(new Playlist());
            out.print(playlistResponse);
        } else {
            String playlistResponse = this.gson.toJson(sessionPlaylist);
            out.print(playlistResponse);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        // Get a instance of current session on the request
        HttpSession session = request.getSession();

        Playlist sessionPlaylist = (Playlist) session.getAttribute("sessionPlaylist");

        if (sessionPlaylist == null) {
            session.setAttribute("sessionPlaylist", new Playlist());
        }

        // If we don't have an available session playlist, we send a blank one instead
        if (sessionPlaylist == null) {
            String playlistResponse = this.gson.toJson(new Playlist());
            out.print(playlistResponse);
        } else {
            String playlistResponse = this.gson.toJson(sessionPlaylist);
            out.print(playlistResponse);
        }
    }

}

package com.cs122b.web;

import com.cs122b.model.Album;
import com.cs122b.service.AlbumService;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(name = "AlbumServlet", urlPatterns = {"/albums"})
public class AlbumServlet extends HttpServlet {
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String albumId = request.getParameter("id");

        Album album = null;

        try {
            album = AlbumService.fetchAlbum(albumId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String tracksResponse = this.gson.toJson(album);

        PrintWriter out = response.getWriter();
        out.print(tracksResponse);
    }
}

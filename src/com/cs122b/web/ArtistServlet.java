package com.cs122b.web;

import com.cs122b.model.Artist;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import static com.cs122b.service.ArtistService.fetchArtist;

@WebServlet(name = "ArtistServlet", urlPatterns = { "/artist" })
public class ArtistServlet extends HttpServlet {
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Artist artist = null;
        try {
            artist = fetchArtist(request.getParameter("id"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        PrintWriter out = response.getWriter();

        if (artist == null) {
            response.setStatus(404);
            out.print("{ \"message\": \"resource not found\"}");
        } else {
            String artistResponse = this.gson.toJson(artist);
            out.print("{ \"artist\": " + artistResponse + " }");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String insertionResponse = "";
        try {
            insertionResponse = insertArtist(request.getParameter("id"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        PrintWriter out = response.getWriter();

        if (artist == null) {
            response.setStatus(404);
            out.print("{ \"message\": \"resource not found\"}");
        } else {
            String artistResponse = this.gson.toJson(artist);
            out.print("{ \"artist\": " + artistResponse + " }");
        }
    }
}

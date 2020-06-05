package com.cs122b.web;

import com.cs122b.model.Album;
import com.cs122b.service.AlbumService;
import com.cs122b.service.ArtistService;
import com.cs122b.utils.JsonParse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.Naming;
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
        } catch (NamingException e) {
            e.printStackTrace();
        }

        String tracksResponse = this.gson.toJson(album);

        PrintWriter out = response.getWriter();
        out.print(tracksResponse);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        JsonObject jsonRequestBody = JsonParse.toJson(request.getReader());

        String insertionResponse = "";
        try {
            insertionResponse = AlbumService.insertAlbum(
                    jsonRequestBody.get("id").getAsString(),
                    jsonRequestBody.get("name").getAsString(),
                    jsonRequestBody.get("image").getAsString(),
                    jsonRequestBody.get("album_type").getAsString(),
                    jsonRequestBody.get("release_date").getAsString(),
                    jsonRequestBody.get("artist_id").getAsString()
            );
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(404);
            out.print("{ \"message\": \"A SQL Error has occured\"}");
        } catch (NamingException e) {
            e.printStackTrace();
        }

        if (insertionResponse.equalsIgnoreCase("duplicate id")) {
            response.setStatus(403);
            out.print("{ \"message\": \"Inserted artist already exists\"}");
        } else {
            out.print("{ \"id\": \"" + insertionResponse + "\" }");
        }
    }
}

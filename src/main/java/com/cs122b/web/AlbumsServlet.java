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
import java.util.List;

@WebServlet(name = "AlbumsServlet", urlPatterns = { "/albums" })
public class AlbumsServlet extends HttpServlet {
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
        String name = request.getParameter("name");
        String artist_id = request.getParameter("artist_id");

        List<Album> albums = null;
        try {
            albums = AlbumService.fetchAlbums(offset != null && offset != "" ? Integer.parseInt(offset) : 0,
                    limit != null && limit != "" ? Integer.parseInt(limit) : 20,
                    sortBy != null && sortBy != "" ? sortBy : "popularity desc",
                    searchMode != null && searchMode != "" ? searchMode : null,
                    search != null && search != "" ? search : null, name != null && name != "" ? name : null,
                    artist_id != null && artist_id != "" ? artist_id : null);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        PrintWriter out = response.getWriter();

        String albumsResponse = this.gson.toJson(albums);
        out.print("{ \"albums\": " + albumsResponse + " }");
    }
}

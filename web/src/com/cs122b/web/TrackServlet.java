package com.cs122b.web;

import com.cs122b.model.Track;
import com.cs122b.service.TrackService;
import com.cs122b.utils.JsonParse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import static com.cs122b.service.TrackService.fetchTrack;

@WebServlet(name = "TrackServlet", urlPatterns = { "/track" })
public class TrackServlet extends HttpServlet {
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Track track = null;
        try {
            track = fetchTrack(request.getParameter("id"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        PrintWriter out = response.getWriter();

        if (track == null) {
            response.setStatus(404);
            out.print("{ \"message\": \"resource not found\"}");
        } else {
            String trackResponse = this.gson.toJson(track);
            out.print("{ \"song\": " + trackResponse + " }");
        }
    }

//    public static String insertTrack(String id, String name, int track_number, String album_id, String artist_id) throws SQLException {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        JsonObject jsonRequestBody = JsonParse.toJson(request.getReader());

        String insertionResponse = "";
        try {
            insertionResponse = TrackService.insertTrack(
                    jsonRequestBody.get("id").getAsString(),
                    jsonRequestBody.get("name").getAsString(),
                    jsonRequestBody.get("track_number").getAsInt(),
                    jsonRequestBody.get("duration_ms").getAsInt(),
                    jsonRequestBody.get("album_id").getAsString(),
                    jsonRequestBody.get("artist_id").getAsString()
            );
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(404);
            out.print("{ \"message\": \"A SQL Error has occured\"}");
        }

        if (insertionResponse.equalsIgnoreCase("duplicate id")) {
            response.setStatus(403);
            out.print("{ \"message\": \"Inserted artist already exists\"}");
        } else {
            out.print("{ \"id\": \"" + insertionResponse + "\" }");
        }
    }
}

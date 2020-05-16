package com.cs122b.web;

import com.cs122b.model.Playlist;
import com.cs122b.service.PlaylistService;
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

@WebServlet(name = "PkaylistSnapshotServlet", urlPatterns = { "/playlist/snapshot" })
public class PlaylistSnapshotServlet extends HttpServlet {
    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        JsonObject jsonRequestBody = com.cs122b.utils.JsonParse.toJson(request.getReader());

        try {
            PlaylistService.insertSnapshot(jsonRequestBody.get("playlistId").getAsString(), jsonRequestBody.get("snapshotId").getAsString());
            response.setStatus(200);
            out.print("{ \"message\": \"Snapshot added to playlist\"}");
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(404);
            out.print("{ \"message\": \"Resource could not be added\"}");
        }
    }
}

package com.cs122b.web;

import com.cs122b.model.Track;
import com.google.gson.Gson;

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
            out.print("{ \"message\": \"resource not found \"}");
        } else {
            String tracksResponse = this.gson.toJson(track);
            out.print("{ \"song\": " + tracksResponse + " }");
        }
    }

}

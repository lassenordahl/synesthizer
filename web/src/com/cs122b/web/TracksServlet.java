package com.cs122b.web;

import com.cs122b.model.Track;
import com.cs122b.service.TrackService;
import com.google.gson.Gson;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import static com.cs122b.service.TrackService.*;
import com.cs122b.utils.MyLogger;

@WebServlet(name = "TrackServlet", urlPatterns = { "/tracks" })
public class TracksServlet extends HttpServlet {
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        long startTime = System.nanoTime();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String offset = request.getParameter("offset");
        String limit = request.getParameter("limit");
        String sortBy = request.getParameter("sortBy");
        String searchMode = request.getParameter("searchMode");
        String search = request.getParameter("search");
        String subMode = request.getParameter("subMode");
        String name = request.getParameter("name");
        String poolingString = request.getParameter("pooling");

        Boolean pooling = true;
        if (poolingString != null) {
            pooling = !poolingString.equalsIgnoreCase("false");
        }
        String logTime = request.getParameter("logTime");

        List<Track> tracks = null;
        try {
            tracks = fetchTracks(pooling, offset != null && offset != "" ? Integer.parseInt(offset) : 0,
                    limit != null && limit != "" ? Integer.parseInt(limit) : 20,
                    sortBy != null && sortBy != "" ? sortBy : "popularity desc",
                    searchMode != null && searchMode != "" ? searchMode : null,
                    search != null && search != "" ? search : null, 
                    subMode != null && subMode != "" ? subMode : null, 
                    name != null && name != "" ? name : null,
                    logTime != null && logTime != "" ? logTime : null);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }

        PrintWriter out = response.getWriter();

        String tracksResponse = this.gson.toJson(tracks);
        out.print("{ \"songs\": " + tracksResponse + " }");

        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;

        if (logTime != null) {
            MyLogger.log(String.format("[%s]: Ts: %s", logTime, Long.toString(elapsedTime)));
        }
    }
}

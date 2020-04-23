package com.cs122b.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;

@WebFilter(filterName = "LoginFilter", urlPatterns = "/api/*")
public class LoginFilter implements Filter {
    private final ArrayList<String> allowedURIs = new ArrayList<String>();

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (this.isUrlAllowedWithoutLogin(httpRequest.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        // Redirect to login page if the "user" attribute doesn't exist in session
        if (httpRequest.getSession().getAttribute("user_id") == null) {
            httpResponse.setContentType("application/json");
            httpResponse.setCharacterEncoding("UTF-8");
            JsonObject responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("status", "failure");
            responseJsonObject.addProperty("message", "must be logged in");
            httpResponse.setStatus(401);
            httpResponse.getWriter().write(responseJsonObject.toString());
        } else {
            chain.doFilter(request, response);
        }
    }

    private boolean isUrlAllowedWithoutLogin(String requestURI) {
        // DEV
        // return true;
        return allowedURIs.stream().anyMatch(requestURI.toLowerCase()::endsWith);
    }

    public void init(FilterConfig fConfig) {

        // Album
        // allowedURIs.add("api/albums");
        allowedURIs.add("api/album");
        allowedURIs.add("api/albums/tracks");

        // Artist
        allowedURIs.add("api/artists");
        allowedURIs.add("api/artist");

        // Track
        allowedURIs.add("api/tracks");
        allowedURIs.add("api/track");
        allowedURIs.add("api/track/meta");

        // Playlist
        allowedURIs.add("api/playlist/session");
        allowedURIs.add("api/playlist/session/track");

        // Auth
        allowedURIs.add("api/login");
        allowedURIs.add("api/logout");
    }

    public void destroy() {
        // ignored.
    }
}
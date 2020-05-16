package com.cs122b.web;

import com.google.gson.JsonObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LogoutServlet", urlPatterns = "/logout")
public class LogoutServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JsonObject responseJsonObject = new JsonObject();

        Integer user_id = (Integer) request.getSession().getAttribute("user_id");
        request.getSession().setAttribute("user_id", null);
        if (user_id != null) {
            responseJsonObject.addProperty("status", "success");
            responseJsonObject.addProperty("message", "you are logged out");

        } else {
            responseJsonObject.addProperty("status", "fail");
            responseJsonObject.addProperty("message", "you were not logged in");
        }

        response.getWriter().write(responseJsonObject.toString());
    }
}
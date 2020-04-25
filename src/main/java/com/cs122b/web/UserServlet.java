package com.cs122b.web;

import com.cs122b.service.UserService;
import com.cs122b.model.User;
import com.cs122b.utils.JsonParse;

import com.google.gson.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.sql.SQLException;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "UserServlet", urlPatterns = "/user")
public class UserServlet extends HttpServlet {
    private Gson gson = new Gson();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JsonObject jsonRequestBody = JsonParse.toJson(request.getReader());

        PrintWriter out = response.getWriter();

        User user = null;
        try {
            user = UserService.createUser(jsonRequestBody);
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(400);
            out.write("{ \"message\": \"resource not created\"}");
            return;
        }

        if (user == null) {
            response.setStatus(400);
            out.write("{ \"message\": \"resource not created\"}");
            return;
        }

        String userResponse = this.gson.toJson(user);
        out.write(userResponse);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        int user_id = (Integer) request.getSession().getAttribute("user_id");

        User user = null;
        try {
            user = UserService.fetchUser(user_id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        PrintWriter out = response.getWriter();

        if (user == null) {
            response.setStatus(404);
            out.print("{ \"message\": \"resource not found\"}");
        } else {
            String trackResponse = this.gson.toJson(user);
            out.print(trackResponse);
        }
    }
}
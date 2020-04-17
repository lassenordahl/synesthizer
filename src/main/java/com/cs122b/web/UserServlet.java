package com.cs122b.web;

import com.cs122b.service.UserService;
import com.cs122b.model.User;
import com.cs122b.utils.JsonParse;

import com.google.gson.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        }

        String userResponse = this.gson.toJson(user);
        out.write(userResponse);
    }
}
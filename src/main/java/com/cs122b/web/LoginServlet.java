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

@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JsonObject jsonRequestBody = JsonParse.toJson(request.getReader());

        String email = jsonRequestBody.get("email").getAsString();
        String password = jsonRequestBody.get("password").getAsString();

        JsonObject responseJsonObject = new JsonObject();

        User user = null;
        try {
            user = UserService.authenticateUser(email, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (user != null) {
            request.getSession().setAttribute("user_id", user.getId());

            responseJsonObject.addProperty("status", "success");
            responseJsonObject.addProperty("message", "success");

        } else {
            response.setStatus(401);
            responseJsonObject.addProperty("status", "incorrect username or password");
        }

        response.getWriter().write(responseJsonObject.toString());
    }
}
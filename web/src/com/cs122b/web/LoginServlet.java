package com.cs122b.web;

import com.cs122b.model.Employee;
import com.cs122b.service.EmployeeService;
import com.cs122b.service.UserService;
import com.cs122b.model.User;
import com.cs122b.utils.JsonParse;

import com.cs122b.utils.RecaptchaVerifyUtils;
import com.google.gson.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
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
        String appType = jsonRequestBody.get("appType").getAsString();

        JsonObject responseJsonObject = new JsonObject();

        User user = null;

        try {
            if (!appType.equalsIgnoreCase("android")) {
                RecaptchaVerifyUtils.verify(jsonRequestBody.get("captcha").getAsString());
            }

            try {
                user = UserService.authenticateUser(email, password);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            Employee employee = null;
            if (user == null) {
                try {
                    employee = EmployeeService.authenticateEmployee(email, password);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }


            if (user != null) {
                request.getSession().setAttribute("user_id", user.getId());

                responseJsonObject.addProperty("status", "success");
                responseJsonObject.addProperty("message", "success");

            } else if (employee != null) {
                request.getSession().setAttribute("employee_id", employee.getId());

                responseJsonObject.addProperty("isEmployee", "true");
                responseJsonObject.addProperty("status", "success");
                responseJsonObject.addProperty("message", "success");
            } else {
                response.setStatus(401);
                responseJsonObject.addProperty("status", "incorrect username or password");
            }
        } catch (Exception e) {
            response.setStatus(400);
            responseJsonObject.addProperty("message", "No captcha provided or invalid captcha");
        }

        response.getWriter().write(responseJsonObject.toString());
    }
}
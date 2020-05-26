package com.cs122b.web;

import com.cs122b.service.EmployeeService;
import com.cs122b.model.Employee;
import com.cs122b.utils.JsonParse;

import com.google.gson.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.sql.SQLException;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "EmployeeServlet", urlPatterns = "/employee")
public class EmployeeServlet extends HttpServlet {
    private Gson gson = new Gson();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JsonObject jsonRequestBody = JsonParse.toJson(request.getReader());

        PrintWriter out = response.getWriter();

        Employee employee = null;
        try {
            employee = EmployeeService.createEmployee(jsonRequestBody);
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(400);
            out.write("{ \"message\": \"resource not created\"}");
            return;
        }

        if (employee == null) {
            response.setStatus(400);
            out.write("{ \"message\": \"resource not created\"}");
            return;
        }

        String employeeResponse = this.gson.toJson(employee);
        out.write(employeeResponse);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        int employee_id = (Integer) request.getSession().getAttribute("employee_id");

        Employee employee = null;
        try {
            employee = EmployeeService.fetchEmployee(employee_id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        PrintWriter out = response.getWriter();

        if (employee == null) {
            response.setStatus(404);
            out.print("{ \"message\": \"resource not found\"}");
        } else {
            String trackResponse = this.gson.toJson(employee);
            out.print(trackResponse);
        }
    }
}
package com.ibrahim;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class HelloServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("In Service");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<h2><b>Hello World</b></h2>");
    }
}

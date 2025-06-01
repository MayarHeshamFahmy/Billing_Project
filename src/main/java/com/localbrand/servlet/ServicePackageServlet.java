package com.localbrand.servlet;

import com.localbrand.model.ServicePackage;
import com.localbrand.service.ServicePackageService;
import com.localbrand.service.impl.ServicePackageServiceImpl;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/service-packages/*")
public class ServicePackageServlet extends HttpServlet {
    private ServicePackageService servicePackageService;

    @Override
    public void init() throws ServletException {
        servicePackageService = new ServicePackageServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            List<ServicePackage> packages = servicePackageService.getAllServicePackages();
            out.print(new JSONObject().put("packages", packages).toString());
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
} 
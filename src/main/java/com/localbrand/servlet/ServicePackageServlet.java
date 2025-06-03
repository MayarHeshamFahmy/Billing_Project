package com.localbrand.servlet;

import com.localbrand.model.ServicePackage;
import com.localbrand.service.ServicePackageService;
import com.localbrand.service.impl.ServicePackageServiceImpl;
import org.json.JSONArray;
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
            JSONArray packagesArray = new JSONArray();
            
            for (ServicePackage pkg : packages) {
                JSONObject packageJson = new JSONObject();
                packageJson.put("id", pkg.getId());
                packageJson.put("name", pkg.getName());
                packageJson.put("description", pkg.getDescription());
                packageJson.put("price", pkg.getPrice());
                packagesArray.put(packageJson);
            }
            
            JSONObject responseJson = new JSONObject();
            responseJson.put("packages", packagesArray);
            out.print(responseJson.toString());
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JSONObject errorJson = new JSONObject();
            errorJson.put("error", e.getMessage());
            out.print(errorJson.toString());
        }
    }
} 
package com.localbrand.servlet;

import com.localbrand.model.RatePlan;
import com.localbrand.service.RatePlanService;
import com.localbrand.service.impl.RatePlanServiceImpl;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/api/rate-plans/*")
public class RatePlanServlet extends HttpServlet {
    private RatePlanService ratePlanService;

    @Override
    public void init() throws ServletException {
        ratePlanService = new RatePlanServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            List<RatePlan> ratePlans = ratePlanService.getAllRatePlans();
            JSONArray ratePlansArray = new JSONArray();
            
            for (RatePlan ratePlan : ratePlans) {
                JSONObject ratePlanJson = new JSONObject();
                ratePlanJson.put("id", ratePlan.getId());
                ratePlanJson.put("name", ratePlan.getName());
                ratePlanJson.put("description", ratePlan.getDescription());
                ratePlanJson.put("basePrice", ratePlan.getBasePrice());
                ratePlansArray.put(ratePlanJson);
            }
            
            JSONObject responseJson = new JSONObject();
            responseJson.put("ratePlans", ratePlansArray);
            out.print(responseJson.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            StringBuilder jsonBody = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBody.append(line);
            }
            JSONObject json = new JSONObject(jsonBody.toString());
            
            RatePlan ratePlan = new RatePlan();
            ratePlan.setName(json.getString("name"));
            ratePlan.setDescription(json.getString("description"));
            ratePlan.setBasePrice(BigDecimal.valueOf(json.getDouble("basePrice")));

            RatePlan savedRatePlan = ratePlanService.createRatePlan(ratePlan);
            out.print(new JSONObject(savedRatePlan).toString());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
} 
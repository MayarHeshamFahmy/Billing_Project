package com.localbrand.servlet;

import com.localbrand.model.ServiceSubscription;
import com.localbrand.service.ServiceSubscriptionService;
import com.localbrand.service.impl.ServiceSubscriptionServiceImpl;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;

@WebServlet("/api/service-subscriptions/*")
public class ServiceSubscriptionServlet extends HttpServlet {
    private ServiceSubscriptionService subscriptionService;

    @Override
    public void init() throws ServletException {
        subscriptionService = new ServiceSubscriptionServiceImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            BufferedReader reader = request.getReader();
            JSONObject json = new JSONObject(reader.readLine());
            
            ServiceSubscription subscription = new ServiceSubscription();
            subscription.setCustomerPhone(json.getString("customerPhone"));
            subscription.setServicePackageId(json.getLong("servicePackageId"));
            subscription.setStartDate(LocalDate.parse(json.getString("startDate")).atStartOfDay());
            
            if (json.has("endDate") && !json.isNull("endDate")) {
                subscription.setEndDate(LocalDate.parse(json.getString("endDate")).atTime(LocalTime.MAX));
            }

            ServiceSubscription savedSubscription = subscriptionService.createSubscription(subscription);
            out.print(new JSONObject(savedSubscription).toString());
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
} 
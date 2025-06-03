package com.localbrand.servlet;

import com.localbrand.model.ServiceSubscription;
import com.localbrand.model.Customer;
import com.localbrand.model.ServicePackage;
import com.localbrand.service.ServiceSubscriptionService;
import com.localbrand.service.CustomerService;
import com.localbrand.service.ServicePackageService;
import com.localbrand.service.impl.ServiceSubscriptionServiceImpl;
import com.localbrand.service.impl.CustomerServiceImpl;
import com.localbrand.service.impl.ServicePackageServiceImpl;
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
    private CustomerService customerService;
    private ServicePackageService servicePackageService;

    @Override
    public void init() throws ServletException {
        subscriptionService = new ServiceSubscriptionServiceImpl();
        customerService = new CustomerServiceImpl();
        servicePackageService = new ServicePackageServiceImpl();
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
            
            String customerPhone = json.getString("customerPhone");
            Long servicePackageId = json.getLong("servicePackageId");
            
            // Look up customer by phone number
            Customer customer = customerService.getCustomerByPhoneNumber(customerPhone);
            if (customer == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                JSONObject errorJson = new JSONObject();
                errorJson.put("error", "Customer not found with phone number: " + customerPhone);
                out.print(errorJson.toString());
                return;
            }
            
            // Get service package to get free units
            ServicePackage servicePackage = servicePackageService.getServicePackageById(servicePackageId);
            if (servicePackage == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                JSONObject errorJson = new JSONObject();
                errorJson.put("error", "Service package not found with ID: " + servicePackageId);
                out.print(errorJson.toString());
                return;
            }
            
            ServiceSubscription subscription = new ServiceSubscription();
            subscription.setCustomerId(customer.getId());
            subscription.setServicePackageId(servicePackageId);
            subscription.setStartDate(LocalDate.parse(json.getString("startDate")).atStartOfDay());
            subscription.setActive(true);
            subscription.setRemainingFreeUnits(servicePackage.getFreeUnits());
            
            if (json.has("endDate") && !json.isNull("endDate")) {
                subscription.setEndDate(LocalDate.parse(json.getString("endDate")).atTime(LocalTime.MAX));
            }

            ServiceSubscription savedSubscription = subscriptionService.createSubscription(subscription);
            out.print(new JSONObject(savedSubscription).toString());
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JSONObject errorJson = new JSONObject();
            errorJson.put("error", e.getMessage());
            out.print(errorJson.toString());
        }
    }
} 
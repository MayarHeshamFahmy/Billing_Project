package com.localbrand.servlet;

import com.localbrand.model.Customer;
import com.localbrand.model.ServiceSubscription;
import com.localbrand.model.Invoice;
import com.localbrand.service.CustomerService;
import com.localbrand.service.InvoiceService;
import com.localbrand.service.impl.CustomerServiceImpl;
import com.localbrand.service.impl.InvoiceServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;
import com.google.gson.Gson;

@WebServlet("/api/customers/*/dashboard")
public class CustomerDashboardServlet extends HttpServlet {
    private CustomerService customerService;
    private InvoiceService invoiceService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        customerService = new CustomerServiceImpl();
        invoiceService = new InvoiceServiceImpl();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        String phoneNumber = pathInfo.substring(1, pathInfo.indexOf("/dashboard"));

        try {
            Customer customer = customerService.findByPhoneNumber(phoneNumber);
            if (customer == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Customer not found");
                return;
            }

            Map<String, Object> dashboardData = new HashMap<>();
            
            // Get customer's active subscriptions
            List<ServiceSubscription> subscriptions = customer.getSubscriptions().stream()
                .filter(ServiceSubscription::getActive)
                .collect(Collectors.toList());

            // Calculate total free units and remaining free units
            int totalFreeUnits = subscriptions.stream()
                .mapToInt(sub -> sub.getServicePackage().getFreeUnits())
                .sum();
            int remainingFreeUnits = subscriptions.stream()
                .mapToInt(ServiceSubscription::getRemainingFreeUnits)
                .sum();

            // Get recent invoices
            List<Invoice> invoices = invoiceService.getInvoicesByCustomerPhone(phoneNumber);

            // Prepare subscription data
            List<Map<String, Object>> subscriptionData = subscriptions.stream()
                .map(sub -> {
                    Map<String, Object> subData = new HashMap<>();
                    subData.put("packageName", sub.getServicePackage().getName());
                    subData.put("remainingFreeUnits", sub.getRemainingFreeUnits());
                    subData.put("totalFreeUnits", sub.getServicePackage().getFreeUnits());
                    subData.put("active", sub.getActive());
                    return subData;
                })
                .collect(Collectors.toList());

            // Prepare invoice data
            List<Map<String, Object>> invoiceData = invoices.stream()
                .map(inv -> {
                    Map<String, Object> invData = new HashMap<>();
                    invData.put("id", inv.getId());
                    invData.put("invoiceNumber", inv.getInvoiceNumber());
                    invData.put("issueDate", inv.getIssueDate());
                    invData.put("total", inv.getTotal());
                    invData.put("status", inv.getStatus());
                    return invData;
                })
                .collect(Collectors.toList());

            // Add all data to dashboard
            dashboardData.put("balance", calculateBalance(invoices));
            dashboardData.put("balanceLimit", 1000.0); // Example limit
            dashboardData.put("remainingFreeUnits", remainingFreeUnits);
            dashboardData.put("totalFreeUnits", totalFreeUnits);
            dashboardData.put("subscriptions", subscriptionData);
            dashboardData.put("invoices", invoiceData);
            dashboardData.put("recentActivity", getRecentActivity(customer));

            // Send response
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(gson.toJson(dashboardData));

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                "Error loading dashboard data: " + e.getMessage());
        }
    }

    private double calculateBalance(List<Invoice> invoices) {
        return invoices.stream()
            .filter(inv -> "UNPAID".equals(inv.getStatus()))
            .mapToDouble(inv -> inv.getTotal().doubleValue())
            .sum();
    }

    private List<Map<String, Object>> getRecentActivity(Customer customer) {
        // This would typically come from a separate activity log
        // For now, we'll create some example activity
        List<Map<String, Object>> activity = new ArrayList<>();
        
        // Add subscription activities
        customer.getSubscriptions().forEach(sub -> {
            Map<String, Object> subActivity = new HashMap<>();
            subActivity.put("type", "SUBSCRIPTION");
            subActivity.put("description", 
                String.format("Subscribed to %s package", 
                    sub.getServicePackage().getName()));
            subActivity.put("timestamp", sub.getStartDate());
            activity.add(subActivity);
        });

        // Add invoice activities
        customer.getInvoices().forEach(inv -> {
            Map<String, Object> invActivity = new HashMap<>();
            invActivity.put("type", "INVOICE");
            invActivity.put("description", 
                String.format("Invoice %s %s", 
                    inv.getInvoiceNumber(),
                    inv.getStatus().toLowerCase()));
            invActivity.put("timestamp", inv.getIssueDate());
            activity.add(invActivity);
        });

        // Sort by timestamp descending and limit to 10 most recent
        return activity.stream()
            .sorted((a, b) -> ((LocalDateTime)b.get("timestamp"))
                .compareTo((LocalDateTime)a.get("timestamp")))
            .limit(10)
            .collect(Collectors.toList());
    }
} 
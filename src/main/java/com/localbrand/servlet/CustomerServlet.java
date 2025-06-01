package com.localbrand.servlet;

import com.localbrand.model.Customer;
import com.localbrand.model.RatePlan;
import com.localbrand.service.CustomerService;
import com.localbrand.service.RatePlanService;
import com.localbrand.service.impl.CustomerServiceImpl;
import com.localbrand.service.impl.RatePlanServiceImpl;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/customers/*")
public class CustomerServlet extends HttpServlet {
    private CustomerService customerService;
    private RatePlanService ratePlanService;

    @Override
    public void init() throws ServletException {
        customerService = new CustomerServiceImpl();
        ratePlanService = new RatePlanServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            // Get all customers
            List<Customer> customers = customerService.getAllCustomers();
            out.print(new JSONObject().put("customers", customers).toString());
        } else {
            // Get customer by phone number
            String phoneNumber = pathInfo.substring(1);
            try {
                Customer customer = customerService.getCustomerByPhoneNumber(phoneNumber);
                if (customer != null) {
                    out.print(new JSONObject(customer).toString());
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\": \"Customer not found\"}");
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("{\"error\": \"" + e.getMessage() + "\"}");
            }
        }
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
            
            Customer customer = new Customer();
            customer.setName(json.getString("name"));
            customer.setPhoneNumber(json.getString("phoneNumber"));
            customer.setEmail(json.getString("email"));
            customer.setAddress(json.getString("address"));
            
            Long ratePlanId = json.getLong("ratePlanId");
            RatePlan ratePlan = ratePlanService.getRatePlanById(ratePlanId);
            customer.setRatePlan(ratePlan);

            Customer savedCustomer = customerService.createCustomer(customer);
            out.print(new JSONObject(savedCustomer).toString());
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
} 
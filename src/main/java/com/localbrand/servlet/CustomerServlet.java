package com.localbrand.servlet;

import com.localbrand.model.Customer;
import com.localbrand.repository.CustomerRepository;
import com.localbrand.repository.impl.CustomerRepositoryImpl;
import org.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/customers/*")
public class CustomerServlet extends HttpServlet {
    private CustomerRepository customerRepository;

    @Override
    public void init() throws ServletException {
        customerRepository = new CustomerRepositoryImpl();
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
            List<Customer> customers = customerRepository.findAll();
            JSONArray jsonArray = new JSONArray(customers);
            out.print(jsonArray.toString());
        } else {
            // Get customer by phone number
            String phoneNumber = pathInfo.substring(1);
            Customer customer = customerRepository.findByPhoneNumber(phoneNumber);
            if (customer != null) {
                out.print(new org.json.JSONObject(customer).toString());
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\": \"Customer not found\"}");
            }
        }
    }
} 
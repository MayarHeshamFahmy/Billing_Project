package com.localbrand.servlet;

import com.localbrand.model.CustomerInvoice;
import com.localbrand.service.DataTransferService;
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

@WebServlet("/api/transfer/*")
public class DataTransferServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"Invalid endpoint\"}");
            return;
        }

        try {
            switch (pathInfo) {
                case "/customer-invoice":
                    DataTransferService.transferDataToCustomerInvoice();
                    out.print("{\"message\": \"Customer invoice data transfer completed\"}");
                    break;
                case "/invoice":
                    DataTransferService.transferDataToInvoice();
                    out.print("{\"message\": \"Invoice data transfer completed\"}");
                    break;
                default:
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\": \"Invalid endpoint\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || !pathInfo.startsWith("/customer/")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"Invalid endpoint\"}");
            return;
        }

        try {
            String customerMsisdn = pathInfo.substring("/customer/".length());
            List<CustomerInvoice> invoices = DataTransferService.getCustomerInvoices(customerMsisdn);
            
            JSONArray jsonArray = new JSONArray();
            for (CustomerInvoice invoice : invoices) {
                JSONObject json = new JSONObject();
                json.put("customerMsisdn", invoice.getCustomerMsisdn());
                json.put("serviceType", invoice.getServiceType());
                json.put("totalVolume", invoice.getTotalVolume());
                json.put("totalCharges", invoice.getTotalCharges());
                json.put("invoiceDate", invoice.getInvoiceDate());
                jsonArray.put(json);
            }
            
            out.print(jsonArray.toString());
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
} 
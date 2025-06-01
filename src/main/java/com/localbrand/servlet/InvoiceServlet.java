package com.localbrand.servlet;

import com.localbrand.model.Invoice;
import com.localbrand.service.InvoiceService;
import com.localbrand.service.impl.InvoiceServiceImpl;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/invoices/*")
public class InvoiceServlet extends HttpServlet {
    private InvoiceService invoiceService;

    @Override
    public void init() throws ServletException {
        invoiceService = new InvoiceServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"Invalid endpoint\"}");
            return;
        }

        try {
            if (pathInfo.startsWith("/customer/")) {
                // Get invoices by customer phone number
                String phoneNumber = pathInfo.substring("/customer/".length());
                List<Invoice> invoices = invoiceService.getInvoicesByCustomerPhone(phoneNumber);
                out.print(new JSONObject().put("invoices", invoices).toString());
            } else {
                // Get single invoice by ID
                String invoiceId = pathInfo.substring(1);
                Invoice invoice = invoiceService.getInvoiceById(Long.parseLong(invoiceId));
                if (invoice != null) {
                    out.print(new JSONObject(invoice).toString());
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\": \"Invoice not found\"}");
                }
            }
        } catch (Exception e) {
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

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || !pathInfo.matches("/\\d+/mark-paid")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"Invalid endpoint\"}");
            return;
        }

        try {
            String invoiceId = pathInfo.split("/")[1];
            Invoice invoice = invoiceService.markInvoiceAsPaid(Long.parseLong(invoiceId));
            out.print(new JSONObject(invoice).toString());
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
} 
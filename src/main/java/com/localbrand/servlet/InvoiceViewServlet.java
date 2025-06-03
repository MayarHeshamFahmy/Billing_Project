package com.localbrand.servlet;

import com.localbrand.dto.InvoiceViewDTO;
import com.localbrand.service.InvoiceViewService;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;

@WebServlet("/api/invoice/customer")
public class InvoiceViewServlet extends HttpServlet {
    private final InvoiceViewService invoiceViewService = new InvoiceViewService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String msisdn = req.getParameter("msisdn");
        String dateStr = req.getParameter("date");
        if (msisdn == null || dateStr == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Missing msisdn or date parameter\"}");
            return;
        }

        Date invoiceDate = Date.valueOf(dateStr);
        InvoiceViewDTO invoice = invoiceViewService.getInvoiceForCustomer(msisdn, invoiceDate);

        resp.setContentType("application/json");
        resp.getWriter().write(new Gson().toJson(invoice));
    }
} 
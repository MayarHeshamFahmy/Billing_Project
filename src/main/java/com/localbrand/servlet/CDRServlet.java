package com.localbrand.servlet;

import com.localbrand.dto.RatedCDR;
import com.localbrand.model.CDR;
import com.localbrand.model.Customer;
import com.localbrand.model.ServiceType;
import com.localbrand.service.RatingEngine;
import com.localbrand.service.impl.RatingEngineImpl;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

@WebServlet("/api/cdr/*")
public class CDRServlet extends HttpServlet {
    private RatingEngine ratingEngine;

    @Override
    public void init() throws ServletException {
        ratingEngine = new RatingEngineImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/rate")) {
            handleRateCDR(request, response);
        } else if (pathInfo.equals("/batch")) {
            handleBatchCDRs(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.print("{\"error\": \"Invalid endpoint\"}");
        }
    }

    private void handleRateCDR(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        PrintWriter out = response.getWriter();
        try {
            BufferedReader reader = request.getReader();
            JSONObject json = new JSONObject(reader.readLine());
            
            CDR cdr = new CDR();
            cdr.setDialA(json.getString("dialA"));
            cdr.setDialB(json.getString("dialB"));
            cdr.setServiceType(ServiceType.valueOf(json.getString("serviceType")));
            cdr.setUsage(json.getLong("usage"));
            cdr.setStartTime(LocalDateTime.parse(json.getString("startTime")));
            cdr.setExternalCharges(json.getDouble("externalCharges"));

            Long customerId = Long.parseLong(request.getParameter("customerId"));
            Customer customer = new Customer();
            customer.setId(customerId);

            RatedCDR ratedCDR = ratingEngine.rateCDR(cdr, customer);
            ratingEngine.updateFreeUnits(customer, ratedCDR);

            out.print(new JSONObject(ratedCDR).toString());
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private void handleBatchCDRs(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        PrintWriter out = response.getWriter();
        try {
            BufferedReader reader = request.getReader();
            String cdrBatch = reader.readLine();
            String[] cdrs = cdrBatch.split("\n");
            int processedCount = 0;

            for (String cdrLine : cdrs) {
                String[] parts = cdrLine.split(",");
                if (parts.length >= 6) {
                    CDR cdr = new CDR();
                    cdr.setDialA(parts[0]);
                    cdr.setDialB(parts[1]);
                    cdr.setServiceType(ServiceType.values()[Integer.parseInt(parts[2]) - 1]);
                    cdr.setUsage(Long.parseLong(parts[3]));
                    cdr.setStartTime(parseTime(parts[4]));
                    cdr.setExternalCharges(Double.parseDouble(parts[5]));

                    Customer customer = new Customer();
                    RatedCDR ratedCDR = ratingEngine.rateCDR(cdr, customer);
                    ratingEngine.updateFreeUnits(customer, ratedCDR);
                    processedCount++;
                }
            }

            out.print("{\"message\": \"Processed " + processedCount + " CDRs successfully\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private LocalDateTime parseTime(String timeStr) {
        String[] parts = timeStr.split(":");
        LocalDateTime now = LocalDateTime.now();
        return now.withHour(Integer.parseInt(parts[0]))
                 .withMinute(Integer.parseInt(parts[1]))
                 .withSecond(Integer.parseInt(parts[2]));
    }
} 
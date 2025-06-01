package com.localbrand.service;

import com.localbrand.model.Invoice;
import java.util.List;

public interface InvoiceService {
    List<Invoice> getInvoicesByCustomerPhone(String phoneNumber);
    Invoice getInvoiceById(Long id);
    Invoice markInvoiceAsPaid(Long id);
} 
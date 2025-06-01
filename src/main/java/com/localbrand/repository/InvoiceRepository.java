package com.localbrand.repository;

import com.localbrand.model.Invoice;
import java.util.List;

public interface InvoiceRepository {
    List<Invoice> findByCustomerPhone(String phoneNumber);
    Invoice findById(Long id);
    Invoice save(Invoice invoice);
} 
package com.localbrand.service.impl;

import com.localbrand.model.Invoice;
import com.localbrand.repository.InvoiceRepository;
import com.localbrand.repository.impl.InvoiceRepositoryImpl;
import com.localbrand.service.InvoiceService;
import java.util.List;

public class InvoiceServiceImpl implements InvoiceService {
    private InvoiceRepository invoiceRepository;

    public InvoiceServiceImpl() {
        this.invoiceRepository = new InvoiceRepositoryImpl();
    }

    @Override
    public List<Invoice> getInvoicesByCustomerPhone(String phoneNumber) {
        return invoiceRepository.findByCustomerPhone(phoneNumber);
    }

    @Override
    public Invoice getInvoiceById(Long id) {
        return invoiceRepository.findById(id);
    }

    @Override
    public Invoice markInvoiceAsPaid(Long id) {
        Invoice invoice = invoiceRepository.findById(id);
        if (invoice != null) {
            invoice.setStatus("PAID");
            return invoiceRepository.save(invoice);
        }
        return null;
    }
} 
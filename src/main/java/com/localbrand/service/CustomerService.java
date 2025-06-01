package com.localbrand.service;

import com.localbrand.model.Customer;
import java.util.List;

public interface CustomerService {
    List<Customer> getAllCustomers();
    Customer getCustomerByPhoneNumber(String phoneNumber);
    Customer createCustomer(Customer customer);

    public Customer findByPhoneNumber(String phoneNumber);
} 
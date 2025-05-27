package com.localbrand.repository;

import com.localbrand.model.Customer;
import java.util.List;

public interface CustomerRepository {
    List<Customer> findAll();
    Customer findByPhoneNumber(String phoneNumber);
} 
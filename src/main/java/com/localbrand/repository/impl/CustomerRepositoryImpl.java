package com.localbrand.repository.impl;

import com.localbrand.model.Customer;
import com.localbrand.repository.CustomerRepository;
import com.localbrand.util.DatabaseUtil;

import javax.persistence.EntityManager;
import java.util.List;

public class CustomerRepositoryImpl implements CustomerRepository {
    private EntityManager em;

    public CustomerRepositoryImpl() {
        this.em = DatabaseUtil.getEntityManager();
    }

    @Override
    public List<Customer> findAll() {
        return em.createQuery("SELECT c FROM Customer c", Customer.class)
                .getResultList();
    }

    @Override
    public Customer findByPhoneNumber(String phoneNumber) {
        try {
            return em.createQuery("SELECT c FROM Customer c WHERE c.phoneNumber = :phone", Customer.class)
                    .setParameter("phone", phoneNumber)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
} 
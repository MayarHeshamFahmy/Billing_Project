package com.localbrand.service.impl;

import com.localbrand.model.ServiceSubscription;
import com.localbrand.repository.ServiceSubscriptionRepository;
import com.localbrand.repository.impl.ServiceSubscriptionRepositoryImpl;
import com.localbrand.service.ServiceSubscriptionService;

public class ServiceSubscriptionServiceImpl implements ServiceSubscriptionService {
    private ServiceSubscriptionRepository subscriptionRepository;

    public ServiceSubscriptionServiceImpl() {
        this.subscriptionRepository = new ServiceSubscriptionRepositoryImpl();
    }

    @Override
    public ServiceSubscription createSubscription(ServiceSubscription subscription) {
        return subscriptionRepository.save(subscription);
    }
} 
package com.pactual.btg.pactual.application.usecase;

import com.pactual.btg.pactual.domain.Subscription;

import java.util.List;

public interface GetSubscriptionQuery {
    Subscription getById(String customerId, String fundId, String subscriptionId);
    List<Subscription> listByCustomer(String customerId, String status /* nullable */);
}

package com.pactual.btg.pactual.application.usecase;

import com.pactual.btg.pactual.domain.Customer;

public interface GetCustomerQuery {
    Customer getById(String customerId);
}

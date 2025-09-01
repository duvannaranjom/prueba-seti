package com.pactual.btg.pactual.web.controller;

import com.pactual.btg.pactual.application.usecase.GetCustomerQuery;
import com.pactual.btg.pactual.domain.Customer;
import com.pactual.btg.pactual.web.dto.response.CustomerResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@Validated
@RestController
@RequestMapping("/V1/customers")
class CustomersController {
    private final GetCustomerQuery customers;
    CustomersController(GetCustomerQuery customers){ this.customers = customers; }


    @GetMapping("/{customerId}")
    CustomerResponse get(@PathVariable String customerId){
        Customer c = customers.getById(customerId);
        return new CustomerResponse(c.getCustomerId(), c.getBalance(), c.getNotificationPreference());
    }


    @GetMapping("/{customerId}/balance")
    ResponseEntity<BigDecimal> balance(@PathVariable String customerId){
        return ResponseEntity.ok(customers.getById(customerId).getBalance());
    }
}

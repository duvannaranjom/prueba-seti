package com.pactual.btg.pactual.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Customer {
    private String customerId;
    private BigDecimal balance; // saldo actual
    private String notificationPreference;
}

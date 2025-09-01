package com.pactual.btg.pactual.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Data
public class Transaction {

    private String transactionId; // UUID
    private String customerId;
    private String fundId;
    private String type; // OPEN | CANCEL
    private BigDecimal amount;
    private Instant createdAt;

    public static Transaction open(String customerId, String fundId, BigDecimal amount) {
        Transaction t = new Transaction();
        t.transactionId = UUID.randomUUID().toString();
        t.customerId = Objects.requireNonNull(customerId);
        t.fundId = Objects.requireNonNull(fundId);
        t.type = "OPEN";
        t.amount = Objects.requireNonNull(amount);
        t.createdAt = Instant.now();
        return t;
    }


    public static Transaction cancel(String customerId, String fundId, BigDecimal amount) {
        Transaction t = new Transaction();
        t.transactionId = UUID.randomUUID().toString();
        t.customerId = Objects.requireNonNull(customerId);
        t.fundId = Objects.requireNonNull(fundId);
        t.type = "CANCEL";
        t.amount = Objects.requireNonNull(amount);
        t.createdAt = Instant.now();
        return t;
    }
}

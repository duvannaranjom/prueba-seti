package com.pactual.btg.pactual.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Data
public class Subscription {
    private String subscriptionId; // UUID
    private String customerId;
    private String fundId;
    private BigDecimal amount;
    private String status; // ACTIVE | CANCELLED
    private Instant createdAt;
    private Instant cancelledAt; // nullable

    public Subscription() {}

    public static Subscription newActive(String customerId, String fundId, BigDecimal amount) {
        Subscription s = new Subscription();
        s.subscriptionId = UUID.randomUUID().toString();
        s.customerId = Objects.requireNonNull(customerId);
        s.fundId = Objects.requireNonNull(fundId);
        s.amount = Objects.requireNonNull(amount);
        s.status = "ACTIVE";
        s.createdAt = Instant.now();
        return s;
    }


    public void cancelNow() {
        if (!"ACTIVE".equals(this.status)) {
            throw new IllegalStateException("Subscription not ACTIVE");
        }
        this.status = "CANCELLED";
        this.cancelledAt = Instant.now();
    }
}

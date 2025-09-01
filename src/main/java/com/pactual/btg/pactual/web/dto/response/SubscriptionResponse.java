package com.pactual.btg.pactual.web.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

public record SubscriptionResponse(
        String subscriptionId,
        String customerId,
        String fundId,
        BigDecimal amount,
        String status,
        Instant createdAt,
        Instant cancelledAt
) {}

package com.pactual.btg.pactual.web.dto.response;

import java.time.Instant;

public record CancelSubscriptionResponse(
        String subscriptionId,
        String status,
        Instant cancelledAt
) {}

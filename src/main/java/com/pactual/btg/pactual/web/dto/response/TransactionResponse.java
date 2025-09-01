package com.pactual.btg.pactual.web.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionResponse(
        String transactionId,
        String customerId,
        String fundId,
        String type, //OPEN | cancel
        BigDecimal amount,
        Instant createdAt
) {}

package com.pactual.btg.pactual.web.dto.response;

import java.math.BigDecimal;

public record CustomerResponse(
        String customerId,
        BigDecimal balance,
        String notificationPreference
) {}

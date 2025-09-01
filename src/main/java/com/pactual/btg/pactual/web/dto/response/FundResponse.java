package com.pactual.btg.pactual.web.dto.response;

import java.math.BigDecimal;

public record FundResponse(
        String fundId,
        String name,
        BigDecimal minAmount,
        String category
) {}

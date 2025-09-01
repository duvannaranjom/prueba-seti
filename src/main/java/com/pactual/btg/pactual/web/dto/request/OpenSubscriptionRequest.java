package com.pactual.btg.pactual.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record OpenSubscriptionRequest(
        @NotBlank String fundId,
        @NotNull @Positive BigDecimal amount
) {}

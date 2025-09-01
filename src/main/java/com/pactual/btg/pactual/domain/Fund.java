package com.pactual.btg.pactual.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Fund {
    private String fundId;
    private String name;
    private BigDecimal minAmount; // monto mínimo de vinculación
    private String category;
}

package com.pactual.btg.pactual.infraestructure.persistence.dynamodb.model;
import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;


import java.math.BigDecimal;
import java.time.Instant;


@Data
@DynamoDbBean
public class TransactionItem {
    private String pk; // CUST#<customerId>
    private String sk; // TX#<yyyyMMddHHmmss>#<transactionId>


    // GSI2 (por tipo)
    private String gsi2pk; // CUST#<customerId>
    private String gsi2sk; // TYPE#<OPEN|CANCEL>#<yyyyMMddHHmmss>#<transactionId>


    // Datos
    private String transactionId; // UUID
    private String customerId;
    private String fundId;
    private String type; // OPEN | CANCEL
    private BigDecimal amount;
    private Instant createdAt; // instante de la transacción


    // ======= Getters anotados para claves =======
    @DynamoDbPartitionKey
    @DynamoDbAttribute("PK")
    public String getPk() { return pk; }


    @DynamoDbSortKey
    @DynamoDbAttribute("SK")
    public String getSk() { return sk; }


    @DynamoDbSecondaryPartitionKey(indexNames = "GSI2")
    @DynamoDbAttribute("GSI2PK")
    public String getGsi2pk() { return gsi2pk; }


    @DynamoDbSecondarySortKey(indexNames = "GSI2")
    @DynamoDbAttribute("GSI2SK")
    public String getGsi2sk() { return gsi2sk; }
}

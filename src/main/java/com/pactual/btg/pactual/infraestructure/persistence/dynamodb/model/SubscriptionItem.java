package com.pactual.btg.pactual.infraestructure.persistence.dynamodb.model;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@DynamoDbBean
public class SubscriptionItem {

    private String pk;          // CUST#<customerId>
    private String sk;          // SUB#<fundId>#<subscriptionId>

    private String gsi1pk;      // FUND#<fundId>
    private String gsi1sk;      // SUB#<subscriptionId>

    private String gsi2pk;      // CUST#<customerId>
    private String gsi2sk;      // TYPE#<OPEN|CANCEL>#<fecha>

    private String customerId;
    private String fundId;
    private String subscriptionId;
    private String status;      // ACTIVE|CANCELLED
    private BigDecimal amount;      // o BigDecimal; String si prefieres control fino
    private Instant createdAt;   // ISO-8601; también puedes usar epoch-Long
    private Instant cancelledAt;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("PK")
    public String getPk() { return pk; }

    @DynamoDbSortKey
    @DynamoDbAttribute("SK")
    public String getSk() { return sk; }

    @DynamoDbSecondaryPartitionKey(indexNames = "GSI1")
    @DynamoDbAttribute("GSI1PK")
    public String getGsi1pk() { return gsi1pk; }

    @DynamoDbSecondarySortKey(indexNames = "GSI1")
    @DynamoDbAttribute("GSI1SK")
    public String getGsi1sk() { return gsi1sk; }

    @DynamoDbSecondaryPartitionKey(indexNames = "GSI2")
    @DynamoDbAttribute("GSI2PK")
    public String getGsi2pk() { return gsi2pk; }

    @DynamoDbSecondarySortKey(indexNames = "GSI2")
    @DynamoDbAttribute("GSI2SK")
    public String getGsi2sk() { return gsi2sk; }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(Instant cancelledAt) {
        this.cancelledAt = cancelledAt;
    }
}


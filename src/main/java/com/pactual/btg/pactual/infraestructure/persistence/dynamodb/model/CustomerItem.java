package com.pactual.btg.pactual.infraestructure.persistence.dynamodb.model;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;


import java.math.BigDecimal;
import java.util.*;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;
import java.math.BigDecimal;

@Data
@DynamoDbBean
public class CustomerItem {
    private String pk;
    private String sk;

    private String customerId;
    private BigDecimal balance;
    private String notificationPreference; // EMAIL|SMS

    @DynamoDbPartitionKey
    @DynamoDbAttribute("PK")
    public String getPk() { return pk; }


    @DynamoDbSortKey
    @DynamoDbAttribute("SK")
    public String getSk() { return sk; }
}

package com.pactual.btg.pactual.infraestructure.persistence.dynamodb;

import com.pactual.btg.pactual.infraestructure.config.DynamoProps;
import com.pactual.btg.pactual.infraestructure.persistence.dynamodb.model.FundItem;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class FundRepository {

    private final DynamoDbEnhancedClient enhancedClient;
    private final DynamoProps dynamoProps;

    public FundRepository(DynamoDbEnhancedClient enhancedClient, DynamoProps dynamoProps) {
        this.enhancedClient = enhancedClient;
        this.dynamoProps = dynamoProps;
    }
    private DynamoDbTable<FundItem> table() {
        return enhancedClient.table(dynamoProps.getTable(), TableSchema.fromBean(FundItem.class));
    }


    public Optional<FundItem> findById(String fundId) {
        Key key = Key.builder()
                .partitionValue("FUND#" + fundId)
                .sortValue("META")
                .build();
        return Optional.ofNullable(table().getItem(r -> r.key(key)));
    }


    /** Lista todo el catálogo de fondos (scan con filtro por PK y SK meta). */
    public List<FundItem> listAll() {
// Filtramos: begins_with(PK, 'FUND#') AND SK = 'META'
        Expression filter = Expression.builder()
                .expression("begins_with(PK, :fund) AND SK = :meta")
                .putExpressionValue(":fund", AttributeValue.builder().s("FUND#").build())
                .putExpressionValue(":meta", AttributeValue.builder().s("META").build())
                .build();


        ScanEnhancedRequest req = ScanEnhancedRequest.builder()
                .filterExpression(filter)
                .build();


        List<FundItem> out = new ArrayList<>();
        table().scan(req).stream().forEach(p -> out.addAll(p.items()));
        return out;
    }

}
package com.pactual.btg.pactual.infraestructure.persistence.dynamodb;

import com.pactual.btg.pactual.infraestructure.config.DynamoProps;
import com.pactual.btg.pactual.infraestructure.persistence.dynamodb.model.TransactionItem;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;


import java.time.format.DateTimeFormatter;
import java.time.ZoneOffset;
import java.util.*;

@Repository
public class TransactionRepository {

    private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").withZone(ZoneOffset.UTC);

    private final DynamoDbEnhancedClient enhancedClient;
    private final DynamoProps dynamoProps;

    public TransactionRepository(DynamoDbEnhancedClient enhancedClient, DynamoProps dynamoProps) {
        this.enhancedClient = enhancedClient;
        this.dynamoProps = dynamoProps;
    }

    private DynamoDbTable<TransactionItem> table() {
        return enhancedClient.table(dynamoProps.getTable(), TableSchema.fromBean(TransactionItem.class));
    }


    /**
     * Persiste una transacción. Antes de llamar, asegúrate de setear PK/SK y GSI2PK/GSI2SK
     * (o usa los helpers buildKeys* de este repo).
     */
    public void save(TransactionItem item) { table().putItem(item); }

    /** Lista el historial completo de un cliente (orden natural por SK ascendente) */
    public List<TransactionItem> listByCustomer(String customerId) {
        QueryConditional cond = QueryConditional.keyEqualTo(Key.builder().partitionValue("CUST#" + customerId).build());
// Filtramos que SK empiece con "TX#" para no traer PROFILE/SUB
        Expression filter = Expression.builder()
                .expression("begins_with(SK, :tx)")
                .putExpressionValue(":tx", AttributeValue.builder().s("TX#").build())
                .build();
        QueryEnhancedRequest req = QueryEnhancedRequest.builder().queryConditional(cond).filterExpression(filter).build();
        List<TransactionItem> out = new ArrayList<>();
        table().query(req).stream().forEach(p -> out.addAll(p.items()));
        return out;
    }


    /** Lista por tipo usando GSI2: TYPE#OPEN|CANCEL */
    public List<TransactionItem> listByCustomerAndType(String customerId, String type /* OPEN|CANCEL */) {
        DynamoDbIndex<TransactionItem> idx = table().index("GSI2");
        QueryConditional cond = QueryConditional.keyEqualTo(Key.builder().partitionValue("CUST#" + customerId).build());
// begins_with(GSI2SK, "TYPE#<type>#")
        Expression filter = Expression.builder()
                .expression("begins_with(GSI2SK, :p)")
                .putExpressionValue(":p", AttributeValue.builder().s("TYPE#" + type + "#").build())
                .build();
        QueryEnhancedRequest req = QueryEnhancedRequest.builder().queryConditional(cond).filterExpression(filter).build();
        List<TransactionItem> out = new ArrayList<>();
        idx.query(req).stream().forEach(p -> out.addAll(p.items()));
        return out;
    }

    // ===== Helpers para construir claves =====
    public TransactionItem buildKeys(String customerId, String transactionId, String type, java.time.Instant createdAt, String fundId) {
        TransactionItem it = new TransactionItem();
        String ts = TS.format(createdAt);
        it.setCustomerId(customerId);
        it.setTransactionId(transactionId);
        it.setType(type);
        it.setFundId(fundId);
        it.setCreatedAt(createdAt);
        it.setPk("CUST#" + customerId);
        it.setSk("TX#" + ts + "#" + transactionId);
        it.setGsi2pk("CUST#" + customerId);
        it.setGsi2sk("TYPE#" + type + "#" + ts + "#" + transactionId);
        return it;
    }

}
package com.pactual.btg.pactual.infraestructure.persistence.dynamodb;

import com.pactual.btg.pactual.infraestructure.config.DynamoProps;
import com.pactual.btg.pactual.infraestructure.persistence.dynamodb.model.SubscriptionItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;


import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Adapter de persistencia para Suscripciones usando DynamoDB Enhanced Client.
 *
 * Requisitos previos:
 * - Bean DynamoDbEnhancedClient definido en tu configuración
 * - Propiedad app.dynamodb.table con el nombre de la tabla
 * - Estructura single-table con atributos PK/SK, GSI1PK/GSI1SK, GSI2PK/GSI2SK.
 */
@Repository
public class SubscriptionRepository {

    private final DynamoDbEnhancedClient enhancedClient;
    private final DynamoProps dynamoProps;

    public SubscriptionRepository(DynamoDbEnhancedClient enhancedClient, DynamoProps dynamoProps) {
        this.enhancedClient = enhancedClient;
        this.dynamoProps = dynamoProps;
    }

    private DynamoDbTable<SubscriptionItem> table() {
        return enhancedClient.table(dynamoProps.getTable(), TableSchema.fromBean(SubscriptionItem.class));
    }


    /** Inserta o reemplaza una suscripción. */
    public void save(SubscriptionItem item) {
        table().putItem(item);
    }


    /** Obtiene una suscripción por customerId + fundId + subscriptionId. */
    public Optional<SubscriptionItem> findByIds(String customerId, String fundId, String subscriptionId) {
        Key key = Key.builder()
                .partitionValue("CUST#" + customerId)
                .sortValue("SUB#" + fundId + "#" + subscriptionId)
                .build();
        SubscriptionItem out = table().getItem(r -> r.key(key));
        return Optional.ofNullable(out);
    }

    /** Lista suscripciones de un cliente. Si status!=null, filtra por estado (ACTIVE/CANCELLED) usando FilterExpression. */
    public List<SubscriptionItem> listByCustomer(String customerId, String status /* nullable */) {
        QueryConditional cond = QueryConditional.keyEqualTo(Key.builder().partitionValue("CUST#" + customerId).build());
// Prefijo de SK para suscripciones (excluye PROFILE o TX)
        Expression skBegins = Expression.builder()
                .expression("begins_with(SK, :sub)")
                .putExpressionValue(":sub", AttributeValue.builder().s("SUB#").build())
                .build();


        QueryEnhancedRequest.Builder req = QueryEnhancedRequest.builder().queryConditional(cond).filterExpression(skBegins);


        if (status != null) {
// Agregamos un AND status = :status
            Expression andStatus = Expression.builder()
                    .expression("status = :st")
                    .putExpressionValue(":st", AttributeValue.builder().s(status).build())
                    .build();
// EnhancedClient no compone expresiones; encadenamos con AND en un solo string
            Expression finalExpr = Expression.builder()
                    .expression(skBegins.expression() + " AND " + andStatus.expression())
                    .expressionValues(merge(skBegins.expressionValues(), andStatus.expressionValues()))
                    .build();
            req = req.filterExpression(finalExpr);
        }


        return collect(table().query(req.build()));
    }


    /** Lista suscripciones por fondo (usa GSI1: GSI1PK=FUND#<fundId>). */
    public List<SubscriptionItem> listByFund(String fundId) {
        DynamoDbIndex<SubscriptionItem> idx = table().index("GSI1");
        QueryConditional cond = QueryConditional.keyEqualTo(
                Key.builder().partitionValue("FUND#" + fundId).build()
        );
        return idx.query(r -> r.queryConditional(cond))
                .stream()
                .flatMap(p -> p.items().stream())
                .collect(Collectors.toList());
    }


    /** Actualiza el estado a CANCELLED y setea cancelledAt, si corresponde. */
    public void markCancelled(String customerId, String fundId, String subscriptionId, Instant cancelledAt) {
        findByIds(customerId, fundId, subscriptionId).ifPresent(item -> {
            item.setStatus("CANCELLED");
            item.setCancelledAt(cancelledAt);
            save(item);
        });
    }

    // ================= Utilidades =================


    private static Map<String, AttributeValue> merge(Map<String, AttributeValue> a, Map<String, AttributeValue> b) {
        Map<String, AttributeValue> m = new HashMap<>();
        if (a != null) m.putAll(a);
        if (b != null) m.putAll(b);
        return m;
    }


    private static <T> List<T> collect(PageIterable<T> pages) {
        List<T> list = new ArrayList<>();
        for (Page<T> p : pages) list.addAll(p.items());
        return list;
    }
}
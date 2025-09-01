package com.pactual.btg.pactual.infraestructure.persistence.dynamodb;

import com.pactual.btg.pactual.infraestructure.config.DynamoProps;
import com.pactual.btg.pactual.infraestructure.persistence.dynamodb.model.CustomerItem;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;

import java.util.*;


import java.math.BigDecimal;

@Repository
public class CustomerRepository {

    private final DynamoDbEnhancedClient enhancedClient;
    private final DynamoProps dynamoProps;

    public CustomerRepository(DynamoDbEnhancedClient enhancedClient, DynamoProps dynamoProps) {
        this.enhancedClient = enhancedClient;
        this.dynamoProps = dynamoProps;
    }

    private DynamoDbTable<CustomerItem> table() {
        return enhancedClient.table(dynamoProps.getTable(), TableSchema.fromBean(CustomerItem.class));
    }


    public Optional<CustomerItem> findById(String customerId) {
        Key key = Key.builder()
                .partitionValue("CUST#" + customerId)
                .sortValue("PROFILE")
                .build();
        return Optional.ofNullable(table().getItem(r -> r.key(key)));
    }


    /**
     * Crea o reemplaza el perfil del cliente (usa para seeds o alta de cliente).
     */
    public void upsertProfile(CustomerItem item) {
// Asegura claves
        item.setPk("CUST#" + item.getCustomerId());
        item.setSk("PROFILE");
        table().putItem(item);
    }


    /**
     * Ajusta el balance sumando delta (positivo o negativo). Retorna el balance final.
     * Nota: implementación simple (read-modify-write). Para alta concurrencia, usar UpdateExpression.
     */
    public BigDecimal adjustBalance(String customerId, BigDecimal delta) {
        CustomerItem current = findById(customerId)
                .orElseThrow(() -> new NoSuchElementException("Customer not found: " + customerId));
        BigDecimal newBalance = (current.getBalance() == null ? BigDecimal.ZERO : current.getBalance()).add(delta);
        current.setBalance(newBalance);
        table().putItem(current);
        return newBalance;
    }


    /** Cambia la preferencia de notificación (EMAIL|SMS). */
    public void updateNotificationPreference(String customerId, String preference) {
        CustomerItem current = findById(customerId)
                .orElseThrow(() -> new NoSuchElementException("Customer not found: " + customerId));
        current.setNotificationPreference(preference);
        table().putItem(current);
    }
}
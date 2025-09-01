package com.pactual.btg.pactual.application.usecase;

import java.time.Instant;

public interface CancelSubscriptionUseCase {

    /**
     * Cancela una suscripción ACTIVE, reintegrando el saldo y registrando transacción CANCEL.
     *
     * Nota: por diseño de clave en DynamoDB, este caso de uso requiere localizar la suscripción
     * por (customerId, fundId, subscriptionId). Si tu API solo recibe subscriptionId, añade un índice
     * adicional o ajusta el controlador para enviar los tres ids.
     */
    CancelResult cancel(String customerId, String fundId, String subscriptionId, String idempotencyKey);


    record CancelResult(String subscriptionId, String status, Instant cancelledAt) {}
}

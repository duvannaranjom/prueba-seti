package com.pactual.btg.pactual.application.usecase;

import com.pactual.btg.pactual.domain.Subscription;

import java.math.BigDecimal;
public interface OpenSubscriptionUseCase {
    /**
     * Abre una suscripción (valida mínimo y saldo, descuenta balance y crea transacción OPEN).
     * @param customerId Id del cliente (normalmente derivado del JWT)
     * @param fundId Id del fondo
     * @param amount Monto a suscribir
     * @param idempotencyKey (opcional) para evitar duplicados si el cliente reintenta
     * @return Subscription de dominio (estado ACTIVE)
     */
    Subscription open(String customerId, String fundId, BigDecimal amount, String idempotencyKey);
}

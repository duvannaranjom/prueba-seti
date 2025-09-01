package com.pactual.btg.pactual.application.service;

import com.pactual.btg.pactual.application.exception.InvalidStateException;
import com.pactual.btg.pactual.application.exception.NotFoundException;
import com.pactual.btg.pactual.application.usecase.CancelSubscriptionUseCase;
import com.pactual.btg.pactual.domain.Transaction;
import com.pactual.btg.pactual.infraestructure.persistence.dynamodb.CustomerRepository;
import com.pactual.btg.pactual.infraestructure.persistence.dynamodb.SubscriptionRepository;
import com.pactual.btg.pactual.infraestructure.persistence.dynamodb.TransactionRepository;
import com.pactual.btg.pactual.infraestructure.persistence.dynamodb.mapper.TransactionItemMapper;
import com.pactual.btg.pactual.infraestructure.persistence.dynamodb.model.SubscriptionItem;
import com.pactual.btg.pactual.infraestructure.persistence.dynamodb.model.TransactionItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.Instant;


@Service
@RequiredArgsConstructor
public class CancelSubscriptionUseCaseImpl implements CancelSubscriptionUseCase {


    private final SubscriptionRepository subscriptionRepo;
    private final TransactionRepository txRepo;
    private final CustomerRepository customerRepo;
    private final TransactionItemMapper txMapper;


    @Override
    public CancelResult cancel(String customerId, String fundId, String subscriptionId, String idempotencyKey) {
// 1) Buscar suscripción
        SubscriptionItem item = subscriptionRepo.findByIds(customerId, fundId, subscriptionId)
                .orElseThrow(() -> new NotFoundException("Subscription not found: " + subscriptionId));


        if (!"ACTIVE".equals(item.getStatus())) {
            throw new InvalidStateException("Subscription not ACTIVE");
        }


// 2) Marcar cancelado y persistir
        Instant now = Instant.now();
        subscriptionRepo.markCancelled(customerId, fundId, subscriptionId, now);


// 3) Crear transacción CANCEL y persistir
        var txDomain = Transaction.cancel(customerId, fundId, item.getAmount());
        TransactionItem txItem = txMapper.toItem(txDomain);
        txRepo.save(txItem);


// 4) Reintegrar balance
        customerRepo.adjustBalance(customerId, item.getAmount());


        return new CancelResult(subscriptionId, "CANCELLED", now);
    }
}

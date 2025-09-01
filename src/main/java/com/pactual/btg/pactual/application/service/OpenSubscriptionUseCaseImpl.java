package com.pactual.btg.pactual.application.service;

import com.pactual.btg.pactual.application.exception.InsufficientBalanceException;
import com.pactual.btg.pactual.application.exception.MinimumAmountException;
import com.pactual.btg.pactual.application.exception.NotFoundException;
import com.pactual.btg.pactual.application.usecase.OpenSubscriptionUseCase;
import com.pactual.btg.pactual.domain.Subscription;
import com.pactual.btg.pactual.domain.Transaction;
import com.pactual.btg.pactual.infraestructure.persistence.dynamodb.CustomerRepository;
import com.pactual.btg.pactual.infraestructure.persistence.dynamodb.FundRepository;
import com.pactual.btg.pactual.infraestructure.persistence.dynamodb.SubscriptionRepository;
import com.pactual.btg.pactual.infraestructure.persistence.dynamodb.TransactionRepository;
import com.pactual.btg.pactual.infraestructure.persistence.dynamodb.mapper.SubscriptionItemMapper;
import com.pactual.btg.pactual.infraestructure.persistence.dynamodb.mapper.TransactionItemMapper;
import com.pactual.btg.pactual.infraestructure.persistence.dynamodb.model.SubscriptionItem;
import com.pactual.btg.pactual.infraestructure.persistence.dynamodb.model.TransactionItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
public class OpenSubscriptionUseCaseImpl implements OpenSubscriptionUseCase {


    private final CustomerRepository customerRepo;
    private final FundRepository fundRepo;
    private final SubscriptionRepository subscriptionRepo;
    private final TransactionRepository txRepo;
    private final SubscriptionItemMapper subscriptionMapper;
    private final TransactionItemMapper txMapper;


    @Override
    public Subscription open(String customerId, String fundId, BigDecimal amount, String idempotencyKey) {
// 1) Cargar Fund y validar mínimo
        var fund = fundRepo.findById(fundId).orElseThrow(() -> new NotFoundException("Fund not found: " + fundId));
        if (amount == null || fund.getMinAmount() != null && amount.compareTo(fund.getMinAmount()) < 0) {
            throw new MinimumAmountException("El monto es inferior al mínimo del fondo");
        }


// 2) Cargar Customer y validar saldo
        var customer = customerRepo.findById(customerId).orElseThrow(() -> new NotFoundException("Customer not found: " + customerId));
        BigDecimal balance = customer.getBalance() == null ? BigDecimal.ZERO : customer.getBalance();
        if (balance.compareTo(amount) < 0) {
// Mensaje exacto solicitado en la prueba
            String fundName = fund.getName() != null ? fund.getName() : fundId;
            throw new InsufficientBalanceException("No tiene saldo disponible para vincularse al fondo " + fundName);
        }


// 3) Crear modelos de dominio
        Subscription sub = Subscription.newActive(customerId, fundId, amount);
        Transaction tx = Transaction.open(customerId, fundId, amount);


// 4) Persistir
// 4.1) Suscripción
        SubscriptionItem subItem = subscriptionMapper.toItem(sub);
        subItem.setStatus("ACTIVE");
        subscriptionRepo.save(subItem);


// 4.2) Transacción
        TransactionItem txItem = txMapper.toItem(tx);
        txRepo.save(txItem);


// 4.3) Ajustar balance (-amount)
        customerRepo.adjustBalance(customerId, amount.negate());


        return sub; // el controlador puede mapearlo a Response DTO
    }
}

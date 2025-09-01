package com.pactual.btg.pactual.application.service.query;

import com.pactual.btg.pactual.application.usecase.ListTransactionsQuery;
import com.pactual.btg.pactual.infraestructure.persistence.dynamodb.TransactionRepository;
import com.pactual.btg.pactual.web.dto.response.TransactionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListTransactionsQueryImpl implements ListTransactionsQuery {
    private final TransactionRepository txRepo;

    @Override
    public List<TransactionResponse> listByCustomer(String customerId) {
        return txRepo.listByCustomer(customerId).stream()
                .map(t -> new TransactionResponse(
                        t.getTransactionId(), t.getCustomerId(), t.getFundId(),
                        t.getType(), t.getAmount(), t.getCreatedAt()))
                .toList();
    }
}
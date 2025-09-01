package com.pactual.btg.pactual.application.usecase;

import com.pactual.btg.pactual.web.dto.response.TransactionResponse;

import java.util.List;

public interface ListTransactionsQuery {
    List<TransactionResponse> listByCustomer(String customerId);
}

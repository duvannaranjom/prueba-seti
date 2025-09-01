package com.pactual.btg.pactual.web.controller;

import com.pactual.btg.pactual.application.usecase.ListTransactionsQuery;
import com.pactual.btg.pactual.web.dto.response.TransactionResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/V1/transactions")
class TransactionsController {
    private final ListTransactionsQuery listTx;
    TransactionsController(ListTransactionsQuery listTx){ this.listTx = listTx; }


    @GetMapping
    List<TransactionResponse> list(@RequestParam String customerId){
        return listTx.listByCustomer(customerId);
    }
}

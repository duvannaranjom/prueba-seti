package com.pactual.btg.pactual.web.controller;

import com.pactual.btg.pactual.application.usecase.ListFundsQuery;
import com.pactual.btg.pactual.web.dto.response.FundResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/V1/funds")
class FundsController {
    private final ListFundsQuery funds;
    FundsController(ListFundsQuery funds){ this.funds = funds; }


    @GetMapping
    List<FundResponse> list(){
        return funds.listAll();
    }


    @GetMapping("/{fundId}")
    FundResponse get(@PathVariable String fundId){
        return funds.getById(fundId);
    }
}

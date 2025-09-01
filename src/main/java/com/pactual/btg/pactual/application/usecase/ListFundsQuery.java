package com.pactual.btg.pactual.application.usecase;

import com.pactual.btg.pactual.web.dto.response.FundResponse;

import java.util.List;

public interface ListFundsQuery {
    List<FundResponse> listAll();
    FundResponse getById(String fundId);
}

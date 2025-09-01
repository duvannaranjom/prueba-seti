package com.pactual.btg.pactual.application.service.query;

import com.pactual.btg.pactual.application.exception.NotFoundException;
import com.pactual.btg.pactual.application.usecase.ListFundsQuery;
import com.pactual.btg.pactual.infraestructure.persistence.dynamodb.FundRepository;
import com.pactual.btg.pactual.web.dto.response.FundResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListFundsQueryImpl implements ListFundsQuery {
    private final FundRepository fundRepo;

    @Override
    public List<FundResponse> listAll() {
        return fundRepo.listAll().stream()
                .map(f -> new FundResponse(f.getFundId(), f.getName(), f.getMinAmount(), f.getCategory()))
                .toList();
    }

    @Override
    public FundResponse getById(String fundId) {
        var f = fundRepo.findById(fundId)
                .orElseThrow(() -> new NotFoundException("Fund not found: " + fundId));
        return new FundResponse(f.getFundId(), f.getName(), f.getMinAmount(), f.getCategory());
    }
}

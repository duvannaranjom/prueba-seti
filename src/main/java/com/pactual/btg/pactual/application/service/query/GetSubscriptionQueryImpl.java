package com.pactual.btg.pactual.application.service.query;

import com.pactual.btg.pactual.application.exception.NotFoundException;
import com.pactual.btg.pactual.application.usecase.GetSubscriptionQuery;
import com.pactual.btg.pactual.domain.Subscription;
import com.pactual.btg.pactual.infraestructure.persistence.dynamodb.SubscriptionRepository;
import com.pactual.btg.pactual.infraestructure.persistence.dynamodb.mapper.SubscriptionItemMapper;
import com.pactual.btg.pactual.web.dto.response.SubscriptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetSubscriptionQueryImpl implements GetSubscriptionQuery {
    private final SubscriptionRepository subRepo;
    private final SubscriptionItemMapper mapper;

    @Override
    public Subscription getById(String customerId, String fundId, String subscriptionId) {
        var item = subRepo.findByIds(customerId, fundId, subscriptionId)
                .orElseThrow(() -> new NotFoundException("Subscription not found: " + subscriptionId));
        return mapper.toDomain(item);
    }

    @Override
    public List<Subscription> listByCustomer(String customerId, String status) {
        return subRepo.listByCustomer(customerId, status).stream()
                .map(mapper::toDomain)
                .toList();
    }
}

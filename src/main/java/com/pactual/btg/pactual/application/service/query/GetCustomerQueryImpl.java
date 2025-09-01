package com.pactual.btg.pactual.application.service.query;

import com.pactual.btg.pactual.application.exception.NotFoundException;
import com.pactual.btg.pactual.application.usecase.GetCustomerQuery;
import com.pactual.btg.pactual.domain.Customer;
import com.pactual.btg.pactual.infraestructure.persistence.dynamodb.CustomerRepository;
import com.pactual.btg.pactual.infraestructure.persistence.dynamodb.mapper.CustomerItemMapper;
import com.pactual.btg.pactual.web.dto.response.CustomerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetCustomerQueryImpl implements GetCustomerQuery {
    private final CustomerRepository customerRepo;
    private final CustomerItemMapper customerMapper;

    @Override
    public Customer getById(String customerId) {
        var item = customerRepo.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found: " + customerId));
        return customerMapper.toDomain(item); // ← convierte a dominio
    }
}

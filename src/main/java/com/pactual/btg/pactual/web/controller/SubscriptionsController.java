package com.pactual.btg.pactual.web.controller;

import com.pactual.btg.pactual.application.usecase.CancelSubscriptionUseCase;
import com.pactual.btg.pactual.application.usecase.GetSubscriptionQuery;
import com.pactual.btg.pactual.application.usecase.OpenSubscriptionUseCase;
import com.pactual.btg.pactual.domain.Subscription;
import com.pactual.btg.pactual.web.dto.request.OpenSubscriptionRequest;
import com.pactual.btg.pactual.web.dto.response.CancelSubscriptionResponse;
import com.pactual.btg.pactual.web.dto.response.SubscriptionResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@Validated
@RestController
@RequestMapping("/V1/subscriptions")
class SubscriptionsController {
    private final OpenSubscriptionUseCase openUC;
    private final CancelSubscriptionUseCase cancelUC;
    private final GetSubscriptionQuery query;


    SubscriptionsController(OpenSubscriptionUseCase openUC,
                            CancelSubscriptionUseCase cancelUC,
                            GetSubscriptionQuery query) {
        this.openUC = openUC;
        this.cancelUC = cancelUC;
        this.query = query;
    }


    @PostMapping
    ResponseEntity<SubscriptionResponse> open(@RequestHeader("X-Customer-Id") String customerId,
                                              @RequestHeader(value = "Idempotency-Key", required = false) String idemKey,
                                              @Valid @RequestBody OpenSubscriptionRequest req){
        String idk = idemKey != null ? idemKey : UUID.randomUUID().toString();
        Subscription sub = openUC.open(customerId, req.fundId(), req.amount(), idk);
        SubscriptionResponse resp = new SubscriptionResponse(
                sub.getSubscriptionId(), sub.getCustomerId(), sub.getFundId(),
                sub.getAmount(), sub.getStatus(), sub.getCreatedAt(), sub.getCancelledAt()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }


    @PostMapping("/{fundId}/{subscriptionId}/cancel")
    CancelSubscriptionResponse cancel(@RequestHeader("X-Customer-Id") String customerId,
                                      @RequestHeader(value = "Idempotency-Key", required = false) String idemKey,
                                      @PathVariable String fundId,
                                      @PathVariable String subscriptionId){
        String idk = idemKey != null ? idemKey : UUID.randomUUID().toString();
        var result = cancelUC.cancel(customerId, fundId, subscriptionId, idk);
        return new CancelSubscriptionResponse(result.subscriptionId(), result.status(), result.cancelledAt());
    }


    @GetMapping("/{subscriptionId}")
    SubscriptionResponse get(@PathVariable String subscriptionId, @RequestParam String customerId, @RequestParam String fundId){
        var sub = query.getById(customerId, fundId, subscriptionId);
        return new SubscriptionResponse(
                sub.getSubscriptionId(), sub.getCustomerId(), sub.getFundId(),
                sub.getAmount(), sub.getStatus(), sub.getCreatedAt(), sub.getCancelledAt()
        );
    }


    @GetMapping
    List<SubscriptionResponse> listByCustomer(@RequestParam String customerId,
                                              @RequestParam(required = false) String status){
        return query.listByCustomer(customerId, status).stream()
                .map(sub -> new SubscriptionResponse(
                        sub.getSubscriptionId(),
                        sub.getCustomerId(),
                        sub.getFundId(),
                        sub.getAmount(),
                        sub.getStatus(),
                        sub.getCreatedAt(),
                        sub.getCancelledAt()
                ))
                .toList();
    }
}

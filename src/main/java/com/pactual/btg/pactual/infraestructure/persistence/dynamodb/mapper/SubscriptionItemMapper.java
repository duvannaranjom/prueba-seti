package com.pactual.btg.pactual.infraestructure.persistence.dynamodb.mapper;

import com.pactual.btg.pactual.domain.Subscription;
import com.pactual.btg.pactual.infraestructure.persistence.dynamodb.model.SubscriptionItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SubscriptionItemMapper {

    SubscriptionItem toItem(Subscription src);
    Subscription toDomain(SubscriptionItem item);


    @AfterMapping
    default void setKeys(@MappingTarget SubscriptionItem item, Subscription src) {

        item.setPk("CUST#" + src.getCustomerId());
        item.setSk("SUB#" + src.getFundId() + "#" + src.getSubscriptionId());

        item.setGsi1pk("FUND#" + src.getFundId());
        item.setGsi1sk("SUB#" + src.getSubscriptionId());

        item.setCustomerId(src.getCustomerId());
        item.setFundId(src.getFundId());
        item.setSubscriptionId(src.getSubscriptionId());
    }
}

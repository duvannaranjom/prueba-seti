package com.pactual.btg.pactual.infraestructure.persistence.dynamodb.mapper;

import com.pactual.btg.pactual.domain.Fund;
import com.pactual.btg.pactual.infraestructure.persistence.dynamodb.model.FundItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FundItemMapper {

    FundItem toItem(Fund src);
    Fund toDomain(FundItem item);


    @AfterMapping
    default void setKeys(@MappingTarget FundItem item, Fund src) {
        item.setPk("FUND#" + src.getFundId());
        item.setSk("META");
        item.setFundId(src.getFundId());
    }
}

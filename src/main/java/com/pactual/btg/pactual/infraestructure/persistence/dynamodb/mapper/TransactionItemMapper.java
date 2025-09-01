package com.pactual.btg.pactual.infraestructure.persistence.dynamodb.mapper;

import com.pactual.btg.pactual.domain.Transaction;
import com.pactual.btg.pactual.infraestructure.persistence.dynamodb.model.TransactionItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface TransactionItemMapper {

    DateTimeFormatter TS = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").withZone(ZoneOffset.UTC);


    TransactionItem toItem(Transaction src);
    Transaction toDomain(TransactionItem item);


    @AfterMapping
    default void setKeys(@MappingTarget TransactionItem item, Transaction src) {
        String ts = TS.format(src.getCreatedAt());
        item.setPk("CUST#" + src.getCustomerId());
        item.setSk("TX#" + ts + "#" + src.getTransactionId());
        item.setGsi2pk("CUST#" + src.getCustomerId());
        item.setGsi2sk("TYPE#" + src.getType() + "#" + ts + "#" + src.getTransactionId());

        item.setCustomerId(src.getCustomerId());
        item.setFundId(src.getFundId());
        item.setTransactionId(src.getTransactionId());
    }
}

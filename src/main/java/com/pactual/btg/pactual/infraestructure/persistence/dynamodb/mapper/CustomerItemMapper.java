package com.pactual.btg.pactual.infraestructure.persistence.dynamodb.mapper;



import com.pactual.btg.pactual.domain.Customer;
import com.pactual.btg.pactual.infraestructure.persistence.dynamodb.model.CustomerItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface CustomerItemMapper {

    CustomerItem toItem(Customer src);
    Customer toDomain(CustomerItem item);


    @AfterMapping
    default void setKeys(@MappingTarget CustomerItem item, Customer src) {
        item.setPk("CUST#" + src.getCustomerId());
        item.setSk("PROFILE");
        item.setCustomerId(src.getCustomerId());
    }
}

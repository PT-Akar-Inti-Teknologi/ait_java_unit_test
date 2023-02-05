package org.ait.project.unittestexample.modules.customerorder.transform;

import org.ait.project.unittestexample.modules.customerorder.dto.CustomerOrderLineItemDTO;
import org.ait.project.unittestexample.modules.customerorder.model.jpa.CustomerOrderLineItem;
import org.ait.project.unittestexample.modules.food.transform.FoodMapper;
import org.ait.project.unittestexample.shared.utils.transform.EntityMapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * Mapper for the entity {@link CustomerOrderLineItem} and its DTO {@link CustomerOrderLineItemDTO}.
 */
@Mapper(uses = {FoodMapper.class, CustomerOrderMapper.class})
public interface CustomerOrderLineItemMapper 
       extends EntityMapper<CustomerOrderLineItemDTO, CustomerOrderLineItem> {

    CustomerOrderLineItemMapper INSTANCE = Mappers.getMapper(CustomerOrderLineItemMapper.class);

	@Mapping(source = "food.id", target = "foodId")
    @Mapping(source = "customerOrder.id", target = "customerOrderId")
    CustomerOrderLineItemDTO toDto(CustomerOrderLineItem customerOrderLineItem);

    @Mapping(source = "foodId", target = "food.id")
    @Mapping(source = "customerOrderId", target = "customerOrder.id")
    CustomerOrderLineItem toEntity(CustomerOrderLineItemDTO customerOrderLineItemDTO);

}

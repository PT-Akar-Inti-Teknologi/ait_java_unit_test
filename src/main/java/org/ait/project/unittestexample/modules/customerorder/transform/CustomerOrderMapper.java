package org.ait.project.unittestexample.modules.customerorder.transform;

import org.ait.project.unittestexample.modules.customerorder.dto.CustomerOrderDTO;
import org.ait.project.unittestexample.modules.customerorder.dto.CustomerOrderFromMobileApp;
import org.ait.project.unittestexample.modules.customerorder.model.jpa.CustomerOrder;
import org.ait.project.unittestexample.shared.utils.transform.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


/**
 * Mapper for the entity {@link CustomerOrder} and its DTO {@link CustomerOrderDTO}.
 */
@Mapper(uses = {})
public interface CustomerOrderMapper extends EntityMapper<CustomerOrderDTO, CustomerOrder> {

	CustomerOrderMapper INSTANCE = Mappers.getMapper(CustomerOrderMapper.class);

    CustomerOrder toEntity(CustomerOrderDTO customerOrderDTO);
    
    CustomerOrderDTO toDto(CustomerOrder entity);
    
    CustomerOrderFromMobileApp toResponse(CustomerOrder entity);

    default CustomerOrder fromId(Long id) {
        if (id == null) {
            return null;
        }
        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setId(id);
        return customerOrder;
    }
}

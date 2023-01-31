package org.ait.project.unittestexample.modules.customerorder.transform;

import static org.junit.jupiter.api.Assertions.*;

import org.ait.module.java.unittest.mappertester.EntityMapperTester;
import org.ait.project.unittestexample.modules.customerorder.dto.CustomerOrderLineItemDTO;
import org.ait.project.unittestexample.modules.customerorder.model.jpa.CustomerOrderLineItem;
import org.junit.jupiter.api.Test;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerOrderLineItemMapperTest 
       extends EntityMapperTester<CustomerOrderLineItemDTO, CustomerOrderLineItem>{

	private CustomerOrderLineItemMapper mapper = CustomerOrderLineItemMapper.INSTANCE;
	
	private CustomerOrderLineItem entity;
	
	private CustomerOrderLineItemDTO dto;

	
	@Test
	public void testMapDtoToEntity() {
		CustomerOrderLineItem resultEntity = mapper.toEntity(dto);
		
		assertEquals(dto.getCustomerOrderId(), resultEntity.getCustomerOrder().getId());
		assertEquals(dto.getFoodId(), resultEntity.getFood().getId());
		assertEquals(dto.getId(), resultEntity.getId());
		assertEquals(dto.getQuantity(), resultEntity.getQuantity());
	}

	@Test
	public void testMapEntityToDto() {
        CustomerOrderLineItemDTO resultDto = mapper.toDto(entity);
		
		assertEquals(entity.getCustomerOrder().getId(), resultDto.getCustomerOrderId());
		assertEquals(entity.getFood().getId(), resultDto.getFoodId());
		assertEquals(entity.getId(), resultDto.getId());
		assertEquals(entity.getQuantity(), resultDto.getQuantity());
		
	}

	
	@Override
	public void generateEntity() {
		entity = getEasyRandom().nextObject(CustomerOrderLineItem.class);
	}

	@Override
	public void generateDto() {
		dto = getEasyRandom().nextObject(CustomerOrderLineItemDTO.class);
	}

}

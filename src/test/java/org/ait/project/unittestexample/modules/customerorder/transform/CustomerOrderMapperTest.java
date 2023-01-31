package org.ait.project.unittestexample.modules.customerorder.transform;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.ait.module.java.unittest.mappertester.EntityMapperTester;
import org.ait.project.unittestexample.modules.customerorder.dto.CustomerOrderDTO;
import org.ait.project.unittestexample.modules.customerorder.model.jpa.CustomerOrder;
import org.junit.jupiter.api.Test;

import lombok.Getter;
import lombok.Setter;

/**
 * Unit Test for {@link CustomerOrderMapper}.
 * 
 * @author AIT Java Collective
 *
 */
@Getter
@Setter
public class CustomerOrderMapperTest 
       extends EntityMapperTester<CustomerOrderDTO, CustomerOrder> {

	
	private CustomerOrderMapper mapper = CustomerOrderMapper.INSTANCE;
	
	private CustomerOrder entity;
	
	private CustomerOrderDTO dto;
	

	@Test
	@Override
	public void testMapDtoToEntity() {
		CustomerOrder resultEntity = mapper.toEntity(getDto());
		
		assertEquals(getDto().getGuestName(), resultEntity.getGuestName());
		assertEquals(getDto().getId(), resultEntity.getId());
		assertEquals(getDto().getOrderType(), resultEntity.getOrderType());
		assertEquals(getDto().getQueueNumber(), resultEntity.getQueueNumber());
		assertEquals(getDto().getOrderTime(), resultEntity.getOrderTime());
		
	}

	@Test
	@Override
	public void testMapEntityToDto() {
		CustomerOrderDTO resultDto = mapper.toDto(getEntity());
		
		assertEquals(getEntity().getGuestName(), resultDto.getGuestName());
		assertEquals(getEntity().getId(), resultDto.getId());
		assertEquals(getEntity().getOrderType(), resultDto.getOrderType());
		assertEquals(getEntity().getQueueNumber(), resultDto.getQueueNumber());
		assertEquals(getEntity().getOrderTime(), resultDto.getOrderTime());
		
	}

	@Override
	public void generateEntity() {
		entity = getEasyRandom().nextObject(CustomerOrder.class);
	}

	@Override
	public void generateDto() {
		dto = getEasyRandom().nextObject(CustomerOrderDTO.class);
	}

}

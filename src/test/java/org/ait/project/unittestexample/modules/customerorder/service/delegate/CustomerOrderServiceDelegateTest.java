package org.ait.project.unittestexample.modules.customerorder.service.delegate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.ait.module.java.unittest.servicetester.ServiceDelegateTester;
import org.ait.project.unittestexample.modules.customerorder.dto.CustomerOrderDTO;
import org.ait.project.unittestexample.modules.customerorder.model.jpa.CustomerOrder;
import org.ait.project.unittestexample.modules.customerorder.model.jpa.CustomerOrderRepository;
import org.ait.project.unittestexample.modules.customerorder.service.delegate.CustomerOrderLineItemServiceDelegateTest.TestParameters;
import org.ait.project.unittestexample.modules.customerorder.transform.CustomerOrderMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.repository.JpaRepository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
public class CustomerOrderServiceDelegateTest 
       extends ServiceDelegateTester<CustomerOrderServiceDelegate, CustomerOrder>{

	private CustomerOrderRepository repository;
	
	private CustomerOrder entity;
	
	private CustomerOrderServiceDelegate service;
	
	private Map<TestParameters, Object> testParameterValues;
	
	private CustomerOrderMapper mapper = CustomerOrderMapper.INSTANCE;
	
	@Test
	public void testCreateNew() {
		CustomerOrderDTO request = getEasyRandom().nextObject(CustomerOrderDTO.class);
		request.setId(null);
		CustomerOrder saveResult = mapper.toEntity(request);
		saveResult.setId(getEasyRandom().nextLong());
		
		when(repository.save(any())).thenReturn(saveResult);
		
		CustomerOrder actual = service.createNew(request);
		
		assertEquals(saveResult.getId(), actual.getId());
		assertEquals(saveResult.getGuestName(), actual.getGuestName());
		assertEquals(saveResult.getOrderType(), actual.getOrderType());
		assertEquals(saveResult.getQueueNumber(), actual.getQueueNumber());
		assertTrue(actual.getOrderTime().isEqual(saveResult.getOrderTime()));
	}

	@Test
	public void testUpdateExisting() {
		CustomerOrderDTO update = getEasyRandom().nextObject(CustomerOrderDTO.class);
		update.setId(getEntity().getId());
		
		CustomerOrder entityToSave = mapper.toEntity(update);
		
		when(getRepository().findById((Long)getTestParameterValues().get(TestParameters.CUSTOMER_ORDER_ID))).thenReturn(Optional.of(getEntity()));
		when(getRepository().save(entityToSave)).thenReturn(entityToSave);
		
		CustomerOrder actual = service.updateExisting(update);
		
		assertEquals(update.getId(), actual.getId());
		assertEquals(update.getGuestName(), actual.getGuestName());
		assertEquals(update.getOrderType(), actual.getOrderType());
		assertEquals(update.getQueueNumber(), actual.getQueueNumber());
		assertTrue(actual.getOrderTime().isEqual(update.getOrderTime()));
		
		Long nonExistentId = (Long)getTestParameterValues().get(TestParameters.NONEXISTENT_CUSTOMER_ORDER_ID);
		update.setId(nonExistentId);
		when(getRepository().findById(nonExistentId)).thenReturn(Optional.empty());
		
		assertThrows(EntityNotFoundException.class, ()->{
			service.updateExisting(update);
		});

	}

	@Test
	public void testDeleteExisting() {
		Long existingId = (Long)getTestParameterValues().get(TestParameters.CUSTOMER_ORDER_ID);
		
		service.deleteExisting(existingId);
		
		verify(getRepository()).deleteById(existingId);
		
		when(getRepository().findById(existingId)).thenReturn(Optional.empty());
		
		assertThrows(EntityNotFoundException.class, ()->{
			getService().fetchOneById(existingId);
		});
	}
	
	@Test
	public void testFetchOneById() {
		Long existingId = (Long)getTestParameterValues().get(TestParameters.CUSTOMER_ORDER_ID);
		when(getRepository().findById(existingId)).thenReturn(Optional.of(getEntity()));
		CustomerOrder actualPositive = service.fetchOneById(existingId);
		
		assertNotNull(actualPositive);
		
		Long nonExistentId = (Long)getTestParameterValues().get(TestParameters.NONEXISTENT_CUSTOMER_ORDER_ID);
		when(getRepository().findById(nonExistentId)).thenReturn(Optional.empty());
		
		assertThrows(EntityNotFoundException.class, ()->{
			service.fetchOneById(nonExistentId);
		});
	}
	

	@RequiredArgsConstructor
	enum TestParameters {
		CUSTOMER_ORDER_ID(Long.class),
		NONEXISTENT_CUSTOMER_ORDER_ID(Long.class);
		

		private final Class<?> valueType;
	}
	
	@BeforeEach
	public void beforeEachTest() {
		super.beforeEachTest();
		repository = Mockito.mock(CustomerOrderRepository.class);
		service = new CustomerOrderServiceDelegate(repository);
	}
	
	@Override
	public void generateEntity() {
		entity = getEasyRandom().nextObject(CustomerOrder.class);
		entity.setId((Long) getTestParameterValues().get(TestParameters.CUSTOMER_ORDER_ID));
	}

	@Override
	public void generateTestParameterValues() {
		testParameterValues = new HashMap<>();
		testParameterValues.put(TestParameters.CUSTOMER_ORDER_ID, getEasyRandom().nextLong());
		testParameterValues.put(TestParameters.NONEXISTENT_CUSTOMER_ORDER_ID, getEasyRandom().nextLong());
	}



}

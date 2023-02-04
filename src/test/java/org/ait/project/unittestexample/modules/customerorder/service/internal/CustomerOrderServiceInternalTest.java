package org.ait.project.unittestexample.modules.customerorder.service.internal;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.ait.module.java.unittest.servicetester.ServiceInternalTester;
import org.ait.project.unittestexample.modules.customerorder.dto.CustomerOrderDTO;
import org.ait.project.unittestexample.modules.customerorder.dto.CustomerOrderFromMobileApp;
import org.ait.project.unittestexample.modules.customerorder.dto.CustomerOrderLineItemDTO;
import org.ait.project.unittestexample.modules.customerorder.model.jpa.CustomerOrder;
import org.ait.project.unittestexample.modules.customerorder.model.jpa.CustomerOrderLineItem;
import org.ait.project.unittestexample.modules.customerorder.service.delegate.CustomerOrderLineItemServiceDelegate;
import org.ait.project.unittestexample.modules.customerorder.service.delegate.CustomerOrderServiceDelegate;
import org.ait.project.unittestexample.modules.customerorder.transform.CustomerOrderLineItemMapper;
import org.ait.project.unittestexample.modules.customerorder.transform.CustomerOrderMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class CustomerOrderServiceInternalTest extends ServiceInternalTester {

	private CustomerOrderServiceInternal service;
	
	
	private Map<TestParameter, Object> testParameterValues = new HashMap<>();
	
	private CustomerOrderFromMobileApp creationRequest;
	
	private CustomerOrder beforeUpdate;
	private CustomerOrderDTO updatingRequest;
	
	private CustomerOrderMapper orderMapper = CustomerOrderMapper.INSTANCE;
	private CustomerOrderLineItemMapper lineItemMapper = CustomerOrderLineItemMapper.INSTANCE;


	private CustomerOrderServiceDelegate customerOrderDelegateService;
	private CustomerOrderLineItemServiceDelegate customerOrderLineItemDelegateService;

	@Test
	public void testCreateNewCustomerOrder() {
		CustomerOrder createdCustomerOrder = orderMapper.toEntity(creationRequest);
		Long savedId = getEasyRandom().nextLong();
		createdCustomerOrder.setId(savedId);
		//TODO: mock return values in the repositories' reached functions
		when(customerOrderDelegateService.createNew(creationRequest)).thenReturn(createdCustomerOrder);
		System.out.println(createdCustomerOrder);
		List<CustomerOrderLineItem> submittedlineItems = 
				creationRequest.getLineItems()
				               .stream()
				               .map(lineItemMapper::toEntity)
				               .collect(Collectors.toList());
		List<CustomerOrderLineItem> savedLineItems = new ArrayList<>(submittedlineItems);
		savedLineItems.forEach(lineItem -> {
			lineItem.setId(getEasyRandom().nextLong());
			lineItem.setCustomerOrder(createdCustomerOrder);
		});
		
		when(customerOrderLineItemDelegateService.bulkSave(createdCustomerOrder.getId(), 
				                                           creationRequest.getLineItems()))
		     .thenReturn(savedLineItems);		
		
		CustomerOrderFromMobileApp result = service.createNewCustomerOrder(creationRequest);
		System.out.println(result);
		assertNotNull(result.getId());
		assertEquals(creationRequest.getGuestName(), result.getGuestName());
		assertEquals(creationRequest.getOrderType(), result.getOrderType());
		assertEquals(creationRequest.getQueueNumber(), result.getQueueNumber());
		assertTrue(creationRequest.getOrderTime().isEqual( result.getOrderTime()));
		
		List<CustomerOrderLineItemDTO> expectedLineItems = creationRequest.getLineItems();
		List<CustomerOrderLineItemDTO> actualLineItems = result.getLineItems();
		
		assertEquals(expectedLineItems.size(), actualLineItems.size());
		
		for (int i = 0; i < expectedLineItems.size(); i++) {
			CustomerOrderLineItemDTO expectedLineItem = expectedLineItems.get(i);
			CustomerOrderLineItemDTO actualLineItem = actualLineItems.get(i);
			
			assertNotNull(actualLineItem.getId());
			assertEquals(expectedLineItem.getFoodId(), actualLineItem.getFoodId());
			assertEquals(expectedLineItem.getQuantity(), actualLineItem.getQuantity());
			assertEquals(savedId, actualLineItem.getCustomerOrderId());
		}
	}

	@Test
	public void testUpdateCustomerOrder() {
		
		when(customerOrderDelegateService.fetchOneById(updatingRequest.getId())).thenReturn(beforeUpdate);
		CustomerOrder afterUpdate = orderMapper.toEntity(updatingRequest);
		when(customerOrderDelegateService.updateExisting(beforeUpdate, updatingRequest)).thenReturn(afterUpdate);
		CustomerOrder actualUpdateResult = service.updateCustomerOrder(updatingRequest);
		
		assertEquals(beforeUpdate.getId(), actualUpdateResult.getId());
		assertEquals(updatingRequest.getId(), actualUpdateResult.getId());
		
		assertNotEquals(beforeUpdate.getGuestName(), actualUpdateResult.getGuestName());
		assertNotEquals(beforeUpdate.getOrderType(), actualUpdateResult.getOrderType());
		assertNotEquals(beforeUpdate.getQueueNumber(), actualUpdateResult.getQueueNumber());
		assertFalse(beforeUpdate.getOrderTime().isEqual(actualUpdateResult.getOrderTime()));
		
		assertEquals(updatingRequest.getGuestName(), actualUpdateResult.getGuestName());
		assertEquals(updatingRequest.getOrderType(), actualUpdateResult.getOrderType());
		assertEquals(updatingRequest.getQueueNumber(), actualUpdateResult.getQueueNumber());
		assertTrue(updatingRequest.getOrderTime().isEqual(actualUpdateResult.getOrderTime()));
	}

	@Test
	public void testDeleteCustomerOrder() {
		Long customerOrderIdToDelete = (Long) testParameterValues.get(TestParameter.CUSTOMER_ORDER_ID_TO_DELETE);
		service.deleteCustomerOrder(customerOrderIdToDelete);
	}

	@Override
	public void generateRequests() {
		creationRequest = getEasyRandom().nextObject(CustomerOrderFromMobileApp.class);
		creationRequest.setId(null);
		creationRequest.getLineItems().forEach(lineItem -> lineItem.setId(null));
		
		beforeUpdate = getEasyRandom().nextObject(CustomerOrder.class);
		updatingRequest = getEasyRandom().nextObject(CustomerOrderDTO.class);
		
		Long idToUpdate = 
				(Long)getTestParameterValues().get(TestParameter.CUSTOMER_ORDER_ID_TO_UPDATE);
		beforeUpdate.setId(idToUpdate);
		updatingRequest.setId(idToUpdate);
	}

	@Override
	public void generateTestParameterValues() {
		getTestParameterValues().put(TestParameter.CUSTOMER_ORDER_ID_TO_UPDATE, 
				                     getEasyRandom().nextLong());
	}

	@Override
	public void mockServices() {
	    // delegates
		customerOrderDelegateService = Mockito.mock(CustomerOrderServiceDelegate.class);
		customerOrderLineItemDelegateService = Mockito.mock(CustomerOrderLineItemServiceDelegate.class);
		
		service = new CustomerOrderServiceInternal(customerOrderDelegateService, customerOrderLineItemDelegateService);
	}
	
	@RequiredArgsConstructor
	@Getter
	enum TestParameter {
		CUSTOMER_ORDER_ID_TO_UPDATE(Long.class),
		CUSTOMER_ORDER_ID_TO_DELETE(Long.class);
		
		private final Class<?> valueType;
	}

}

package org.ait.project.unittestexample.modules.customerorder.service.delegate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.ait.module.java.unittest.servicetester.ServiceDelegateTester;
import org.ait.project.unittestexample.modules.customerorder.model.jpa.CustomerOrderLineItem;
import org.ait.project.unittestexample.modules.customerorder.model.jpa.CustomerOrderLineItemRepository;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * unit test for {@link CustomerOrderLineItemServiceDelegate}
 * 
 * @author AIT Java Collective
 *
 */
@ExtendWith(MockitoExtension.class)
@Getter
public class CustomerOrderLineItemServiceDelegateTest 
       extends ServiceDelegateTester<CustomerOrderLineItemServiceDelegate,
                                     CustomerOrderLineItem> {

	@Mock
	private CustomerOrderLineItemRepository repository;
	
	@InjectMocks
	private CustomerOrderLineItemServiceDelegate service;
	
	private CustomerOrderLineItem entity;
	
	private Map<TestParameters, Object> testParameterValues;
	
	@Test
	public void testSetLineItemQuantity() {
		Long idToFind = (Long) getTestParameterValues().get(TestParameters.LINE_ITEM_ID);
		Long newQuantity = (Long) getTestParameterValues().get(TestParameters.NEW_QUANTITY);
		Long originalQuantity = getEntity().getQuantity();
		
		when(getRepository().findById(idToFind)).thenReturn(Optional.of(getEntity()));
		CustomerOrderLineItem afterSet = getEntity();
		afterSet.setQuantity(newQuantity);
		when(getRepository().save(any())).thenReturn(afterSet);
		
		CustomerOrderLineItem resultLineItem = service.setLineItemQuantity(idToFind,
			             	                                               newQuantity);
		
		assertNotEquals(originalQuantity, resultLineItem.getQuantity());
		assertEquals(newQuantity, resultLineItem.getQuantity());
	}
	
	@Test
	public void testIncrementQuantity() {
		
		Long idToFind = (Long) getTestParameterValues().get(TestParameters.LINE_ITEM_ID);
		when(getRepository().findById(idToFind)).thenReturn(Optional.of(getEntity()));
		
		Long originalQuantity = getEntity().getQuantity();
		
		
		CustomerOrderLineItem afterSet = getEntity();
		Long newQuantity = afterSet.getQuantity() + 1L;
		afterSet.setQuantity(newQuantity);
		when(getRepository().save(any())).thenReturn(afterSet);
		
		CustomerOrderLineItem resultLineItem = service.incrementQuantity(idToFind);
		
		assertNotEquals(originalQuantity, resultLineItem.getQuantity());
		assertEquals(afterSet.getQuantity(), resultLineItem.getQuantity());
	}
	
	@Test
	public void testDecrementQuantity() {
		
		Long idToFind = (Long) getTestParameterValues().get(TestParameters.LINE_ITEM_ID);
		when(getRepository().findById(idToFind)).thenReturn(Optional.of(getEntity()));
		
		Long originalQuantity = getEntity().getQuantity();
		
		
		CustomerOrderLineItem afterSet = getEntity();
		Long newQuantity = afterSet.getQuantity() - 1L;
		afterSet.setQuantity(newQuantity);
		when(getRepository().save(any())).thenReturn(afterSet);
		
		Optional<CustomerOrderLineItem> resultLineItemOptional = service.decrementQuantity(idToFind);
		CustomerOrderLineItem resultLineItem = resultLineItemOptional.get();
		
		assertNotEquals(originalQuantity, resultLineItem.getQuantity());
		assertEquals(afterSet.getQuantity(), resultLineItem.getQuantity());
	}
	
	@RequiredArgsConstructor
	enum TestParameters {
		LINE_ITEM_ID(Long.class),
		NEW_QUANTITY(Long.class);

		private final Class<?> valueType;
	}

	@Override
	public void generateEntity() {
		entity = getEasyRandom().nextObject(CustomerOrderLineItem.class);
		entity.setId((Long) getTestParameterValues().get(TestParameters.LINE_ITEM_ID));
		entity.setQuantity(new RandomDataGenerator().nextLong(30, 50));
		if (entity.getQuantity().equals((Long) getTestParameterValues().get(TestParameters.NEW_QUANTITY))) {
			// should not be the same as new quantity to use as parameter
			entity.setQuantity(getEasyRandom().nextLong());
		}
	}

	@Override
	public void generateTestParameterValues() {
		testParameterValues = new HashMap<>();
		for (TestParameters param : TestParameters.values()) {
			if (param.valueType.equals(Long.class)) {
				testParameterValues.put(param, new RandomDataGenerator().nextLong(1, 25));
			} else {
				testParameterValues.put(param, getEasyRandom().nextObject(param.valueType));
			}
		}
		
	}

}

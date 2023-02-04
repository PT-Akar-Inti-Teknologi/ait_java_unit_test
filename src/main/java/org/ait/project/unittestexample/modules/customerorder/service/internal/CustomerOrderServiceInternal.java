package org.ait.project.unittestexample.modules.customerorder.service.internal;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.ait.project.unittestexample.modules.customerorder.dto.CustomerOrderFromMobileApp;
import org.ait.project.unittestexample.modules.customerorder.dto.CustomerOrderDTO;
import org.ait.project.unittestexample.modules.customerorder.dto.CustomerOrderLineItemDTO;
import org.ait.project.unittestexample.modules.customerorder.model.jpa.CustomerOrder;
import org.ait.project.unittestexample.modules.customerorder.model.jpa.CustomerOrderLineItem;
import org.ait.project.unittestexample.modules.customerorder.model.jpa.CustomerOrderRepository;
import org.ait.project.unittestexample.modules.customerorder.service.delegate.CustomerOrderLineItemServiceDelegate;
import org.ait.project.unittestexample.modules.customerorder.service.delegate.CustomerOrderServiceDelegate;
import org.ait.project.unittestexample.modules.customerorder.transform.CustomerOrderLineItemMapper;
import org.ait.project.unittestexample.modules.customerorder.transform.CustomerOrderMapper;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

/**
 * 
 *  {@link CustomerOrder}
 * 
 * @author AIT Java Collective
 *
 */
@Service
@AllArgsConstructor
public class CustomerOrderServiceInternal {
	
	private final CustomerOrderServiceDelegate customerOrderServiceDelegate;
	
	private final CustomerOrderLineItemServiceDelegate customerOrderLineItemServiceDelegate;
	
	private final CustomerOrderMapper customerOrderMapper = CustomerOrderMapper.INSTANCE;
	private final CustomerOrderLineItemMapper lineItemMapper = CustomerOrderLineItemMapper.INSTANCE; 
	
	public CustomerOrderFromMobileApp createNewCustomerOrder(CustomerOrderFromMobileApp customerOrder) {
		CustomerOrder createdCustomerOrder = customerOrderServiceDelegate.createNew(customerOrder);
		
		List<CustomerOrderLineItem> savedLineItems = customerOrderLineItemServiceDelegate.bulkSave(createdCustomerOrder.getId(), 
				                                                                                   customerOrder.getLineItems());
	
		List<CustomerOrderLineItemDTO> lineItemsForResponse = savedLineItems.stream()
				                                                            .map(lineItemMapper::toDto)
				                                                            .collect(Collectors.toList());
		
		CustomerOrderFromMobileApp toReturn = customerOrderMapper.toResponse(createdCustomerOrder);
		toReturn.setLineItems(lineItemsForResponse);
		
		return toReturn;
	}
	
	public CustomerOrder updateCustomerOrder(CustomerOrderDTO source) {
		CustomerOrder toUpdate = customerOrderServiceDelegate.fetchOneById(source.getId());
		CustomerOrder customerOrder = customerOrderServiceDelegate.updateExisting(toUpdate, source);
		
		return customerOrder;
	}
	
	public void deleteCustomerOrder(Long customerOrderId) {
		customerOrderServiceDelegate.deleteExisting(customerOrderId);
	}

}

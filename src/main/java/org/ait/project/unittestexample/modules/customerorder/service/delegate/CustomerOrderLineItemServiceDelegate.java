package org.ait.project.unittestexample.modules.customerorder.service.delegate;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.ait.project.unittestexample.modules.customerorder.model.jpa.CustomerOrderLineItem;
import org.ait.project.unittestexample.modules.customerorder.model.jpa.CustomerOrderLineItemRepository;
import org.ait.project.unittestexample.modules.customerorder.transform.CustomerOrderLineItemMapper;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.comparator.NullSafeComparator;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomerOrderLineItemServiceDelegate {

	private final CustomerOrderLineItemRepository customerOrderLineItemRepository;

	public CustomerOrderLineItem incrementQuantity(Long lineItemId) {

		Optional<CustomerOrderLineItem> customerOrderLineItemOptional = customerOrderLineItemRepository.findById(lineItemId);
		if (customerOrderLineItemOptional.isPresent()) {
			CustomerOrderLineItem lineItem = customerOrderLineItemOptional.get();
			Long newQuantity = lineItem.getQuantity() + 1L;
			lineItem.setQuantity(newQuantity);
			return customerOrderLineItemRepository.save(lineItem);
		} else throw new EntityNotFoundException(); 

	}

	public Optional<CustomerOrderLineItem> decrementQuantity(Long lineItemId) {

		Optional<CustomerOrderLineItem> customerOrderLineItemOptional = customerOrderLineItemRepository.findById(lineItemId);
		if (customerOrderLineItemOptional.isPresent()) {
			CustomerOrderLineItem lineItem = customerOrderLineItemOptional.get();
			Long newQuantity = lineItem.getQuantity() - 1;
			if (NullSafeComparator.NULLS_LOW.compare(newQuantity, 0L) <= 0) {
				customerOrderLineItemRepository.deleteById(lineItemId);
				return Optional.empty();
			} else {
				lineItem.setQuantity(newQuantity);
				return Optional.of(customerOrderLineItemRepository.save(lineItem));	
			}

		} else throw new EntityNotFoundException(); 

	}

	public CustomerOrderLineItem setLineItemQuantity(Long lineItemId, Long quantity) {
		System.out.println("new quantity = " + quantity);
		if (NullSafeComparator.NULLS_LOW.compare(quantity, 0L) <= 0) {
			throw new IllegalArgumentException("must be greater than 0");
		}
		Optional<CustomerOrderLineItem> lineItemOptional = customerOrderLineItemRepository.findById(lineItemId);
		
		if (lineItemOptional.isPresent()) {
			CustomerOrderLineItem lineItem = lineItemOptional.get();
			lineItem.setQuantity(quantity);
			return customerOrderLineItemRepository.save(lineItem);
		} else {
			throw new EntityNotFoundException();
		}
	}



}

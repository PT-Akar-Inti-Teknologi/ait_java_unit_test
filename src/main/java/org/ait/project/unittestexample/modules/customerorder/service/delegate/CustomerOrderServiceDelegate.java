package org.ait.project.unittestexample.modules.customerorder.service.delegate;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.ait.project.unittestexample.modules.customerorder.dto.CustomerOrderDTO;
import org.ait.project.unittestexample.modules.customerorder.model.jpa.CustomerOrder;
import org.ait.project.unittestexample.modules.customerorder.model.jpa.CustomerOrderRepository;
import org.ait.project.unittestexample.modules.customerorder.transform.CustomerOrderMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerOrderServiceDelegate {
	
	private final CustomerOrderRepository customerOrderRepository;

	private CustomerOrderMapper customerOrderMapper = CustomerOrderMapper.INSTANCE;
	
	public CustomerOrder createNew(CustomerOrderDTO customerOrderDTO) {
		CustomerOrder customerOrder = customerOrderMapper.toEntity(customerOrderDTO);
		
		return customerOrderRepository.save(customerOrder);
	}
	
	public CustomerOrder updateExisting(CustomerOrder entityToUpdate, 
			                            CustomerOrderDTO updateDTO) {
		
		return update(entityToUpdate, updateDTO);
	}
	
	// refactoring target: simplify update
	private CustomerOrder update(CustomerOrder customerOrder, CustomerOrderDTO updateDTO) {
		customerOrder.setOrderType(updateDTO.getOrderType());
		customerOrder.setGuestName(updateDTO.getGuestName());
		customerOrder.setQueueNumber(updateDTO.getQueueNumber());
		customerOrder.setOrderTime(updateDTO.getOrderTime());
		
		return customerOrderRepository.save(customerOrder);
	}

	public void deleteExisting(Long coId) {
		customerOrderRepository.deleteById(coId);
	}

	public CustomerOrder fetchOneById(Long existingId) {
		Optional<CustomerOrder> customerOrderOptional = customerOrderRepository.findById(existingId);
		
		if (customerOrderOptional.isEmpty()) {
			throw new EntityNotFoundException(); 
		}
		
		return customerOrderOptional.get();
	}

}

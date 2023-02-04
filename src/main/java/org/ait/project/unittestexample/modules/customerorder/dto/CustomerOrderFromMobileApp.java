package org.ait.project.unittestexample.modules.customerorder.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerOrderFromMobileApp extends CustomerOrderDTO {

	private List<CustomerOrderLineItemDTO> lineItems;
	
}

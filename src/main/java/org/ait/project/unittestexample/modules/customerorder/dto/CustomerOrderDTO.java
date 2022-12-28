package org.ait.project.unittestexample.modules.customerorder.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

import org.ait.project.unittestexample.shared.constant.enums.CustomerOrderType;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * A DTO for the {@link org.ait.project.aitjhsmono.domain.CustomerOrder} entity.
 */
@Getter
@Setter
@ToString
public class CustomerOrderDTO implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 4365598362862756838L;

	private Long id;

    private String queueNumber;
    
    private String guestName;

    private CustomerOrderType orderType;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ssXXX")
    private ZonedDateTime orderTime;
    
    private List<CustomerOrderLineItemDTO> lineItems;

 
}

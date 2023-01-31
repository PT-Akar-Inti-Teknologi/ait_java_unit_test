package org.ait.project.unittestexample.modules.customerorder.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * A DTO for the {@link org.ait.project.aitjhsmono.domain.CustomerOrderLineItem} entity.
 */
@Getter
@Setter
@ToString
public class CustomerOrderLineItemDTO implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 7391901762514319875L;

	private Long id;

    @NotNull
    private Long quantity;

    private Long foodId;

    private Long customerOrderId;
    
}

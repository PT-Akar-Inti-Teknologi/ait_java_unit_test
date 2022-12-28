package org.ait.project.unittestexample.modules.customerorder.dto;

import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

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
    private Integer quantity;

    private Long foodId;

    private Long customerOrderId;
    
}

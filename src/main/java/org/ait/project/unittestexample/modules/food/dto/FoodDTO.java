package org.ait.project.unittestexample.modules.food.dto;

import javax.validation.constraints.*;

import org.ait.project.unittestexample.modules.food.model.jpa.Food;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Lob;

/**
 * A DTO for the {@link Food} entity.
 */
@Getter
@Setter
@ToString
public class FoodDTO implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 8099053234525603857L;

	private Long id;

    @NotNull
    private String name;

    @NotNull
    private BigDecimal price;


}

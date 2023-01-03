package org.ait.project.unittestexample.modules.customerorder.model.jpa;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.ait.project.unittestexample.modules.food.model.jpa.Food;
import org.ait.project.unittestexample.shared.constant.enums.CustomerOrderType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Set;

/**
 * A CustomerOrderLineItem.
 */
@Entity
@Table(name = "customer_order_line_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class CustomerOrderLineItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = "customerOrderLineItems", allowSetters = true)
    private Food food;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = "lineItems", allowSetters = true)
    private CustomerOrder customerOrder;

   
}

package org.ait.project.unittestexample.modules.food.model.jpa;

import org.ait.project.unittestexample.modules.customerorder.model.jpa.CustomerOrderLineItem;
import org.ait.project.unittestexample.shared.constant.enums.EntityStatus;
import org.ait.project.unittestexample.shared.interfaces.SoftDeletable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * A Food.
 */
@Entity
@Table(name = "food")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class Food implements Serializable, SoftDeletable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "price", precision = 21, scale = 2, nullable = false)
    private BigDecimal price;
    
    @Lob
    @Column(name = "photo", nullable = false)
    private byte[] photo;

    @Column(name = "photo_content_type", nullable = false)
    private String photoContentType;
    
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private EntityStatus status;

    @OneToMany(mappedBy = "food")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<CustomerOrderLineItem> customerOrderLineItems = new HashSet<>();

    public Food addCustomerOrderLineItem(CustomerOrderLineItem customerOrderLineItem) {
        this.customerOrderLineItems.add(customerOrderLineItem);
        customerOrderLineItem.setFood(this);
        return this;
    }

    public Food removeCustomerOrderLineItem(CustomerOrderLineItem customerOrderLineItem) {
        this.customerOrderLineItems.remove(customerOrderLineItem);
        customerOrderLineItem.setFood(null);
        return this;
    }
}

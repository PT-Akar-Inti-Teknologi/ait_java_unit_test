package org.ait.project.unittestexample.modules.customerorder.model.jpa;

import org.ait.project.unittestexample.shared.constant.enums.CustomerOrderType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A CustomerOrder.
 */
@Entity
@Table(name = "customer_order")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CustomerOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "queue_number")
    private String queueNumber;
    
    @Column(name = "guest_name")
    private String guestName;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_type")
    private CustomerOrderType orderType;

    @OneToMany(mappedBy = "customerOrder", 
    		   fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<CustomerOrderLineItem> lineItems = new HashSet<>();

    
    public CustomerOrder addLineItem(CustomerOrderLineItem customerOrderLineItem) {
        this.lineItems.add(customerOrderLineItem);
        customerOrderLineItem.setCustomerOrder(this);
        return this;
    }

    public CustomerOrder removeLineItem(CustomerOrderLineItem customerOrderLineItem) {
        this.lineItems.remove(customerOrderLineItem);
        customerOrderLineItem.setCustomerOrder(null);
        return this;
    }

}

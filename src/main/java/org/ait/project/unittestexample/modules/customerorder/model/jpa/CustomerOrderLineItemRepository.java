package org.ait.project.unittestexample.modules.customerorder.model.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the CustomerOrderLineItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerOrderLineItemRepository 
       extends JpaRepository<CustomerOrderLineItem, Long> {
}

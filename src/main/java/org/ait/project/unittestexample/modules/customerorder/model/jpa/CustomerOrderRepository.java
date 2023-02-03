package org.ait.project.unittestexample.modules.customerorder.model.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the CustomerOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerOrderRepository 
       extends JpaRepository<CustomerOrder, Long> {
}

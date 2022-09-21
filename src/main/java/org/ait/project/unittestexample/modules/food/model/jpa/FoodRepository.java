package org.ait.project.unittestexample.modules.food.model.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Food entity.
 */
@SuppressWarnings("unused")
@Repository()
public interface FoodRepository 
       extends JpaRepository<Food, Long> {
	
}

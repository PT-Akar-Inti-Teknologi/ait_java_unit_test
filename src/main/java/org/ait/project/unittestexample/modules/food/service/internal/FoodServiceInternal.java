package org.ait.project.unittestexample.modules.food.service.internal;

import java.io.IOException;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.ait.project.unittestexample.modules.food.dto.FoodDTO;
import org.ait.project.unittestexample.modules.food.dto.FoodPictureHolder;
import org.ait.project.unittestexample.modules.food.model.jpa.FoodRepository;
import org.ait.project.unittestexample.modules.food.service.delegate.FoodServiceDelegate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FoodServiceInternal {
	
	private final FoodServiceDelegate foodServiceDelegate;
	
	public FoodDTO createNewFood(FoodDTO newFood) {
		FoodDTO createdFood = foodServiceDelegate.createNewFood(newFood);
		return createdFood;
	}
	
	public void attachPicture(Long foodId, MultipartFile fileToAttach) throws IOException {
		foodServiceDelegate.attachPicture(foodId, fileToAttach);
		
		//TODO : wrap successful attach picture response
	}
	
	public FoodDTO fetchFood(Long foodId) {
		Optional<FoodDTO> foodOptional = foodServiceDelegate.fetchOne(foodId);
		
        if (foodOptional.isPresent()) {
			return foodOptional.get();
		} else {
			throw new EntityNotFoundException();
		}
	}
	
	public FoodPictureHolder fetchFoodPicture(Long foodId) {
		return foodServiceDelegate.fetchPicture(foodId);	
	}
	
	public void deleteFood(Long foodId) {
		foodServiceDelegate.deleteFood(null);
		// TODO : wrap successful deletion response
	}

}

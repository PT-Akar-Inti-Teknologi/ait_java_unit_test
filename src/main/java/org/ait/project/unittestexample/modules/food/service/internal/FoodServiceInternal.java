package org.ait.project.unittestexample.modules.food.service.internal;

import java.io.IOException;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.ait.project.unittestexample.modules.food.dto.FoodDTO;
import org.ait.project.unittestexample.modules.food.dto.FoodPictureHolder;
import org.ait.project.unittestexample.modules.food.model.jpa.Food;
import org.ait.project.unittestexample.modules.food.model.jpa.FoodRepository;
import org.ait.project.unittestexample.modules.food.service.delegate.FoodServiceDelegate;
import org.ait.project.unittestexample.modules.food.transform.FoodMapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FoodServiceInternal {
	
	private final FoodServiceDelegate foodServiceDelegate;
	
	private FoodMapper mapper = FoodMapper.INSTANCE;
	
	public FoodDTO createNewFood(FoodDTO newFood) {
		Food createdFood = foodServiceDelegate.createNewFood(newFood);
		return mapper.toDto(createdFood);
	}
	
	public FoodPictureHolder attachPicture(Long foodId, MultipartFile fileToAttach) throws IOException {
		Food food = foodServiceDelegate.fetchOne(foodId);
		
		Food result = foodServiceDelegate.attachPicture(food, fileToAttach);
		
		FoodPictureHolder holder = mapper.toPictureHolder(result);
		
		return holder;
	}
	
	public FoodDTO fetchFood(Long foodId) {
		Food food = foodServiceDelegate.fetchOne(foodId);
		
        return mapper.toDto(food);
	}
	
	public FoodPictureHolder fetchFoodPicture(Long foodId) {
		Food food = foodServiceDelegate.fetchOne(foodId);
		
		FoodPictureHolder picture = mapper.toPictureHolder(food);
		
		return picture;
	}
	
	public void deleteFood(Long foodId) {
		foodServiceDelegate.deleteFood(foodId);
	}

}

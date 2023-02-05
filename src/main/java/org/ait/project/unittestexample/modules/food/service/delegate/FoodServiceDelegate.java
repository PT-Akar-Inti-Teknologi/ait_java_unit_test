package org.ait.project.unittestexample.modules.food.service.delegate;

import java.io.IOException;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.ait.project.unittestexample.modules.food.dto.FoodDTO;
import org.ait.project.unittestexample.modules.food.dto.FoodPictureHolder;
import org.ait.project.unittestexample.modules.food.model.jpa.Food;
import org.ait.project.unittestexample.modules.food.model.jpa.FoodRepository;
import org.ait.project.unittestexample.modules.food.transform.FoodMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FoodServiceDelegate {
	
	@Autowired
	private FoodRepository foodRepository;
	
	private FoodMapper foodMapper = FoodMapper.INSTANCE;
	
	public Food createNewFood(FoodDTO foodDto) {
		Food food = foodMapper.toEntity(foodDto);
		
		return foodRepository.save(food);
		
	}
	
	public Food attachPicture(Food food, MultipartFile file) throws IOException {
		final byte[] pictureToAttach = file.getBytes();
		food.setPhoto(pictureToAttach);
		food.setPhotoContentType(file.getContentType());
		
		return foodRepository.save(food);
	}
	
	public FoodPictureHolder fetchPicture(Long foodId) {
		Food food = fetchOne(foodId);
		FoodPictureHolder holder = new FoodPictureHolder();
		holder.setPictureBytes(new ByteArrayResource(food.getPhoto()));
		holder.setPictureContentType(food.getPhotoContentType());
		return holder;
	}
	
	
	public Food fetchOne(Long id) {
		Optional<Food> fetchedFood = foodRepository.findById(id);
		
		if (fetchedFood.isEmpty()) {
			throw new EntityNotFoundException();
		}
		
		return fetchedFood.get();
	}
	
	public void deleteFood(Long id) {
		foodRepository.deleteById(id);
	}

}

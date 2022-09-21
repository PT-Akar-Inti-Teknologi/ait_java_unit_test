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

import lombok.RequiredArgsConstructor;

@Service
public class FoodServiceDelegate {
	
	@Autowired
	private FoodRepository foodRepository;
	
	private FoodMapper foodMapper = FoodMapper.INSTANCE;
	
	public FoodDTO createNewFood(FoodDTO foodDto) {
		Food food = foodMapper.toEntity(foodDto);
		
		return foodMapper.toDto(foodRepository.save(food));
		
	}
	
	public void attachPicture(Long foodId, MultipartFile file) throws IOException {
		final byte[] pictureToAttach = file.getBytes();
		
		foodRepository.findById(foodId).ifPresent(food -> {
			food.setPhoto(pictureToAttach);
			food.setPhotoContentType(file.getContentType());
			foodRepository.save(food);
		});
	}
	
	public FoodPictureHolder fetchPicture(Long foodId) {
		Optional<Food> foodOptional = foodRepository.findById(foodId);
		
		if (foodOptional.isPresent()) {
			Food food = foodOptional.get();
			FoodPictureHolder picture = new FoodPictureHolder();
			picture.setPictureBytes(new ByteArrayResource(food.getPhoto(), food.getName()));
			picture.setPictureContentType(food.getPhotoContentType());
			return picture;
		} else {
			throw new EntityNotFoundException();
		}
		
	}
	
	
	public Optional<FoodDTO> fetchOne(Long id) {
		Optional<Food> fetchedFood = foodRepository.findById(id);
		
		return fetchedFood.map(foodMapper::toDto);
	}
	
	public void deleteFood(Long id) {
		foodRepository.deleteById(id);
	}

}

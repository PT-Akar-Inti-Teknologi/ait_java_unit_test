package org.ait.project.unittestexample.modules.food.service.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.ait.project.unittestexample.modules.food.dto.FoodDTO;
import org.ait.project.unittestexample.modules.food.dto.FoodPictureHolder;
import org.ait.project.unittestexample.modules.food.model.jpa.Food;
import org.ait.project.unittestexample.modules.food.model.jpa.FoodRepository;
import org.ait.project.unittestexample.modules.food.service.delegate.FoodServiceDelegate;
import org.ait.project.unittestexample.modules.food.transform.FoodMapper;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;


@ExtendWith(MockitoExtension.class)
public class FoodServiceDelegateUnitTest {

	private EasyRandom easyRandom = new EasyRandom();

	// module mocks ======================
	@Mock
	private FoodRepository foodRepository;
	
	@InjectMocks
	private FoodServiceDelegate foodServiceDelegate;
	
	private FoodMapper foodMapper = FoodMapper.INSTANCE;

	// Control Variables ============================
	private FoodDTO newFood;
	
	private Food existingFood;

	private MultipartFile testMultipartFile;
	
	@BeforeEach
	public void setTestVariables() {
		newFood = newFood();
		existingFood = easyRandom.nextObject(Food.class);
		byte[] bytesToUse = new byte[1024];
		easyRandom.nextBytes(bytesToUse);
		MockMultipartFile multipartFile = new MockMultipartFile(easyRandom.nextObject(String.class), bytesToUse);
		testMultipartFile = multipartFile;
	}
	private FoodDTO newFood() {
		FoodDTO food = easyRandom.nextObject(FoodDTO.class);
		food.setId(null);
		return food;
	}
	
	
	// ==============================================
	
	@Test
	public void testCreateNewFood() {
		// mock events ==================
		Food savedFood = foodMapper.toEntity(newFood);
		savedFood.setId(easyRandom.nextLong());
		when(foodRepository.save(foodMapper.toEntity(newFood))).thenReturn(savedFood);
		// ==============================
		
		FoodDTO createdFood = foodServiceDelegate.createNewFood(newFood);
		
		assertNotNull(createdFood.getId(), "saved entity should have id");
		assertEquals(savedFood.getId(), createdFood.getId(), "saved food should have same id as created food");
		assertEquals(createdFood.getName(), newFood.getName());
		assertEquals(createdFood.getPrice(), newFood.getPrice());
		
	}

	@Test
	public void testAttachPicture() throws IOException {
		newFood.setId(easyRandom.nextLong());
		foodServiceDelegate.attachPicture(newFood.getId(), testMultipartFile);
		
	}

	@Test
	public void testFetchFood() {
		when(foodRepository.findById(existingFood.getId())).thenReturn(Optional.of(existingFood));
		
		FoodDTO fetchedFood = foodServiceDelegate.fetchOne(existingFood.getId()).get();
		
		assertEquals(existingFood.getId(), fetchedFood.getId());
		assertEquals(existingFood.getPrice(), fetchedFood.getPrice());
		assertEquals(existingFood.getName(), fetchedFood.getName());
	}

	@Test
	public void testFetchFoodPicture() throws IOException {
		when(foodRepository.findById(existingFood.getId())).thenReturn(Optional.of(existingFood));
		
		FoodPictureHolder fetchedPicture = foodServiceDelegate.fetchPicture(existingFood.getId());
		
		assertEquals(existingFood.getPhoto().length, fetchedPicture.getPictureBytes().contentLength());
		assertEquals(existingFood.getPhoto(), fetchedPicture.getPictureBytes().getByteArray());
		assertEquals(existingFood.getPhotoContentType(), fetchedPicture.getPictureContentType());
		
	}

	@Test
	public void testDeleteFood() {
		newFood.setId(easyRandom.nextLong());
		foodServiceDelegate.deleteFood(newFood.getId());
		
		when(foodRepository.findById(newFood.getId())).thenThrow(EntityNotFoundException.class);
		
		assertThrows(EntityNotFoundException.class, () -> {
			foodServiceDelegate.fetchOne(newFood.getId());
		});
	}
	
	// Cleanup after all tests ==========

	@AfterAll
	public static void cleanup() {
		
	}
	
	// ==================================

}

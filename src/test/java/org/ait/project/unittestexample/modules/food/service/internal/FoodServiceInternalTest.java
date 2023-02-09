package org.ait.project.unittestexample.modules.food.service.internal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityNotFoundException;

import org.ait.module.java.unittest.servicetester.ServiceInternalTester;
import org.ait.project.unittestexample.modules.food.dto.FoodDTO;
import org.ait.project.unittestexample.modules.food.dto.FoodPictureHolder;
import org.ait.project.unittestexample.modules.food.model.jpa.Food;
import org.ait.project.unittestexample.modules.food.service.delegate.FoodServiceDelegate;
import org.ait.project.unittestexample.modules.food.service.internal.FoodServiceDelegateUnitTest.TestVariable;
import org.ait.project.unittestexample.modules.food.transform.FoodMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class FoodServiceInternalTest extends ServiceInternalTester {

	private FoodServiceDelegate foodServiceDelegate;
	private FoodServiceInternal foodServiceInternal;
	
	private FoodDTO creationRequest;
	private Food existingEntity;
	private MultipartFile pictureAttachRequest;
	
	private FoodMapper mapper = FoodMapper.INSTANCE;
	
	
	@Test
	public void testCreateNewFood() {
		Food createdFood = mapper.toEntity(creationRequest);
		Long createdId = getTestParameterValues().get(TestParameter.FOOD_ID_EXISTENT);
		createdFood.setId(createdId);
		
		when(foodServiceDelegate.createNewFood(creationRequest)).thenReturn(createdFood);
		FoodDTO actual = foodServiceInternal.createNewFood(creationRequest);
		
		assertNotNull(actual.getId());
		assertEquals(creationRequest.getName(), actual.getName());
		assertEquals(creationRequest.getPrice(), actual.getPrice());
	}

	@Test
	public void testAttachPicture() throws IOException {
		Long existId = getTestParameterValues().get(TestParameter.FOOD_ID_EXISTENT);
		existingEntity.setId(existId);
		when(foodServiceDelegate.fetchOne(existId)).thenReturn(existingEntity);
		
		Food afterAttach = new Food();
		afterAttach.setId(existingEntity.getId());
		afterAttach.setName(existingEntity.getName());
		afterAttach.setPhoto(pictureAttachRequest.getBytes());
		afterAttach.setPhotoContentType(pictureAttachRequest.getContentType());
		afterAttach.setPrice(existingEntity.getPrice());
		afterAttach.setStatus(existingEntity.getStatus());
		when(foodServiceDelegate.attachPicture(existingEntity, pictureAttachRequest)).thenReturn(afterAttach);
		FoodPictureHolder actual = foodServiceInternal.attachPicture(existId, pictureAttachRequest);
		
		assertEquals(existingEntity.getId(), actual.getFoodId());
		assertEquals(afterAttach.getPhoto(), actual.getPictureBytes().getByteArray());
		assertTrue(actual.getPictureBytes().getDescription().contains(existingEntity.getName()));
		assertEquals(afterAttach.getPhotoContentType(), actual.getPictureContentType());
	}

	@Test
	public void testFetchFood() {
		// positive
		Long foodIdExists = getTestParameterValues().get(TestVariable.FOOD_ID_SAVED);
		existingEntity.setId(foodIdExists);
		when(foodServiceDelegate.fetchOne(foodIdExists)).thenReturn(existingEntity);
		FoodDTO actualPositive = foodServiceInternal.fetchFood(foodIdExists);
		
		assertEquals(existingEntity.getId(), actualPositive.getId());
		assertEquals(existingEntity.getName(), actualPositive.getName());
		assertEquals(existingEntity.getPrice(), actualPositive.getPrice());
		
		//negative
		Long foodIdNotExists = getTestParameterValues().get(TestVariable.FOOD_ID_NONEXISTENT);
		when(foodServiceDelegate.fetchOne(foodIdNotExists)).thenThrow(EntityNotFoundException.class);
		assertThrows(EntityNotFoundException.class, ()-> foodServiceInternal.fetchFood(foodIdNotExists));
	}

	@Test
	public void testFetchFoodPicture() {
		//fail("Not yet implemented");
	}

	@Test
	public void testDeleteFood() {
		Long idToDelete = getTestParameterValues().get(TestParameter.FOOD_ID_TO_DELETE);
		foodServiceInternal.deleteFood(idToDelete);
		
		verify(foodServiceDelegate).deleteFood(idToDelete);
	}

	@Override
	public void generateRequests() {
		creationRequest = getEasyRandom().nextObject(FoodDTO.class);
		creationRequest.setId(null);
		
		byte[] pictureByteArray = new byte[1024];
		getEasyRandom().nextBytes(pictureByteArray);
		pictureAttachRequest = new MockMultipartFile("testpicture.jpg", pictureByteArray);
		
		existingEntity = getEasyRandom().nextObject(Food.class);
	}

	@Override
	public void generateTestParameterValues() {
		for (TestParameter param : TestParameter.values()) {
			getTestParameterValues().put(param, getEasyRandom().nextLong());
		}
	}
	
	@Override
	public void mockServices() {
		foodServiceDelegate = Mockito.mock(FoodServiceDelegate.class);
		foodServiceInternal = new FoodServiceInternal(foodServiceDelegate);
	}

	private Map<TestParameter, Long> testParameterValues = new HashMap<>();
	
	@RequiredArgsConstructor
	@Getter
	enum TestParameter {
		FOOD_ID_NONEXISTENT(Long.class),
		FOOD_ID_EXISTENT(Long.class),
		FOOD_ID_TO_DELETE(Long.class),;
		
		private final Class<?> valueType;
	}

}

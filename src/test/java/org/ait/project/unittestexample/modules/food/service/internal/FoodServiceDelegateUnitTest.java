package org.ait.project.unittestexample.modules.food.service.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.ait.module.java.unittest.servicetester.ServiceDelegateTester;
import org.ait.project.unittestexample.modules.food.dto.FoodDTO;
import org.ait.project.unittestexample.modules.food.dto.FoodPictureHolder;
import org.ait.project.unittestexample.modules.food.model.jpa.Food;
import org.ait.project.unittestexample.modules.food.model.jpa.FoodRepository;
import org.ait.project.unittestexample.modules.food.service.delegate.FoodServiceDelegate;
import org.ait.project.unittestexample.modules.food.transform.FoodMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@ExtendWith(MockitoExtension.class)
@Getter
public class FoodServiceDelegateUnitTest 
       extends ServiceDelegateTester<FoodServiceDelegate, Food>{

	// module mocks ======================
	@Mock
	private FoodRepository repository;
	
	@InjectMocks
	private FoodServiceDelegate service;
	
	private FoodMapper mapper = FoodMapper.INSTANCE;
	// Control Variables ============================
	private FoodDTO dto;
	
	private Food entity;

	private MultipartFile testMultipartFile;
	
	private Map<TestVariable, Long> testParameterValues = new HashMap<>();
	
	
	@BeforeEach
	@Override
	public void beforeEachTest() {
		super.beforeEachTest();
		byte[] bytesToUse = new byte[1024];
		getEasyRandom().nextObject(Byte.class);
		MockMultipartFile multipartFile = new MockMultipartFile(getEasyRandom().nextObject(String.class), bytesToUse);
		testMultipartFile = multipartFile;
	}
	
	// ==============================================
	
	@Test
	public void testCreateNewFood() {
		// mock events ==================
		dto.setId(null);
		Food entityToSave = mapper.toEntity(dto);
		
		Food entitySaved = mapper.toEntity(dto);
		Long givenId = getTestParameterValues().get(TestVariable.FOOD_ID_SAVED);
		entitySaved.setId(givenId);
		
		when(getRepository().save(entityToSave)).thenReturn(entitySaved);
		//==============================
		Food createdFood = service.createNewFood(dto);
		
		assertNotNull(createdFood.getId(), "saved entity should have id");
		assertEquals(createdFood.getName(), dto.getName());
		assertEquals(createdFood.getPrice(), dto.getPrice());
		
	}

	@Test
	public void testAttachPicture() throws IOException {
		Long idToAttachPicture = getTestParameterValues().get(TestVariable.FOOD_ID_TO_ATTACH_PHOTO);
		entity.setId(idToAttachPicture);
		entity.setPhotoContentType(null);
		entity.setPhoto(null);
		
		Food savedEntity = new Food();
		savedEntity.setName(entity.getName());
		savedEntity.setPrice(entity.getPrice());
		savedEntity.setStatus(entity.getStatus());
		byte[] bytesContainer = new byte[1024];
		getEasyRandom().nextBytes(bytesContainer);
		savedEntity.setPhoto(bytesContainer);
		savedEntity.setPhotoContentType(getEasyRandom().nextObject(String.class));
		when(getRepository().save(any())).thenReturn(savedEntity); 
		Food result = service.attachPicture(entity, testMultipartFile);
		
		assertEquals(entity.getName(), result.getName());
		assertEquals(entity.getPrice(), result.getPrice());
		assertEquals(entity.getStatus(), result.getStatus());
		assertNotEquals(entity.getPhoto(), result.getPhoto());
		assertNotEquals(entity.getPhotoContentType(), result.getPhotoContentType());
		
		assertNotNull(result.getPhoto());
		assertNotNull(result.getPhotoContentType());
		
	}
	
	@Test
	public void testFetchFood() {
		Long nonexistentId = getTestParameterValues().get(TestVariable.FOOD_ID_NONEXISTENT); 
		when(getRepository().findById(nonexistentId)).thenReturn(Optional.empty());
		assertThrows(EntityNotFoundException.class, () -> service.fetchOne(nonexistentId));
		
		when(getRepository().findById(entity.getId())).thenReturn(Optional.of(entity));
		
		Food fetchedFood = service.fetchOne(entity.getId());
		
		assertEquals(entity.getId(), fetchedFood.getId());
		assertEquals(entity.getPrice(), fetchedFood.getPrice());
		assertEquals(entity.getName(), fetchedFood.getName());
	}

	@Test
	public void testFetchFoodPicture() throws IOException {
		when(getRepository().findById(entity.getId())).thenReturn(Optional.of(entity));
		
		FoodPictureHolder fetchedPicture = service.fetchPicture(entity.getId());
		
		assertEquals(entity.getPhoto().length, fetchedPicture.getPictureBytes().contentLength());
		assertEquals(entity.getPhoto(), fetchedPicture.getPictureBytes().getByteArray());
		assertEquals(entity.getPhotoContentType(), fetchedPicture.getPictureContentType());
		
	}

	@Test
	public void testDeleteFood() {
		Long idToDelete = getTestParameterValues().get(TestVariable.FOOD_ID_TO_DELETE);
		entity.setId(idToDelete);
		
		service.deleteFood(entity.getId());
		
		verify(getRepository()).deleteById(idToDelete);
		verifyNoMoreInteractions(getRepository());

		
		when(getRepository().findById(entity.getId())).thenThrow(EntityNotFoundException.class);
		
		assertThrows(EntityNotFoundException.class, () -> {
			service.fetchOne(entity.getId());
		});
	}
	
	
	@Override
	public void generateEntity() {
		entity = getEasyRandom().nextObject(Food.class);
		dto = getEasyRandom().nextObject(FoodDTO.class);
	}
	
	@Override
	public void generateTestParameterValues() {
		for (TestVariable testvariable : TestVariable.values()) {	
			testParameterValues.put(testvariable, getEasyRandom().nextLong());
		}
	}
	
	@RequiredArgsConstructor
	@Getter
	enum TestVariable {
		FOOD_ID_SAVED(Long.class),
		FOOD_ID_TO_ATTACH_PHOTO(Long.class),
		FOOD_ID_TO_DELETE(Long.class),
		FOOD_ID_NONEXISTENT(Long.class);
		
		private final Class<?> valueType;
	}

}

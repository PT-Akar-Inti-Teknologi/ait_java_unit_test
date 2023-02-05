package org.ait.project.unittestexample.shared;

import static org.mockito.Mockito.when;

import org.ait.project.unittestexample.modules.food.dto.FoodDTO;
import org.ait.project.unittestexample.modules.food.model.jpa.Food;
import org.ait.project.unittestexample.modules.food.model.jpa.FoodRepository;
import org.ait.project.unittestexample.modules.food.service.delegate.FoodServiceDelegate;
import org.ait.project.unittestexample.modules.food.transform.FoodMapper;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class) // 1 : untuk enable mock
public class TestBoilerPlate {

	private EasyRandom easyRandom = new EasyRandom(); // 2 : untuk membuat random object/collection

	// 3 : komponen-komponen yang terlibat pada modul yang akan ditest
	@Mock // komponen yang akan di-mock, yang akan memberikan response yang disiapkan
	private FoodRepository foodRepository;

	@InjectMocks // service yang akan kali ini akan ditest, akan menggunakan komponen yang di-mock 
	private FoodServiceDelegate foodServiceDelegate;

	// special case : mapper dibuat singleton, agar dapat dijalankan di unit test
	private FoodMapper foodMapper = FoodMapper.INSTANCE;  
	// =================================================================

	/**
	 * 
	 * penamaan nama test dengan format : {nama_method}_{kondisi_akhir}
	 * contoh :
	 * "public void createNewFood_success"
	 * "public void createNewFood_failed_mandatory_fields_throw_exception"
	 */
	@Test
	public void createNewFood_success() {
		// mock events ==================
		// 1 - prepare object yang akan diterima service ini
		FoodDTO newFood = easyRandom.nextObject(FoodDTO.class); 
		Food savedFood = foodMapper.toEntity(newFood);
		// 2 - prepare event yang akan terjadi pada komponen yang di-mock
		when(foodRepository.save(foodMapper.toEntity(newFood))).thenReturn(savedFood);
		// ==============================

		// method yang ditest saat ini
		Food createdFood = foodServiceDelegate.createNewFood(newFood);

		// uji hasil return dari function
		
		Assertions.assertNotNull(createdFood.getId(), "saved entity should have id");
		Assertions.assertEquals(savedFood.getId(), createdFood.getId(), "saved food should have same id as created food");
		// assertEquals({object_balikan_asli}, {object_balikan_yang_dinanti});
		Assertions.assertEquals(createdFood.getName(), newFood.getName());
		Assertions.assertEquals(createdFood.getPrice(), newFood.getPrice()); 
	}

}

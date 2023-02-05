package org.ait.project.unittestexample.modules.food.transform;

import static org.junit.jupiter.api.Assertions.*;

import org.ait.module.java.unittest.mappertester.EntityMapperTester;
import org.ait.project.unittestexample.modules.food.dto.FoodDTO;
import org.ait.project.unittestexample.modules.food.model.jpa.Food;
import org.junit.jupiter.api.Test;

import lombok.Getter;

@Getter
public class FoodMapperTest 
       extends EntityMapperTester<FoodDTO, Food>{

	private Food entity;
	private FoodDTO dto;
	
	private FoodMapper mapper = FoodMapper.INSTANCE;
	
	@Test
	public void testMapDtoToEntity() {
		Food actual = mapper.toEntity(dto);
		
		assertEquals(dto.getId(), actual.getId());
		assertEquals(dto.getPrice(), actual.getPrice());
		assertEquals(dto.getName(), actual.getName());
	}

	@Test
	public void testMapEntityToDto() {
		
        FoodDTO actual = mapper.toDto(entity);
		
		assertEquals(entity.getId(), actual.getId());
		assertEquals(entity.getPrice(), actual.getPrice());
		assertEquals(entity.getName(), actual.getName());
	}

	@Override
	public void generateEntity() {
		entity = getEasyRandom().nextObject(Food.class);
	}

	@Override
	public void generateDto() {
		dto = getEasyRandom().nextObject(FoodDTO.class);
	}

}

package org.ait.project.unittestexample.modules.food.transform;


import org.ait.project.unittestexample.modules.food.dto.FoodDTO;
import org.ait.project.unittestexample.modules.food.model.jpa.Food;
import org.ait.project.unittestexample.shared.utils.transform.EntityMapper;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * Mapper for the entity {@link Food} and its DTO {@link FoodDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FoodMapper extends EntityMapper<FoodDTO, Food> {

	FoodMapper INSTANCE = Mappers.getMapper(FoodMapper.class);

    @Mapping(target = "customerOrderLineItems", ignore = true)
    @Mapping(target = "removeCustomerOrderLineItem", ignore = true)
    Food toEntity(FoodDTO foodDTO);

    default Food fromId(Long id) {
        if (id == null) {
            return null;
        }
        Food food = new Food();
        food.setId(id);
        return food;
    }
}

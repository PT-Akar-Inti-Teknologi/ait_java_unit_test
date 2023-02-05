package org.ait.project.unittestexample.modules.food.transform;


import org.ait.project.unittestexample.modules.food.dto.FoodDTO;
import org.ait.project.unittestexample.modules.food.dto.FoodPictureHolder;
import org.ait.project.unittestexample.modules.food.model.jpa.Food;
import org.ait.project.unittestexample.shared.utils.transform.EntityMapper;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.core.io.ByteArrayResource;

/**
 * Mapper for the entity {@link Food} and its DTO {@link FoodDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FoodMapper extends EntityMapper<FoodDTO, Food> {

	FoodMapper INSTANCE = Mappers.getMapper(FoodMapper.class);

	//photo, photoContentType, status
	@Mapping(target = "photoContentType", ignore = true)
	@Mapping(target = "photo", ignore = true)
	@Mapping(target = "status", ignore = true)
    Food toEntity(FoodDTO foodDTO);

	default FoodPictureHolder toPictureHolder(Food source) {
		FoodPictureHolder holder = new FoodPictureHolder();
		holder.setFoodId(source.getId());
		holder.setPictureBytes(new ByteArrayResource(source.getPhoto(), source.getName()));
		holder.setPictureContentType(source.getPhotoContentType());
		
		return holder;
	}

}

package org.ait.project.unittestexample.modules.food.dto;

import org.springframework.core.io.ByteArrayResource;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FoodPictureHolder {

	private ByteArrayResource pictureBytes;
	
	private String pictureContentType;
}

package org.ait.project.unittestexample.modules.food.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;

import org.ait.project.unittestexample.modules.food.transform.FoodMapper;
import org.ait.project.unittestexample.shared.JsonTestInterface;
import org.ait.project.unittestexample.shared.JsonTester;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import com.fasterxml.jackson.databind.DatabindException;

public class FoodDTOTest extends JsonTester<FoodDTO> {

	private FoodMapper mapper = FoodMapper.INSTANCE;
	
	@BeforeEach
	@Override
	protected void setupTest() throws IOException {
		// the JSON is given from the agreed contract between BE and FE
		setJsonTestType(JsonTestType.GIVEN);
		// object to test should be set manually here
		setObjectToTest(getObjectToUse());
		/// declare the Java Type to test for JSON
		setTypeToTest(FoodDTO.class);
		super.setupTest();
	}

	

	@Order(1)
	@Test
	public void serializationTest() throws JSONException, IOException {
		// serialize the object to test, into JSON
		String actual = om.writeValueAsString(getObjectToTest());
		
		// compare the expected JSON provided, against the JSON written by the ObjectMapper 
		JSONAssert.assertEquals(new String(getSourceJson().readAllBytes(), Charset.defaultCharset()), actual, JSONCompareMode.STRICT);
	}
	
	@Order(2)
	@Test
	public void deserializationTest() throws IOException, DatabindException {
		
		// deserialize/read the JSON into a POJO
		FoodDTO actual = om.readValue(getSourceJson(), getTypeToTest());
		
		// get the expected object 
		FoodDTO expected = getObjectToTest();
		
		assertEquals(expected.getId(), actual.getId());
		assertEquals(expected.getName(), actual.getName());
		assertEquals(expected.getPrice().compareTo(actual.getPrice()), 0);
	}
	
	private FoodDTO getObjectToUse() {
		FoodDTO objectToUse = new FoodDTO();
		objectToUse.setId(4L);
		objectToUse.setName("I Fu Mie");
		objectToUse.setPrice(new BigDecimal(55000));
		return objectToUse;
	}
	

}

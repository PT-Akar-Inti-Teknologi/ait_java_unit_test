package org.ait.project.unittestexample.modules.customerorder.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.charset.Charset;

import org.ait.module.java.unittest.JsonTester;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import com.fasterxml.jackson.databind.DatabindException;

public class CustomerOrderDTOTest 
       extends JsonTester<CustomerOrderDTO> {

	@BeforeEach
	@Override
	protected void setupTest() throws IOException {
		// the JSON is Generated, 
		// as there are no agreed contract 
		// between BE and FE yet
		setJsonTestType(JsonTestType.GENERATED);
		
		/// declare the Java Type to test for JSON
		setTypeToTest(CustomerOrderDTO.class);
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
		CustomerOrderDTO actual = om.readValue(getSourceJson(), getTypeToTest());
		
		// get the expected object 
		CustomerOrderDTO expected = getObjectToTest();
		
		assertEquals(expected.getId(), actual.getId());
		assertEquals(expected.getOrderTime().toEpochSecond(), 
				     actual.getOrderTime().toEpochSecond());
		assertEquals(expected.getOrderType(), actual.getOrderType());
		assertEquals(expected.getGuestName(), actual.getGuestName());
		assertEquals(expected.getQueueNumber(), actual.getQueueNumber());
		
		
	}
}

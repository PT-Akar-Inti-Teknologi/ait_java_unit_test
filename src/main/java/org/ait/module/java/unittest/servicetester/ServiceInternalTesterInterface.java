package org.ait.module.java.unittest.servicetester;

import java.util.Map;

public interface ServiceInternalTesterInterface {
	
	public void generateRequests();
	
	public void generateTestParameterValues();
	
	public Map<?, ?> getTestParameterValues();
	
	public void mockServices();
	
}

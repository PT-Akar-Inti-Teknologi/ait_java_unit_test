package org.ait.project.unittestexample.shared.interfaces;

import org.ait.project.unittestexample.shared.constant.enums.EntityStatus;

public interface SoftDeletable {
	
	public void setStatus(EntityStatus entityStatus);
	
	public EntityStatus getStatus();

}

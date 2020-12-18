package com.amazon.gdpr.processor;

import com.amazon.gdpr.model.gdpr.output.RunModuleMgmt;

/****************************************************************************************
 * This processor processes the Module details
 ****************************************************************************************/
public class ModuleMgmtProcessor {
		
	/**
	 * After the completion of each module the status of the module will be updated
	 * @param runModuleMgmt The details of the Module are passed on as input
	 * @return Boolean The status of the RunModuleMgmt table update
	 */
	public Boolean updateModuleStatus(RunModuleMgmt runModuleMgmt) {
		//upload Module Details 
		return true;
	}
}

package com.amazon.gdpr.service;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.amazon.gdpr.model.gdpr.output.RunModuleMgmt;
import com.amazon.gdpr.model.gdpr.output.RunSummaryMgmt;
import com.amazon.gdpr.util.GlobalConstants;

/****************************************************************************************
 * This Service performs the Depersonalization activity on the Heroku Data 
 * This will be invoked by the GDPRController 
****************************************************************************************/
@Service
public class DepersonalizationService {
	public static String MODULE_DEPERSONALIZATION = GlobalConstants.MODULE_DEPERSONALIZATION;
	public static String STATUS_SUCCESS = GlobalConstants.STATUS_SUCCESS;	
	
	/**
	 * Based on the Summary detail captured, depersonalization is performed
	 * @param mapRunSummaryMgmt - The overall Summary information on the depersonalization to be performed 
	 * @param runId The current runid to track the depersonalization
	 * @return The status of the depersonalization
	 */
	public String depersonalize(Map<String, RunSummaryMgmt> mapRunSummaryMgmt, int runId) {
		Date  startDepersonalizeServiceDate = new Date();
		// Loop through each of the rows in RunSummaryMgmt 
		// Execute the respective update queries to depersonalize
		// Update Tagged Table
		
		Date endDepersonalizeServiceDate = new Date();		
		RunModuleMgmt runModuleMgmt = new RunModuleMgmt(runId, MODULE_DEPERSONALIZATION, STATUS_SUCCESS, 
														startDepersonalizeServiceDate, endDepersonalizeServiceDate, "");
		return "";
	}
}
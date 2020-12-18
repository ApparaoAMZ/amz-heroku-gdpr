package com.amazon.gdpr.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.amazon.gdpr.model.gdpr.output.RunModuleMgmt;
import com.amazon.gdpr.model.gdpr.output.RunSummaryMgmt;
import com.amazon.gdpr.processor.SummaryDataProcessor;
import com.amazon.gdpr.util.GlobalConstants;

/****************************************************************************************
 * This Service performs the Depersonalization activity on the Heroku Backup Data 
 * This will be invoked by the GDPRController
 ****************************************************************************************/
@Service
public class BackupService {
	
	public static String MODULE_DATABACKUP = GlobalConstants.MODULE_DATABACKUP;
	public static String STATUS_SUCCESS = GlobalConstants.STATUS_SUCCESS;	
		
	/**
	 * The main method which initiates the Backup Service
	 * @param runId The current run's reference
	 * @param mapRunSummaryMgmt The overall summary of the Depersonalization is managed in this Table
	 * @return Updated Summary information with the backup detail loaded 
	 */
	public Map<String, RunSummaryMgmt> backupService(long runId, Map<String, RunSummaryMgmt> mapRunSummaryMgmt) {
		Date  startBkpupServiceDate = new Date();
		Map<String, Set<String>> mapParentIds = fetchParentIds();
		Map<String, RunSummaryMgmt> mapRunSummaryMgmtUpdated = performBackup(mapParentIds, mapRunSummaryMgmt);
		SummaryDataProcessor smryDataProcessor = new SummaryDataProcessor();
		//mapRunSummaryMgmtUpdated = smryDataProcessor.processSummaryData(mapRunSummaryMgmtUpdated);
		Date endBkpupServiceDate = new Date();
		RunModuleMgmt runModuleMgmt = new RunModuleMgmt(runId, MODULE_DATABACKUP, STATUS_SUCCESS, 
														startBkpupServiceDate, endBkpupServiceDate, "");
		return mapRunSummaryMgmtUpdated;
	}
	
	/**
	 * The list of all the parent Ids for each of the table is loaded in a map
	 * @return Returns the parentIds mapped to their respective tablename
	 */
	public Map<String, Set<String>> fetchParentIds() {
		Map<String, Set<String>> mapParentIds = new HashMap<String, Set<String>>();
		//Fetch Impact Table Id's parent table and Parent table Key and fetch respective table with condition 
		//load it in a Set
		//Fetch the parent ids from respective table and load it in the map
		return mapParentIds;
	}
	
	/**
	 * Backup activity is performed and the details are loaded into the RunSummaryMgmt table
	 * @param mapParentIds    The parent ids are passed as conditions in the table
	 * @param mapRunSummaryMgmt The current summary information is passed on
	 * @return The updated summary information with the back up details
	 */
	public Map<String, RunSummaryMgmt> performBackup(Map<String, Set<String>> mapParentIds, 
													  Map<String, RunSummaryMgmt> mapRunSummaryMgmt) {
		Map<String, RunSummaryMgmt> mapRunSummaryMgmtUpdated = null;	
		//Navigate the RunSummaryMgmt map to fetch the Backup queries one by one
		//With the parent ids as the criteria, fetch the records and load it into respective tables
		// Add the counts in RunSummaryMgmt table
		return mapRunSummaryMgmtUpdated;
	}
}
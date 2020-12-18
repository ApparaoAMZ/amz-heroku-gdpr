package com.amazon.gdpr.service;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import com.amazon.gdpr.model.gdpr.output.RunAnonymization;
import com.amazon.gdpr.model.gdpr.output.RunModuleMgmt;
import com.amazon.gdpr.model.gdpr.output.RunSummaryMgmt;
import com.amazon.gdpr.processor.AnonymizationProcessor;
import com.amazon.gdpr.processor.BackupTableProcessor;
import com.amazon.gdpr.processor.RunMgmtProcessor;
import com.amazon.gdpr.processor.SummaryDataProcessor;
import com.amazon.gdpr.util.GdprException;
import com.amazon.gdpr.util.GlobalConstants;

/****************************************************************************************
 * This Service performs the Initialization activity on the Heroku GDPR  
 * This will be invoked by the GDPRController
 ****************************************************************************************/
@Component
public class InitService {
	
	public static String CURRENT_CLASS	= GlobalConstants.MODULE_INITIALIZATION;
	public static String STATUS_SUCCESS = GlobalConstants.STATUS_SUCCESS;
	
	public List<RunAnonymization> lstRunAnonymization=null;
	public Map<String, RunSummaryMgmt> mapRunSummaryMgmt=null;
	private long runId = 0;
	
	@Autowired
	RunMgmtProcessor runMgmtProcessor;
	
	@Autowired
	AnonymizationProcessor anonymizationProcessor;

	@Autowired
	BackupTableProcessor bkpupTableProcessor;

	@Autowired
	SummaryDataProcessor summaryDataProcessor;
	
	/**
	 * This method initiates the InitService activities
	 * @param runName
	 * @return
	 */
	public String initService(String runName) {
		String CURRENT_METHOD = "initService";
		System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+":: Inside method");
		
		String initServiceReturnStatus = "";
		Boolean exceptionOccured = false;
		Date moduleStartDateTime = null;
		Date moduleEndDateTime = null;
				
		try{
			//Initiates the run. Establishes the run in the DB
			moduleStartDateTime = new Date();
			runId =  runMgmtProcessor.initializeRun(runName);			
			String[] initServiceStatus = initialize(runId);
			initServiceReturnStatus = initServiceStatus[1];
		} catch(GdprException exception) {
			exceptionOccured = true;
			initServiceReturnStatus = exception.getExceptionMessage();
		}
		moduleEndDateTime = new Date();
		try {
			String moduleStatus = exceptionOccured ? GlobalConstants.STATUS_FAILURE : GlobalConstants.STATUS_SUCCESS;
			RunModuleMgmt runModuleMgmt = new RunModuleMgmt(runId, GlobalConstants.MODULE_INITIALIZATION, moduleStatus, 
					moduleStartDateTime, moduleEndDateTime, initServiceReturnStatus);			
			runMgmtProcessor.initiateModuleMgmt(runModuleMgmt);
		} catch(GdprException exception) {
			exceptionOccured = true;
			initServiceReturnStatus = initServiceReturnStatus + exception.getMessage();
		}
		if (exceptionOccured)
			initServiceReturnStatus = initServiceReturnStatus + GlobalConstants.ERR_DISPLAY;
		return initServiceReturnStatus;
	}
	
	/**
	 * The initialization activities are performed. Verification of all the basic steps required for the project
	 * @param runId
	 * @return
	 */
	public String[] initialize(long runId) throws GdprException {
		
		String CURRENT_METHOD = "initialize";
		System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+" :: Inside method");
		String[] initializationStatus = new String[2];		
		
		System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+" :: Before Anonymization Processor : "+LocalTime.now());
		int insertRunAnonymizationCounts = anonymizationProcessor.loadRunAnonymization(runId);
		System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+" :: After Anonymization Processor : "+LocalTime.now());
		if(insertRunAnonymizationCounts == 0) {
			initializationStatus[0] = GlobalConstants.STATUS_FAILURE;
			initializationStatus[1] = GlobalConstants.RUN_ANONYMIZATION_ZERO;
		}
		else {
			initializationStatus[0] = GlobalConstants.STATUS_SUCCESS;
			System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+" :: Before Backup Processor : "+LocalTime.now());
			String processBackupTableStatus = bkpupTableProcessor.processBkpupTable(runId);
			System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+" :: After Backup Processor : "+LocalTime.now());
			System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+" :: Before Summary Processor : "+LocalTime.now());
			//String summaryStatus = summaryDataProcessor.processSummaryData(runId);
			System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+" :: After Summary Processor : "+LocalTime.now());
			initializationStatus[1] = GlobalConstants.RUN_ANONYMIZATION_INSERT + insertRunAnonymizationCounts 
					+ GlobalConstants.SEMICOLON_STRING + "" + GlobalConstants.MSG_GDPR_PROCESSING;
						
		}
		return initializationStatus;
	}
	
	/**
	 * @return the runId
	 */
	public long getRunId() {
		return runId;
	}

	/**
	 * @param runId the runId to set
	 */
	public void setRunId(long runId) {
		this.runId = runId;
	}
}
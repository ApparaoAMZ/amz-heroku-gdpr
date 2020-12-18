package com.amazon.gdpr.service;

import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import com.amazon.gdpr.dao.GdprInputDaoImpl;
import com.amazon.gdpr.dao.GdprOutputDaoImpl;
import com.amazon.gdpr.model.gdpr.output.RunErrorMgmt;
import com.amazon.gdpr.model.gdpr.output.RunModuleMgmt;
import com.amazon.gdpr.processor.RunMgmtProcessor;
import com.amazon.gdpr.util.GdprException;
import com.amazon.gdpr.util.GlobalConstants;

/****************************************************************************************
 * This Service will reorganize the GDPR_Depersonalization__c table  
 * This will be invoked by the GDPRController
 ****************************************************************************************/
@Component
public class ReOrganizeInputService {

	public static String CURRENT_CLASS	= GlobalConstants.MODULE_REORGANIZEINPUT;
	
	@Autowired
	JobLauncher jobLauncher;
	
	@Autowired
    Job processGdprDepersonalizationJob;
	
	@Autowired
	RunMgmtProcessor runMgmtProcessor;

	@Autowired
	GdprInputDaoImpl gdprInputDaoImpl;
	
	@Autowired
	GdprOutputDaoImpl gdprOutputDaoImpl;
	
	/**
	 * This method initiates the InitService activities
	 * @param runName
	 * @return
	 */
	public void reOrganizeInputService(long runId) {
		String CURRENT_METHOD = "initService";
		System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+":: Inside method");
		
		String reOrganizeInputServiceReturnStatus = "";
		Boolean exceptionOccured = false;
		Date moduleStartDateTime = null;
		Date moduleEndDateTime = null;
				
		/*try{
			//Initiates the run. Establishes the run in the DB			
			reOrganizeData(runId);
		} catch(GdprException exception) {
			exceptionOccured = true;
			reOrganizeInputServiceReturnStatus = exception.getExceptionMessage();
		}*/
		moduleEndDateTime = new Date();
		try {
			String moduleStatus = exceptionOccured ? GlobalConstants.STATUS_FAILURE : GlobalConstants.STATUS_SUCCESS;
			RunModuleMgmt runModuleMgmt = new RunModuleMgmt(runId, GlobalConstants.MODULE_REORGANIZEINPUT, moduleStatus, 
					moduleStartDateTime, moduleEndDateTime, reOrganizeInputServiceReturnStatus);			
			runMgmtProcessor.initiateModuleMgmt(runModuleMgmt);
		} catch(GdprException exception) {
			exceptionOccured = true;
			reOrganizeInputServiceReturnStatus = reOrganizeInputServiceReturnStatus + exception.getMessage();
		}
		if (exceptionOccured)
			reOrganizeInputServiceReturnStatus = reOrganizeInputServiceReturnStatus + GlobalConstants.ERR_DISPLAY;
		//return reOrganizeInputServiceReturnStatus;
	}
	
	
	public void reOrganizeData(long runId) { //throws GdprException {
		String CURRENT_METHOD = "reOrganizeData";
		System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+":: Inside method");
		RunErrorMgmt runErrorMgmt = null;
		String reOrganizeDataStatus = "";
		
		try {
			JobParametersBuilder jobParameterBuilder= new JobParametersBuilder();
			jobParameterBuilder.addLong(GlobalConstants.JOB_REORGANIZE_INPUT_RUNID, runId);
					
			System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+" :: JobParameters set ");
			JobParameters jobParameters = jobParameterBuilder.toJobParameters();
			Thread thread = new Thread(){
				@Override
			    public void run(){
			    	try {
						jobLauncher.run(processGdprDepersonalizationJob, jobParameters);
					} catch (JobExecutionAlreadyRunningException | JobRestartException
							| JobInstanceAlreadyCompleteException | JobParametersInvalidException exception) {
						System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+" :: "+GlobalConstants.ERR_REORGANIZE_JOB_RUN);
						exception.printStackTrace();
						//runErrorMgmt = new RunErrorMgmt(runId, CURRENT_CLASS, CURRENT_METHOD, 
							//	GlobalConstants.ERR_REORGANIZE_JOB_RUN, exception.getMessage());
					}
			    }
			};
			thread.start();
			
			//System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+" :: ReOrganize GDPR_Depersonalization table complete. ");
			//reOrganizeDataStatus = "Job Successfully loaded. ";
		} catch (Exception exception) {	
			System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+" :: "+GlobalConstants.ERR_RUN_GDPR_DEPERSONALIZATION);
			exception.printStackTrace();
			runErrorMgmt = new RunErrorMgmt(runId, CURRENT_CLASS, CURRENT_METHOD, 
					GlobalConstants.ERR_RUN_GDPR_DEPERSONALIZATION, exception.getMessage());
		}
		/*try {
			if (runErrorMgmt != null) {
				gdprOutputDaoImpl.loadErrorDetails(runErrorMgmt);
				//throw new GdprException(GlobalConstants.ERR_RUN_GDPR_DEPERSONALIZATION);
			}
		} catch (Exception exception) {
			System.out.println(CURRENT_CLASS + " ::: " + CURRENT_METHOD + " :: " + GlobalConstants.ERR_RUN_GDPR_DEPERSONALIZATION  
					+ GlobalConstants.ERR_RUN_ERROR_MGMT_INSERT);
			exception.printStackTrace();
			//throw new GdprException(GlobalConstants.ERR_RUN_GDPR_DEPERSONALIZATION + GlobalConstants.ERR_RUN_ERROR_MGMT_INSERT);
		}*/
		//return reOrganizeDataStatus;
	}	
}

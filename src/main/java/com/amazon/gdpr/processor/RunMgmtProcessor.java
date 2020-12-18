package com.amazon.gdpr.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazon.gdpr.dao.GdprOutputDaoImpl;
import com.amazon.gdpr.dao.RunMgmtDaoImpl;
import com.amazon.gdpr.model.gdpr.output.RunErrorMgmt;
import com.amazon.gdpr.model.gdpr.output.RunMgmt;
import com.amazon.gdpr.model.gdpr.output.RunModuleMgmt;
import com.amazon.gdpr.util.GdprException;
import com.amazon.gdpr.util.GlobalConstants;

/****************************************************************************************
 * This processor verifies the previous failure run / initiates a current run 
 * Any processing or updates related to the RunMgmt tables are performed here
 ****************************************************************************************/
@Component
public class RunMgmtProcessor {
		
	private static String CURRENT_CLASS		 		= GlobalConstants.CLS_RUNMGMTPROCESSOR;
	private static String STATUS_FAILURE			= GlobalConstants.STATUS_FAILURE;
	
	@Autowired
	RunMgmtDaoImpl runMgmtDaoImpl;
			
	@Autowired
	GdprOutputDaoImpl gdprOutputDaoImpl;
	
	/**
	 * This method handled the initialization of the current run
	 * Takes a call whether the old failure run has to be proceeded on or a new run should be instantiated	 
	 * @param runName The description of the current run is being maintained
	 * @return The RunId is returned back to controller and this is passed on to all methods 
	 */
	public long initializeRun(String runName) throws GdprException {
		String CURRENT_METHOD = "initializeRun";		
		System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+" :: Inside method");
		long runId = 0;
		
		//Fetch lastRunId if failed
		RunModuleMgmt module = oldRunVerification();
		if(module == null)
			runId = initiateNewRun(runName);
		return runId;
	}		
	
	/**
	 * The last failure run is fetched and verified if it needs to be proceeded on 
	 * @return Returns the last Failure entry of the RunModuleMgmt
	 */
	public RunModuleMgmt oldRunVerification() throws GdprException {
		String CURRENT_METHOD = "oldRunVerification";		
		System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+" :: Inside method");
		RunErrorMgmt runErrorMgmt = null;
		
		try{
			RunMgmt runMgmt = runMgmtDaoImpl.fetchLastRunDetail();
			if(runMgmt != null && STATUS_FAILURE.equalsIgnoreCase(runMgmt.getRunStatus()) ){
				System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+" :: Past Failure RunID : "+runMgmt.getRunId());
			}else {
				System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+" :: No past failure run available. ");
				return null;
			}
		} catch(Exception exception) {	
			System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+" :: "+GlobalConstants.ERR_OLD_RUN_FETCH);
			exception.printStackTrace();
			runErrorMgmt = new RunErrorMgmt(GlobalConstants.DUMMY_RUN_ID, CURRENT_CLASS, CURRENT_METHOD, 
					GlobalConstants.ERR_OLD_RUN_FETCH, exception.getMessage());
		}
		try {
			if (runErrorMgmt != null) {
				gdprOutputDaoImpl.loadErrorDetails(runErrorMgmt);
				throw new GdprException(GlobalConstants.ERR_OLD_RUN_FETCH);
			}
		} catch (Exception exception) {
			System.out.println(CURRENT_CLASS + " ::: " + CURRENT_METHOD + " :: " + GlobalConstants.ERR_OLD_RUN_FETCH + GlobalConstants.ERR_RUN_ERROR_MGMT_INSERT);
			exception.printStackTrace();
			throw new GdprException(GlobalConstants.ERR_OLD_RUN_FETCH + GlobalConstants.ERR_RUN_ERROR_MGMT_INSERT);
		}
		return null;
	}
		
	/**
	 * A new run is initiated in this method. An entry is made in the RunMgmt table for this run
	 * @param runName The description of the current run is being maintained
	 * @return The RunId is returned back to controller and this is passed on to all methods
	 */
	public long initiateNewRun(String runName) throws GdprException {
		String CURRENT_METHOD = "initiateNewRun";		
		System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+" :: New run initiation in progress.");
		long runId = 0;		
		RunErrorMgmt runErrorMgmt = null;
		
		try {
			runMgmtDaoImpl.initiateNewRun(runName);
			runId = runMgmtDaoImpl.fetchLastRunDetail().getRunId(); 
		} catch(Exception exception) {	
			System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+" :: "+GlobalConstants.ERR_NEW_RUN_INITIATION);
			exception.printStackTrace();
			runErrorMgmt = new RunErrorMgmt(GlobalConstants.DUMMY_RUN_ID, CURRENT_CLASS, CURRENT_METHOD, 
					GlobalConstants.ERR_NEW_RUN_INITIATION, exception.getMessage());
		}
		try {
			if (runErrorMgmt != null) {
				gdprOutputDaoImpl.loadErrorDetails(runErrorMgmt);
				throw new GdprException(GlobalConstants.ERR_NEW_RUN_INITIATION);
			}
		} catch (Exception exception) {
			System.out.println(CURRENT_CLASS + " ::: " + CURRENT_METHOD + " :: " + GlobalConstants.ERR_NEW_RUN_INITIATION + GlobalConstants.ERR_RUN_ERROR_MGMT_INSERT);
			exception.printStackTrace();
			throw new GdprException(GlobalConstants.ERR_NEW_RUN_INITIATION + GlobalConstants.ERR_RUN_ERROR_MGMT_INSERT);
		}
		return runId;
	}
	
	public void initiateModuleMgmt(RunModuleMgmt runModuleMgmt) throws GdprException {
		String CURRENT_METHOD = "initiateModuleMgmt";		
		System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+" :: Module updates in progress.");
		RunErrorMgmt runErrorMgmt = null;
		
		try {
			runMgmtDaoImpl.insertModuleUpdates(runModuleMgmt);
		} catch(Exception exception) {	
			System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+" :: "+GlobalConstants.ERR_MODULE_MGMT_INSERT);
			exception.printStackTrace();
			runErrorMgmt = new RunErrorMgmt(GlobalConstants.DUMMY_RUN_ID, CURRENT_CLASS, CURRENT_METHOD, 
					GlobalConstants.ERR_MODULE_MGMT_INSERT, exception.getMessage());
		}
		
		try {
			if (runErrorMgmt != null) {
				gdprOutputDaoImpl.loadErrorDetails(runErrorMgmt);
				throw new GdprException(GlobalConstants.ERR_MODULE_MGMT_INSERT);
			}
		} catch (Exception exception) {
			System.out.println(CURRENT_CLASS + " ::: " + CURRENT_METHOD + " :: " + GlobalConstants.ERR_MODULE_MGMT_INSERT + GlobalConstants.ERR_RUN_ERROR_MGMT_INSERT);
			exception.printStackTrace();
			throw new GdprException(GlobalConstants.ERR_MODULE_MGMT_INSERT + GlobalConstants.ERR_RUN_ERROR_MGMT_INSERT);
		}
	}
}
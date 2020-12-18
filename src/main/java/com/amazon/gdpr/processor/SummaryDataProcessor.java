package com.amazon.gdpr.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazon.gdpr.dao.GdprOutputDaoImpl;
import com.amazon.gdpr.model.gdpr.output.RunErrorMgmt;
import com.amazon.gdpr.model.gdpr.output.RunSummaryMgmt;
import com.amazon.gdpr.model.gdpr.output.SummaryData;
import com.amazon.gdpr.util.GdprException;
import com.amazon.gdpr.util.GlobalConstants;
import com.amazon.gdpr.util.SqlQueriesConstant;

@Component
public class SummaryDataProcessor {
	
	public static String CURRENT_CLASS = GlobalConstants.CLS_SUMMARYDATAPROCESSOR;
	Map<String, RunSummaryMgmt> runSummaryMgmtMap = null;
	RunErrorMgmt runErrorMgmt=null;
	
	@Autowired
	GdprOutputDaoImpl gdprOutputDaoImpl;
	
	public String processSummaryData(long runId) throws GdprException {
		
		String CURRENT_METHOD = "processSummaryData";
		System.out.println(CURRENT_CLASS + " ::: " + CURRENT_METHOD + " :: Inside method");
		  
		List<SummaryData> lstSummaryData = this.extractSummaryDetails(runId);
		if(lstSummaryData != null && lstSummaryData.size() > 0) {
			List<RunSummaryMgmt> lstRunSummaryMgmt = this.transformSummaryDetails(runId, lstSummaryData);
			this.loadRunSummaryMgmt(lstRunSummaryMgmt);
		}
		
		return GlobalConstants.MSG_SUMMARY_ROWS+lstSummaryData.size();
	}
		
	public List<SummaryData> extractSummaryDetails(long runId) throws GdprException {
		String CURRENT_METHOD = "extractSummaryDetails";
		System.out.println(CURRENT_CLASS + " ::: " + CURRENT_METHOD + " :: Inside method");
		
		List<SummaryData> lstSummaryData = null;
		
		try{
			lstSummaryData = gdprOutputDaoImpl.fetchSummaryDetails(runId);
		} catch (Exception exception) {	
			System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+" :: "+GlobalConstants.ERR_RUN_SUMMARY_DATA);
			exception.printStackTrace();
			runErrorMgmt = new RunErrorMgmt(GlobalConstants.DUMMY_RUN_ID, CURRENT_CLASS, CURRENT_METHOD, 
					GlobalConstants.ERR_RUN_SUMMARY_DATA, exception.getMessage());
		}
		try {
			if (runErrorMgmt != null) {
				gdprOutputDaoImpl.loadErrorDetails(runErrorMgmt);
				throw new GdprException(GlobalConstants.ERR_RUN_SUMMARY_DATA);
			}
		} catch (Exception exception) {
			System.out.println(CURRENT_CLASS + " ::: " + CURRENT_METHOD + " :: " + GlobalConstants.ERR_RUN_SUMMARY_DATA  
					+ GlobalConstants.ERR_RUN_ERROR_MGMT_INSERT);
			exception.printStackTrace();
			throw new GdprException(GlobalConstants.ERR_RUN_SUMMARY_DATA + GlobalConstants.ERR_RUN_ERROR_MGMT_INSERT);
		}
		return lstSummaryData;
	}
	
	public List<RunSummaryMgmt> transformSummaryDetails(long runId, List<SummaryData> lstSummaryData) throws GdprException {
		String CURRENT_METHOD = "transformSummaryDetails";
		System.out.println(CURRENT_CLASS + " ::: " + CURRENT_METHOD + " :: Inside method");
		
		int prevCategoryId = 0;
		String prevRegion = "";
		String prevCountryCode = "";
		int prevImpactTableId = 0; 
		String prevImpactTableName = "";
		String backupQuery = "SELECT ";
		String depersonalizationQuery = "UPDATE ";
		RunSummaryMgmt runSummaryMgmt = null;		
		List<RunSummaryMgmt> lstRunSummaryMgmt = new ArrayList<RunSummaryMgmt>();
		
		try{
			for(SummaryData summaryData : lstSummaryData) {
				String currentRegion = summaryData.getRegion();
				String currentCountryCode = summaryData.getCountryCode();
				int currentCategoryId = summaryData.getCategoryId();
				int currentImpactTableId = summaryData.getImpactTableId();
				String currentImpactTableName = summaryData.getImpactTableName();
				String currentImpactFieldName = summaryData.getImpactFieldName();
				String currentImpactFieldType = summaryData.getImpactFieldType();
								
				if(prevCategoryId == 0){
					backupQuery = backupQuery + currentImpactFieldName;
					depersonalizationQuery = depersonalizationQuery + currentImpactTableName + " SET " + 
							fetchUpdateField(currentImpactFieldName, currentImpactFieldType, summaryData.getTransformationType());
							
							//+ currentImpactFieldName +" = "+summaryData.getTransformationType();
				}else{
					if(currentCategoryId == prevCategoryId && currentRegion.equalsIgnoreCase(prevRegion) && 
							currentCountryCode.equalsIgnoreCase(prevCountryCode) && prevImpactTableId == currentImpactTableId ) {
						backupQuery = backupQuery + GlobalConstants.COMMA_STRING + currentImpactFieldName;
					    depersonalizationQuery = depersonalizationQuery + GlobalConstants.COMMA_STRING 
							+ fetchUpdateField(currentImpactFieldName, currentImpactFieldType, summaryData.getTransformationType());
							//currentImpactFieldName +" = "+summaryData.getTransformationType();
					} else {
						backupQuery = backupQuery + " FROM " + prevImpactTableName; 
						runSummaryMgmt = new RunSummaryMgmt(runId, prevCategoryId, prevRegion, prevCountryCode, prevImpactTableId, backupQuery, depersonalizationQuery);
						//System.out.println(CURRENT_CLASS + " ::: " + CURRENT_METHOD + " :: runSummaryMgmt "+runSummaryMgmt.toString());
						lstRunSummaryMgmt.add(runSummaryMgmt);
						backupQuery = "SELECT "+currentImpactFieldName;
						depersonalizationQuery = "UPDATE " + currentImpactTableName + " SET " 
								+ fetchUpdateField(currentImpactFieldName, currentImpactFieldType, summaryData.getTransformationType());
								//+ currentImpactFieldName +" = "+summaryData.getTransformationType();
					}
				}
			
				prevCategoryId = currentCategoryId;
				prevRegion = currentRegion;
				prevCountryCode = currentCountryCode;
				prevImpactTableId = currentImpactTableId;
				prevImpactTableName = currentImpactTableName;
			}
			backupQuery = backupQuery + " FROM " + prevImpactTableName;
			runSummaryMgmt = new RunSummaryMgmt(runId, prevCategoryId, prevRegion, prevCountryCode, prevImpactTableId, backupQuery, depersonalizationQuery);
			//System.out.println(CURRENT_CLASS + " ::: " + CURRENT_METHOD + " :: runSummaryMgmt "+runSummaryMgmt.toString());
			lstRunSummaryMgmt.add(runSummaryMgmt);	
		} catch (Exception exception) {	
			System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+" :: "+GlobalConstants.ERR_RUN_ANONYMIZATION_LOAD);
			exception.printStackTrace();
			runErrorMgmt = new RunErrorMgmt(GlobalConstants.DUMMY_RUN_ID, CURRENT_CLASS, CURRENT_METHOD, 
					GlobalConstants.ERR_RUN_ANONYMIZATION_LOAD, exception.getMessage());
		}
		try {
			if (runErrorMgmt != null) {
				gdprOutputDaoImpl.loadErrorDetails(runErrorMgmt);
				throw new GdprException(GlobalConstants.ERR_RUN_ANONYMIZATION_LOAD);
			}
		} catch (Exception exception) {
			System.out.println(CURRENT_CLASS + " ::: " + CURRENT_METHOD + " :: " + GlobalConstants.ERR_RUN_ANONYMIZATION_LOAD  
					+ GlobalConstants.ERR_RUN_ERROR_MGMT_INSERT);
			exception.printStackTrace();
			throw new GdprException(GlobalConstants.ERR_RUN_ANONYMIZATION_LOAD + GlobalConstants.ERR_RUN_ERROR_MGMT_INSERT);
		}
		return lstRunSummaryMgmt;
	}
	
	public String fetchUpdateField(String fieldName, String fieldType, String conversionType) {
		
		String CURRENT_METHOD = "fetchUpdateField";
		//System.out.println(CURRENT_CLASS + " ::: " + CURRENT_METHOD + " :: Inside method");
		String subQuery = "";
		
		if(fieldType.startsWith(GlobalConstants.DATE_DATATYPE)){
			subQuery =  fieldName+" = TO_DATE(TO_CHAR("+fieldName+", \'"+conversionType+"\'), \'DD-MM-YYYY\')";
		}else if (fieldType.startsWith(GlobalConstants.TEXT_DATATYPE)){
			switch (conversionType) {
				case "PRIVACY DELETED" : 
					subQuery =  fieldName+" = \'Privacy Deleted\'";
				case "NULL" :
					subQuery =  fieldName+" = null";
				case "EMPTY" :
					subQuery =  fieldName+" = \'\'";
				case "ALL ZEROS" :
					subQuery =  fieldName+" = TRANSLATE("+fieldName+", \'123456789\', \'000000000\')";
				default : 
					subQuery =  fieldName+" = \'"+conversionType+"\'";
			}
		} else {
			subQuery =  fieldName+" = \'"+conversionType+"\'";
		}
		return subQuery;
	}
	
	public void loadRunSummaryMgmt(List<RunSummaryMgmt> lstRunSummaryMgmt) throws GdprException {
		String CURRENT_METHOD = "loadRunSummaryMgmt";
		System.out.println(CURRENT_CLASS + " ::: " + CURRENT_METHOD + " :: Inside method");

		try{
			gdprOutputDaoImpl.batchInsertRunSummaryMgmt(lstRunSummaryMgmt, SqlQueriesConstant.BATCH_ROW_COUNT);
			
		} catch (Exception exception) {	
			System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+" :: "+GlobalConstants.ERR_RUN_SUMMARY_MGMT_INSERT);
			exception.printStackTrace();
			runErrorMgmt = new RunErrorMgmt(GlobalConstants.DUMMY_RUN_ID, CURRENT_CLASS, CURRENT_METHOD, 
					GlobalConstants.ERR_RUN_SUMMARY_MGMT_INSERT, exception.getMessage());
		}
		try {
			if (runErrorMgmt != null) {
				gdprOutputDaoImpl.loadErrorDetails(runErrorMgmt);
				throw new GdprException(GlobalConstants.ERR_RUN_SUMMARY_MGMT_INSERT);
			}
		} catch (Exception exception) {
			System.out.println(CURRENT_CLASS + " ::: " + CURRENT_METHOD + " :: " + GlobalConstants.ERR_RUN_SUMMARY_MGMT_INSERT  
					+ GlobalConstants.ERR_RUN_ERROR_MGMT_INSERT);
			exception.printStackTrace();
			throw new GdprException(GlobalConstants.ERR_RUN_SUMMARY_MGMT_INSERT + GlobalConstants.ERR_RUN_ERROR_MGMT_INSERT);
		}
	}
}
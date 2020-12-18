package com.amazon.gdpr.util;

/****************************************************************************************
 * This Class contains all the constants that is required through out the project 
 ****************************************************************************************/
public class GlobalConstants {

	//Status
	public static String STATUS_INPROGRESS 		= "INPROGRESS";
	public static String STATUS_SUCCESS 		= "SUCCESS";
	public static String STATUS_FAILURE 		= "FAILURE";
	public static String STATUS_ACTIVE			= "ACTIVE";
	public static String STATUS_SCHEDULED		= "SCHEDULED";
	public static String STATUS_CLEARED			= "CLEARED";
	
	// Modules 
	public static String MODULE_INITIALIZATION 		= "Initialization Module";
	public static String MODULE_REORGANIZEINPUT		= "ReOrganize Input Module";
	public static String MODULE_DATABACKUP 			= "DataBackup Module";
	public static String MODULE_DEPERSONALIZATION 	= "Depersonalization Module";
	public static String MODULE_DATAREFRESH 		= "DataRefresh Module";
	
	//Exception Categories
	public static String EXCEPTION_GENERAL 			= "General Exception";
	public static String EXCEPTION_SQLEXCEPTION 	= "SQL Exception";
	public static String EXCEPTION_URIEXCEPTION 	= "URI Exception";
	
	public static String NA_STRING	= "NA";
	public static String EMPTY_STRING 	= "";
	public static String SPACE_STRING   = " ";
	public static String SEMICOLON_STRING = "; ";
	public static String COMMA_STRING = ", ";
	
	//Controller Files
	public static String CLS_WELCOMEPAGECONTROLLER		= "WelcomePageController";
	public static String CLS_INPUTDETAILCONTROLLER		= "InputDetailController";
	public static String CLS_SALESFORCEDETAILCONTROLLER = "SalesforceDetailController";	
	public static String CLS_GDPRCONTROLLER 			= "GdprController";
	public static String CLS_GDPRPROCESSCONTROLLER		= "GdprProcessController";
	public static String CLS_FILEUPLOADCONTROLLER		= "FileUploadController";
	
	public static String CLS_GDPRAPPLICATION		= "GDPRApplication";
	public static String CLS_GDPRCMDLINEAPPLICATION = "GdprCmdLineApplication";
	
	public static String CLS_RUNMGMTPROCESSOR 		= "RunMgmtProcessor";
	public static String CLS_ANONYMIZATIONPROCESSOR = "AnonymizationProcessor";
	public static String CLS_GDPRDATAPROCESSOR		= "GdprDataProcessor";
	public static String CLS_SUMMARYDATAPROCESSOR   = "SummaryDataProcessor";
	public static String CLS_GDPRDEPERSONALIZATIONPROCESSOR = "GdprDepersonalizationProcessor";	
	
	public static String CLS_DATABASECONFIG			= "DatabaseConfig";
	public static String CLS_GDPR_DEPERSONALIZATION_BATCH_CONFIG		= "GdprDepersonalizationBatchConfig";
	
	public static String CLS_RUNMGMTDAOIMPL			= "RunMgmtDaoImpl";
	public static String CLS_GDPRINPUTDAOIMPL		= "GdprInputDaoImpl";
	public static String CLS_GDPROUTPUTDAOIMPL		= "GdprOutputDaoImpl";
	public static String CLS_HVHOUTPUTDAOIMPL		= "HvhOutputDaoImpl";
	public static String CLS_WRITER					= "Writer";
	
	public static String CLS_RUNMGMTROWMAPPER				= "RunMgmtRowMapper";
	public static String CLS_CATEGORYROWMAPPER				= "CategoryRowMapper";
	public static String CLS_IMPACTTABLEROWMAPPER			= "ImpactTableRowMapper";
	public static String CLS_IMPACTFIELDROWMAPPER			= "ImpactFieldRowMapper";
	public static String CLS_ANONYMIZATIONDETAILROwMAPPER 	= "AnonymizationDetailRowMapper";
	public static String CLS_SUMMARYDATAROWMAPPER			= "SummaryDataRowMapper";
	
	/*****Backup Table Processor Code Change Starts***/
	public static String CLS_BACKUPTABLEPROCESSORDAOIMPL		= "BackupTableProcessorDaoImpl";
	public static String CLS_BACKUPTABLEPROCESSOR				= "BackupTableProcessor";
	public static String CLS_IMPACTTABLEDETAILSROWMAPPER		= "ImpactTableDetailsRowMapper";
	public static String CLS_BACKUPTABLEDETAILSROWMAPPER		= "CLS_BACKUPTABLEDETAILSROWMAPPER";
	public static String CLS_GDPRDEPERSONALIZATIONINPUTROWMAPPER	= "GdprDepersonalizationInputRowMapper";
	
	public static String CLS_JOBCOMPLETIONLISTENER = "JobCompletionListener";
	
	/*****Backup Table Processor Code Change Ends***/
	public static String TEMPLATE_FILEUPLOAD				= "fileUpload";
	public static String TEMPLATE_GDPRINPUT					= "gdprInput";
	
	public static String ATTRIBUTE_MESSAGE					= "message";
	public static String ATTRIBUTE_GDPRINPUT				= "gdprInput";
		
	public static String COL_RUN_ID					= "RUN_ID";
		
	public static String ANONYMIZATION_FILE_TYPE 	= "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	public static String FILE_UPLOAD_LOCATION		= "//temp/GDPR/";
	
	public static String ERROR_FILE_MESSAGE 	= "Please upload a valid anonymization excel file. ";
	public static String ERROR_FILE_EMPTY 		= ERROR_FILE_MESSAGE + "Not file attached. ";
	public static String ERROR_FILE_NOTEXCEL	= ERROR_FILE_MESSAGE + "File attached is not excel. ";
	public static String ERROR_FILE_UPLOAD		= "File processing had an issue. Please verify logs. ";
	public static String SUCCESS_FILE_MESSAGE	= "File has been uploaded !!!";
	
	public static String DATE_FORMAT 			= "yyyy-MM-dd_HH:mm:ss";
	
	//File Upload Messages
	public static String MSG_FILE_UPLOAD		= "Please upload Anonymization File only. ";
	public static String FILE_UPLOAD_SUCCESS	= "File uploaded successfully! ";
	public static String FILE_SHEET_NAME		= "Sheet1";
	
	public static String DATE_DATATYPE 	= "Date";
	public static String TEXT_DATATYPE	= "TEXT";
	
	public static String CONVERSION_TYPE_PRIVACY_DELETED = "PRIVACY DELETED";
	
	public static String JOB_REORGANIZE_HVH_INPUT_PROCESSOR = "ReOrganizeHvhInput";
	public static String JOB_REORGANIZE_INPUT_CATEGORYMAP = "CategoryMap";	
	public static String JOB_REORGANIZE_INPUT_RUNID		 = "RunId";
	public static String JOB_REORGANIZE_INPUT_FIELDCATEGORYMAP = "FieldCategoryMap";
	
	//Error Messages
	public static int DUMMY_RUN_ID = 0;
	public static int ERROR_INITIAL_INDEX = 0;
	public static int ERROR_DETAIL_SIZE = 4999;
	public static String ERR_DISPLAY 				= "Please verify log for further details.";
	public static String ERR_RUN_INITIALIZATION 	= "Run Initiation Exception. ";
	public static String ERR_OLD_RUN_FETCH 			= "Facing issues in verifying the Old run details. ";
	public static String ERR_NEW_RUN_INITIATION 	= "Facing issues in initiating a new run. ";
	public static String ERR_MODULE_MGMT_INSERT		= "Facing issues in inserting data in GDPR Output table - RUN_MODULE_MGMT. ";
		
	public static String MSG_FILE_ROWS_PROCESSED_0	= "No new rows are added to Anonymization file for processing.";
	public static String MSG_FILE_ROWS_PROCESSED	= "Rows parsed from Anonymization file : ";
	public static String MSG_IMPACT_FIELD_ROWS		= "New rows inserted in IMPACT_FIELD table : ";
	public static String MSG_ANONYMIZATION_DTL_ROWS = "New rows inserted in ANONYMIZATION_DETAIL table : ";
	public static String MSG_SUMMARY_ROWS			= "Summary details : ";
	public static String MSG_GDPR_PROCESSING		= "Reorganizing GDPR_Depersonalization__c initiated. ";
	
	public static String ERR_FILE_EMPTY						= "The file is empty. Please upload file with content. ";
	public static String ERR_PARSE_ANONYMIZATION_IO			= "Facing  IO issues in parsing the Anonymization file. ";
	public static String ERR_PARSE_ANONYMIZATION			= "Facing  issues in parsing the Anonymization file. ";
	public static String ERR_GDPR_INPUT_FETCH				= "Facing issues in accessing GDPR Input tables - IMPACT_TABLE, IMPACT_FIELD. ";
	public static String ERR_IMPACT_FIELD_INSERT			= "Facing issues in inserting data in GDPR Input table - IMPACT_FIELD. ";
	public static String ERR_GDPR_INPUT_ALL_FETCH			= "Facing issues in accessing GDPR Input tables - CATEGORY, IMPACT_TABLE, IMPACT_FIELD,"
																+ " ANONYMIZATION_DETAIL. ";
	public static String ERR_ANONYMIZATION_DETAIL_INSERT	= "Facing issues in inserting data in GDPR Input table - ANONYMIZATION_DETAIL. ";
	
	public static String ERR_RUN_ERROR_MGMT_INSERT			= "Facing issues in inserting data in GDPR Output table - RUN_ERROR_MGMT. ";
	
	// Initiatlization Status
	public static String RUN_ANONYMIZATION_INSERT = "Run Anonymization Initiated Count : ";
	public static String RUN_ANONYMIZATION_ZERO   = "No Run Anonymization rows to process. ";
	public static String ERR_RUN_ANONYMIZATION_LOAD   = "Facing issues in loading table - RUN_ANONYMIZATION. ";
	
	public static String ERR_RUN_SUMMARY_DATA     = "Facing issues in fetching the Summary data from the INPUT Tables. ";
	public static String ERR_RUN_SUMMARY_MGMT_INSERT  = "Facing issues in inserting data in GDPR Output Table - RUN_SUMMARY_MGMT. ";
	
	/*****Backup Table Processor Code Change Starts***/
	public static String ERR_RUN_BACKUP_TABLE_PROCESSOR   = "Facing issues in backup table processor - BackupTableProcessor. ";
	public static String ERR_RUN_BACKUP_TABLE_COLUMNCHECK   = "Facing issues in checking backup table column exists or not - BackupTableProcessor. ";
	public static String ERR_RUN_BACKUP_TABLE_REFRESH   = "Facing issues in refreshing backup tables- BackupTableProcessor. ";
	
	public static String ERR_RUN_GDPR_DEPERSONALIZATION	= "Facing issues while processing the GDPR Depersonalization table data. ";
	public static String ERR_REORGANIZE_JOB_RUN	= "Facing issues while initiating the job run. ";
	/*****Backup Table Processor Code Change Ends***/	 
	
	public static String ERR_REORGANIZEINPUT_BEFORESTEP	= "Facing issues in processing the Job parameters. ";
}
package com.amazon.gdpr.configuration;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.amazon.gdpr.batch.JobCompletionListener;
import com.amazon.gdpr.dao.GdprInputDaoImpl;
import com.amazon.gdpr.dao.GdprOutputDaoImpl;
import com.amazon.gdpr.dao.HvhOutputDaoImpl;
import com.amazon.gdpr.model.GdprDepersonalizationInput;
import com.amazon.gdpr.model.GdprDepersonalizationOutput;
import com.amazon.gdpr.util.GdprException;
import com.amazon.gdpr.util.GlobalConstants;
import com.amazon.gdpr.util.SqlQueriesConstant;

/****************************************************************************************
 * This Configuration handles the Reading of SALESFORCE.GDPR_DEPERSONALIZATION__C table 
 * and Writing into GDPR.GDPR_DEPERSONALIZATION
 ****************************************************************************************/
@EnableScheduling
@EnableBatchProcessing
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@Configuration
public class GdprDepersonalizationBatchConfig {
	
	private static String CURRENT_CLASS		 		= GlobalConstants.CLS_GDPR_DEPERSONALIZATION_BATCH_CONFIG;
		
	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	public DataSource dataSource;

	@Autowired
	public HvhOutputDaoImpl hvhOutputDaoImpl; 
	
	@Autowired
	GdprOutputDaoImpl gdprOutputDaoImpl;
	
	@Autowired
	GdprInputDaoImpl gdprInputDaoImpl;
	
	@Bean
	public JdbcCursorItemReader<GdprDepersonalizationInput> reader(){
		String CURRENT_METHOD = "reader";		
		System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+" :: Inside method. ");
		
		JdbcCursorItemReader<GdprDepersonalizationInput> reader = new JdbcCursorItemReader<GdprDepersonalizationInput>();
		reader.setDataSource(dataSource);
		reader.setSql(SqlQueriesConstant.GDPR_DEPERSONALIZATION_FETCH);
		reader.setRowMapper(new GdprDepersonalizationInputRowMapper());		
		return reader;		
	}
	
	//To set values into GdprDepersonalizationInput Object
	public class GdprDepersonalizationInputRowMapper implements RowMapper<GdprDepersonalizationInput> {
		private String CURRENT_CLASS		 		= GlobalConstants.CLS_GDPRDEPERSONALIZATIONINPUTROWMAPPER;
				
		@Override
		public GdprDepersonalizationInput mapRow(ResultSet rs, int rowNum) throws SQLException {
			// TODO Auto-generated method stub
			String candidateId = rs.getString("CANDIDATE__C"); 
			String candidateOrApplicationId = (candidateId != null && ! (GlobalConstants.EMPTY_STRING.equalsIgnoreCase(candidateId.trim()))) ? 
					candidateId.trim() : rs.getString("BGC_Application__c");
			return new GdprDepersonalizationInput(
					candidateOrApplicationId, rs.getString("CATEGORY__C"), rs.getString("COUNTRY_CODE__C"), rs.getString("BGC_STATUS__C"),
					rs.getString("PH_AMAZON_ASSESSMENT_STATUS__C"), rs.getString("PH_CANDIDATE_PROVIDED_STATUS__C"), 
					rs.getString("PH_MASTER_DATA_STATUS__C"), rs.getString("PH_WITH_CONSENT_STATUS__C"));			
		}
	}
	
	//@Scope(value = "step")
	public class GdprDepersonalizationProcessor implements ItemProcessor<GdprDepersonalizationInput, List<GdprDepersonalizationOutput>>{
		private String CURRENT_CLASS		 		= GlobalConstants.CLS_GDPRDEPERSONALIZATIONINPUTROWMAPPER;
		private Map<String, String> categoryMap = null; 
		private Map<String, String> fieldCategoryMap = null;
		
		@BeforeStep
		public void beforeStep(final StepExecution stepExecution) throws GdprException {
			String CURRENT_METHOD = "beforeStep";		
			System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+" :: Inside method. ");
		    
			categoryMap = gdprInputDaoImpl.fetchCategoryDetails();
			Map<String, String> fieldCategoryMap = new HashMap<String, String>();
			fieldCategoryMap.put("bgcStatus", "2");
			fieldCategoryMap.put("amazonAssessmentStatus", "3");
			fieldCategoryMap.put("candidateProvidedStatus", "4");
			fieldCategoryMap.put("masterDataStatus", "5");
			fieldCategoryMap.put("withConsentStatus", "6");				
			
			JobParameters jobParameters = stepExecution.getJobParameters();
			long runId	= jobParameters.getLong(GlobalConstants.JOB_REORGANIZE_INPUT_RUNID);
			System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+" :: runId "+runId);
			
		    System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+" :: categoryMap "+categoryMap.toString());
		    System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+" :: fieldCategoryMap "+fieldCategoryMap.toString());
		}
				
		@Override
		public List<GdprDepersonalizationOutput> process(GdprDepersonalizationInput gdprDepersonalizationInput) throws Exception {
			String CURRENT_METHOD = "process";		
			System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+" :: Inside method. ");
			
			if(categoryMap == null)
				categoryMap = gdprInputDaoImpl.fetchCategoryDetails();
			if(fieldCategoryMap == null){
				fieldCategoryMap = new HashMap<String, String>();
				fieldCategoryMap.put("bgcStatus", "2");
				fieldCategoryMap.put("amazonAssessmentStatus", "3");
				fieldCategoryMap.put("candidateProvidedStatus", "4");
				fieldCategoryMap.put("masterDataStatus", "5");
				fieldCategoryMap.put("withConsentStatus", "6");				
			}
			
			List<GdprDepersonalizationOutput> lstGdprDepersonalizationOutput = new ArrayList<GdprDepersonalizationOutput>();
			List<String> fieldCategoryList = new ArrayList<String>(fieldCategoryMap.keySet());
			
			for(String fieldCategory : fieldCategoryList){
				
				Field field = GdprDepersonalizationInput.class.getDeclaredField(fieldCategory);
				String fieldValue = (String) field.get(gdprDepersonalizationInput);
				//System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+" :: fieldCategory : fieldValue "+fieldCategory+" : "+fieldValue);
				if(GlobalConstants.STATUS_CLEARED.equalsIgnoreCase(fieldValue)){
					GdprDepersonalizationOutput gdprDepersonalizationOutput = new GdprDepersonalizationOutput(gdprDepersonalizationInput.getCandidate(),
						Integer.parseInt(fieldCategoryMap.get(fieldCategory)), gdprDepersonalizationInput.getCountryCode(), fieldValue, 
						GlobalConstants.STATUS_SCHEDULED);
					lstGdprDepersonalizationOutput.add(gdprDepersonalizationOutput);
				}
			}
			hvhOutputDaoImpl.batchInsertGdprDepersonalizationOutput(lstGdprDepersonalizationOutput);
			return lstGdprDepersonalizationOutput;
		}
	}
	
	public class HvhOutputWriter<T> implements ItemWriter<GdprDepersonalizationOutput> { 
		private String CURRENT_CLASS		 		= GlobalConstants.CLS_WRITER;
		
		private final HvhOutputDaoImpl hvhOutputDaoImpl;

		public HvhOutputWriter(HvhOutputDaoImpl hvhOutputDaoImpl) {
			this.hvhOutputDaoImpl = hvhOutputDaoImpl;
		}
					
		@Override
		public void write(List<? extends GdprDepersonalizationOutput> lstGdprDepersonalizationOutput) throws Exception {
			String CURRENT_METHOD = "write";
			System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+" :: Inside method. ");
			//hvhOutputDaoImpl.batchInsertGdprDepersonalizationOutput(lstGdprDepersonalizationOutput);			
			//System.out.println("end of call:: "+LocalTime.now());			
		}		
	}
				
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public Step gdprDepersonalizationStep() {
		return stepBuilderFactory.get("gdprDepersonalizationStep")
				.<GdprDepersonalizationInput, List<GdprDepersonalizationOutput>> chunk(SqlQueriesConstant.BATCH_ROW_COUNT)
				.reader(reader())
				.processor(new GdprDepersonalizationProcessor())
				.writer(new HvhOutputWriter(hvhOutputDaoImpl))				
				.build();		
	}
	
	@Bean
	public Job processGdprDepersonalizationJob() {		
		return jobBuilderFactory.get("processGdprDepersonalizationJob")
								.incrementer(new RunIdIncrementer()).listener(listener(GlobalConstants.JOB_REORGANIZE_HVH_INPUT_PROCESSOR))										
								.flow(gdprDepersonalizationStep())
								.end()
								.build();
	}

	@Bean
	public JobExecutionListener listener(String jobRelatedName) {
	//public JobExecutionListener listener(HvhOutputDaoImpl hvhOutputDaoImpl, String jobRelatedName) {
		//return new JobCompletionListener(hvhOutputDaoImpl, jobRelatedName);
		return new JobCompletionListener(jobRelatedName);
	}
	
}
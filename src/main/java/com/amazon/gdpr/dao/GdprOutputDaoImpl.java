package com.amazon.gdpr.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.amazon.gdpr.model.gdpr.input.AnonymizationDetail;
import com.amazon.gdpr.model.gdpr.output.RunErrorMgmt;
import com.amazon.gdpr.model.gdpr.output.RunSummaryMgmt;
import com.amazon.gdpr.model.gdpr.output.SummaryData;
import com.amazon.gdpr.util.GlobalConstants;
import com.amazon.gdpr.util.SqlQueriesConstant;

/****************************************************************************************
 * The DAOImpl file fetches the JDBCTemplate created in the DatabaseConfig and 
 * Connects with the schema to fetch the Output table rows 
 ****************************************************************************************/
@Transactional
@Repository
public class GdprOutputDaoImpl {
	
	private static String CURRENT_CLASS		 		= GlobalConstants.CLS_GDPROUTPUTDAOIMPL;
	
	@Autowired
	@Qualifier("gdprJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * This method inserts the RunAnonymizationMapping values 
	 * @param lstImpactField
	 * @return
	 */	
	public int batchInsertRunAnonymizeMapping(long runId) {
		String CURRENT_METHOD = "fetchCategoryDetails";		
		System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+":: Inside method");
		
		return jdbcTemplate.update(SqlQueriesConstant.RUN_ANONYMIZATION_INSERT, new Object[]{runId});
	}
	
	/**
	 * This method is called whenever there is an error 
	 * The error details are loaded in this method
	 * @param runErrorMgmt 
	 */
	public void loadErrorDetails(RunErrorMgmt runErrorMgmt) {
		String CURRENT_METHOD = "initiateNewRun";		
		System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+":: Inside method");
		
		jdbcTemplate.update(SqlQueriesConstant.RUN_ERROR_MGMT_INSERT, new Object[]{runErrorMgmt.getErrorId(), runErrorMgmt.getErrorSummary(), 
				runErrorMgmt.getErrorDetail()});	
		
	}
	
	public void loadSummaryDataProcessor(long runId) {
		
	}
	
	
	/**
	 * Fetches the Summary Details rows
	 * @return List<SummaryData>
	 */
	public List<SummaryData> fetchSummaryDetails(long runId){
		String CURRENT_METHOD = "fetchSummaryDetails";		
		System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+":: Inside method");
		
		@SuppressWarnings("unchecked")
		List<SummaryData> lstSummaryData = jdbcTemplate.query(SqlQueriesConstant.SUMMARY_DATA_FETCH, new Object[]{runId}, new SummaryDataRowMapper());	
		return lstSummaryData;		
	}
	
	/****************************************************************************************
	 * This rowMapper converts the row data from IMPACTTABLE table to ImpactTable Object 
	 ****************************************************************************************/
	@SuppressWarnings("rawtypes")
	class SummaryDataRowMapper implements RowMapper {
		private String CURRENT_CLASS = GlobalConstants.CLS_SUMMARYDATAROWMAPPER;
		
		/* 
		 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
		 */
		@Override
		public SummaryData mapRow(ResultSet rs, int rowNum) throws SQLException {
			String CURRENT_METHOD = "mapRow";		
			//System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+":: Inside method");
									
			return new SummaryData(rs.getInt("CATEGORY_ID"), rs.getString("REGION"), rs.getString("COUNTRY_CODE"),   
					rs.getString("IMPACT_TABLE_NAME"), 	rs.getString("IMPACT_FIELD_NAME"), rs.getString("IMPACT_FIELD_TYPE"), 
					rs.getString("TRANSFORMATION_TYPE"), rs.getInt("IMPACT_TABLE_ID"));
		}
	}
	
	/**
	 * This method inserts the RunSummaryMgmt values 
	 * @param lstRunSummaryMgmt
	 * @return
	 */
	@Transactional
	public void insertRunSummaryMgmt(List<RunSummaryMgmt> lstRunSummaryMgmt) {
		String CURRENT_METHOD = "insertRunSummaryMgmt";
		System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+":: Inside method");
		
		jdbcTemplate.batchUpdate(SqlQueriesConstant.RUN_SUMMARY_MGMT_INSERT, new BatchPreparedStatementSetter() {

	        @Override
	        public void setValues(PreparedStatement ps, int i) throws SQLException {
	        	RunSummaryMgmt runSummaryMgmt = lstRunSummaryMgmt.get(i);
	        	ps.setObject(1, runSummaryMgmt.getRunId());
				ps.setInt(2, runSummaryMgmt.getCategoryId());
				ps.setString(3, runSummaryMgmt.getRegion());
				ps.setString(4, runSummaryMgmt.getCountryCode());
				ps.setInt(5, runSummaryMgmt.getImpactTableId());
				ps.setString(6, runSummaryMgmt.getBackupQuery());
				ps.setString(7, runSummaryMgmt.getDepersonalizationQuery());
	        }
	        @Override
	        public int getBatchSize() {
	            return lstRunSummaryMgmt.size();
	        }
	    });		
	}	
	
	/**
	 * This method inserts the RunSummaryMgmt values 
	 * @param lstRunSummaryMgmt
	 * @param batchSize
	 */
	@Transactional
	public void batchInsertRunSummaryMgmt(List<RunSummaryMgmt> lstRunSummaryMgmt, int batchSize) {
		String CURRENT_METHOD = "batchInsertRunSummaryMgmt";
		System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+":: Inside method");
		
		int[][] insertCounts = jdbcTemplate.batchUpdate(SqlQueriesConstant.RUN_SUMMARY_MGMT_INSERT, lstRunSummaryMgmt, batchSize, 
						new ParameterizedPreparedStatementSetter<RunSummaryMgmt>() {
			public void setValues(PreparedStatement ps, RunSummaryMgmt runSummaryMgmt) throws SQLException {				
				ps.setObject(1, runSummaryMgmt.getRunId());
				ps.setInt(2, runSummaryMgmt.getCategoryId());
				ps.setString(3, runSummaryMgmt.getRegion());
				ps.setString(4, runSummaryMgmt.getCountryCode());
				ps.setInt(5, runSummaryMgmt.getImpactTableId());
				ps.setString(6, runSummaryMgmt.getBackupQuery());
				ps.setString(7, runSummaryMgmt.getDepersonalizationQuery());								
			}
		});
		System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+":: insertCounts"+insertCounts);
	}
}
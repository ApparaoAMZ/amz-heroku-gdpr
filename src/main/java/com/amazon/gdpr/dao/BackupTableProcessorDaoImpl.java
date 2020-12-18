package com.amazon.gdpr.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.amazon.gdpr.model.gdpr.input.ImpactTableDetails;
import com.amazon.gdpr.model.gdpr.output.BackupTableDetails;
import com.amazon.gdpr.util.GlobalConstants;
import com.amazon.gdpr.util.SqlQueriesConstant;

/****************************************************************************************
 * The DAOImpl file fetches the JDBCTemplate created in the DatabaseConfig and
 * Connects with the schema to fetch the Impact table and parent table rows
 * Connects with the schema to fetch the backup table rows
 ****************************************************************************************/
@Transactional
@Repository
public class BackupTableProcessorDaoImpl {

	private static String CURRENT_CLASS = GlobalConstants.CLS_BACKUPTABLEPROCESSORDAOIMPL;

	@Autowired
	@Qualifier("gdprJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	/**
	 * Fetches the BackupTableDetails Table rows
	 * 
	 * @return List of BackupTable Details
	 */
	public List<BackupTableDetails> fetchBackupTableDetails() {
		String CURRENT_METHOD = "fetchBackupTableColumn";
		System.out.println(CURRENT_CLASS + " ::: " + CURRENT_METHOD + ":: Inside method");
		List<BackupTableDetails> lstBackupTableDetails = jdbcTemplate.query(SqlQueriesConstant.BACKUPTABLE_COLUMNS_QRY,
				new BackupTableDetailsRowMapper());
		return lstBackupTableDetails;
	}

	/**
	 * This method alters existing table to add new impacted column
	 * 
	 * @param query
	 * @return
	 */
	public void alterBackupTable(String query) {
		String CURRENT_METHOD = "alterBackupTable";
		System.out.println(CURRENT_CLASS + " ::: " + CURRENT_METHOD + ":: Inside method");
		jdbcTemplate.update(query);
		//System.out.println("query query:::" + query);
	}

	/**
	 * This method is to Truncate backup tables
	 * 
	 * @param list of lstBackupTableDetails
	 * @return true or false
	 */
	public Boolean refreshBackupTables(List<ImpactTableDetails> lstImpactTableDetails) {
		String CURRENT_METHOD = "refreshBackupTables";
		Boolean bkpupRefreshStatus = true;
		System.out.println(CURRENT_CLASS + " ::: " + CURRENT_METHOD + ":: Inside method");
		Set<String> setBackupTables = new HashSet();
		setBackupTables = getBackupTableList(lstImpactTableDetails);
		//System.out.println("setBackupTables truncate" + setBackupTables);
		// Convert the Set of String to String
		String lstBkpTables = String.join(", ", setBackupTables);

		// Print the comma separated String
		/*
		 * System.out.println("Comma separated String: " + string); int backupTableLnth
		 * = setBackupTables.size(); for (String backupTable : setBackupTables) {
		 */

		try {

			if (setBackupTables != null && !setBackupTables.isEmpty()) {

				String truncateTableSQL = "DROP TABLE IF EXISTS " + lstBkpTables + ";";
				//System.out.println("before truncate" + truncateTableSQL);
				jdbcTemplate.execute(truncateTableSQL);
				//System.out.println("After truncate" + truncateTableSQL);
				
			}
			bkpupRefreshStatus = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			bkpupRefreshStatus = false;
			e.printStackTrace();
		}

		// }
		return bkpupRefreshStatus;
	}

	/****************************************************************************************
	 * This rowMapper converts the row data from BackupTable table to
	 * BackupTableDetails Object
	 ****************************************************************************************/
	@SuppressWarnings("rawtypes")
	class BackupTableDetailsRowMapper implements RowMapper {
		private String CURRENT_CLASS = GlobalConstants.CLS_BACKUPTABLEDETAILSROWMAPPER;

		/*
		 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
		 */
		@Override
		public BackupTableDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
			String CURRENT_METHOD = "mapRow";
			// System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+":: Inside method");

			return new BackupTableDetails(rs.getString("table_name"), rs.getString("column_name"));
		}
	}

	/**
	 * PreparingListofBackupTable Names
	 * 
	 * @return List of BackupTable Names
	 */
	public Set getBackupTableList(List<ImpactTableDetails> lstImpactTableDetails) {
		String CURRENT_METHOD = "getBackupTableList";
		System.out.println(CURRENT_CLASS + " ::: " + CURRENT_METHOD + ":: Inside method");
		Set lstBackupTables = new HashSet();
		int backupTableLnth = lstImpactTableDetails.size();
		for (int i = 0; i < backupTableLnth; i++) {
			String backupTableName = lstImpactTableDetails.get(i).getImpactTableName();

			lstBackupTables.add("GDPR.BKP_" + backupTableName);

		}

		return lstBackupTables;
	}

}
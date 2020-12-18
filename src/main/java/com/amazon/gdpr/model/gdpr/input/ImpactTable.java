package com.amazon.gdpr.model.gdpr.input;

public class ImpactTable {

	int impactTableId;
	String impactTableName;
	String parentTable;
	String impactTableColumn;
	String parentTableColumn;
		
	/**
	 * @param impactTableId
	 * @param impactTableName
	 * @param parentTable
	 * @param impactColumnMapped
	 * @param parentColumnMapped
	 */
	public ImpactTable(int impactTableId, String impactTableName, String parentTable, String impactTableColumn,
			String parentTableColumn) {
		super();
		this.impactTableId = impactTableId;
		this.impactTableName = impactTableName;
		this.parentTable = parentTable;
		this.impactTableColumn = impactTableColumn;
		this.parentTableColumn = parentTableColumn;
	}
	/**
	 * @return the impactTableId
	 */
	public int getImpactTableId() {
		return impactTableId;
	}
	/**
	 * @param impactTableId the impactTableId to set
	 */
	public void setImpactTableId(int impactTableId) {
		this.impactTableId = impactTableId;
	}
	/**
	 * @return the impactTableName
	 */
	public String getImpactTableName() {
		return impactTableName;
	}
	/**
	 * @param impactTableName the impactTableName to set
	 */
	public void setImpactTableName(String impactTableName) {
		this.impactTableName = impactTableName;
	}
	/**
	 * @return the parentTable
	 */
	public String getParentTable() {
		return parentTable;
	}
	/**
	 * @param parentTable the parentTable to set
	 */
	public void setParentTable(String parentTable) {
		this.parentTable = parentTable;
	}
	/**
	 * @return the impactColumnMapped
	 */
	public String getimpactTableColumn() {
		return impactTableColumn;
	}
	/**
	 * @param impactColumnMapped the impactColumnMapped to set
	 */
	public void setimpactTableColumn(String impactTableColumn) {
		this.impactTableColumn = impactTableColumn;
	}
	/**
	 * @return the parentColumnMapped
	 */
	public String getparentTableColumn() {
		return parentTableColumn;
	}
	/**
	 * @param parentTableColumn the parentTableColumn to set
	 */
	public void setparentTableColumn(String parentTableColumn) {
		this.parentTableColumn = parentTableColumn;
	}
	
}
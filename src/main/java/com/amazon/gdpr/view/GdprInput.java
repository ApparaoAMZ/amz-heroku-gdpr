package com.amazon.gdpr.view;

/**
 * @author susadhan
 *
 */
public class GdprInput {
	
	private String runName;
	private String runStatus;
	
	/**
	 * @return the runName
	 */
	public String getRunName() {
		return runName;
	}

	/**
	 * @param runName the runName to set
	 */
	public void setRunName(String runName) {
		this.runName = runName;
	}

	/**
	 * This status will be forwarded to the UI and displayed
	 * @return the status displaying the success or failure of the run
	 */
	public String getRunStatus() {
		return runStatus;
	}

	/**
	 * This form detail will be procesed and the status will be set in the processing page
	 * @param runStatus 
	 */
	public void setRunStatus(String runStatus) {
		this.runStatus = runStatus;
	}	
}
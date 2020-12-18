package com.amazon.gdpr.batch;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

import com.amazon.gdpr.util.GlobalConstants;

public class JobCompletionListener extends JobExecutionListenerSupport {
	private static String CURRENT_CLASS		 		= GlobalConstants.CLS_JOBCOMPLETIONLISTENER;
	String jobRelatedName = "";
	
	public JobCompletionListener(String jobRelatedName) {
		this.jobRelatedName = jobRelatedName;
	}
	
	@Override
	public void afterJob(JobExecution jobExecution) {		
		String CURRENT_METHOD = "afterJob";		
		System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+" :: Inside method");
		
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+" :: "+jobRelatedName+ " BATCH JOB COMPLETED SUCCESSFULLY");
		}
	}
}
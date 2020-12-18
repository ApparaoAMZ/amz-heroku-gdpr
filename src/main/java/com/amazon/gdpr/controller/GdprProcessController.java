package com.amazon.gdpr.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.amazon.gdpr.model.gdpr.output.RunSummaryMgmt;
import com.amazon.gdpr.service.BackupService;
import com.amazon.gdpr.service.InitService;
import com.amazon.gdpr.service.ReOrganizeInputService;
import com.amazon.gdpr.util.GlobalConstants;
import com.amazon.gdpr.view.GdprInput;

/****************************************************************************************
 * This controller in the main initiator. 
 * This will be invoked by the Heroku UI or the scheduler
 ****************************************************************************************/
@Controller
public class GdprProcessController {

	private static String CURRENT_CLASS		 		= GlobalConstants.CLS_GDPRPROCESSCONTROLLER;
		
	long runId = 0;
	Map<String, RunSummaryMgmt> mapRunSummaryMgmt = null;
	
	@Autowired
	BackupService backpupService;
	
	@Autowired
	InitService initService;
	
	@Autowired
	ReOrganizeInputService reOrganizeInputService;
	
	/**
	 * This method is invoked when GDPR Initiate link is clicked
	 * This initializes the objects and loads the gdprInitiate page
	 * @param model
	 * @return The gdprInput page
	 */
	@GetMapping("/gdprInput")
	public String gdprInputForm(Model model) {
		String CURRENT_METHOD = "gdprInputForm";		
		System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+" :: Inside method");
		model.addAttribute("gdprInput", new GdprInput());
		/* Verify the current status of the run from DB and display it */
		return GlobalConstants.TEMPLATE_GDPRINPUT;		
	}
	
	/**
	 * This method is invoked when Submit button in the GDPR Depersonalization Input Page is clicked
	 * This initiates the Depersonalization activity
	 * @param runName - Input to maintain the run information
	 * @return String - The gdprInput page
	 */
	@PostMapping("/gdprSubmit")
	public String herokuDepersonalization(@ModelAttribute("gdprInput") GdprInput gdprInput, Model model) {
		String CURRENT_METHOD = "herokuDepersonalization";		
		System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+" :: Inside method. ");
		
		// Init Service initiated
		String initServiceStatus =  initService.initService(gdprInput.getRunName());
		runId = initService.getRunId();	
		//reOrganizeInputService.reOrganizeInputService(runId);
		//reOrganizeInputService.reOrganizeData(runId);
		gdprInput.setRunStatus(initServiceStatus);
		model.addAttribute(GlobalConstants.ATTRIBUTE_GDPRINPUT, gdprInput);
		return GlobalConstants.TEMPLATE_GDPRINPUT;
	}
			

	
	/**
	 * @param runId
	 * @param runSummaryMgmtMap
	 * @return
	 */
	public Map<String, RunSummaryMgmt> backupInitialize(long runId, Map<String, RunSummaryMgmt> runSummaryMgmtMap) {
		String CURRENT_METHOD = "backupInitialize";
		System.out.println(CURRENT_CLASS+" ::: "+CURRENT_METHOD+":: Inside method");
				
		runSummaryMgmtMap = backpupService.backupService(runId, runSummaryMgmtMap);
		return null;
	}	
}
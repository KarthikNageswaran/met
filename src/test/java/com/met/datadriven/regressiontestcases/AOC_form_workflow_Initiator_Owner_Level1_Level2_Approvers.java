package com.met.datadriven.regressiontestcases;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.met.datadriven.generic.keywords;
import com.met.datadriven.util.ExtentManager;
import com.met.datadriven.util.constants;
import com.met.datadriven.util.reading_xls;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import jxl.read.biff.BiffException;

@SuppressWarnings("unused")
public class AOC_form_workflow_Initiator_Owner_Level1_Level2_Approvers{
	
	static String sheetName="Area of Compliance";
	keywords app;
	ExtentReports ex;
	ExtentTest test;
	WebDriver driver;
	@BeforeMethod(alwaysRun=true)
	public void browser_url_access() throws IOException{
		ex=ExtentManager.getInstance();
		test=ex.startTest(sheetName);
		app=new keywords(test);
		
		try {
			if(driver==null){
				app.openBrowser("chrome");
				app.navigate("url");	
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test(dataProvider="getData",groups={"regression"})
	public void areaOfComplianceAllData(
										String loginName,String password,String name,String description,String types,
										String validFrom,String validUntil,String ownerOrganizations,String owners,
										String level1approver,String level2approver,String restrictAccessTo,String rel_aoc,
										String rel_ctl,String rel_fwr,String rel_fun,String rel_prod,String rel_ref,
										String rel_regb,String rel_req,String rel_rsk,String rel_std,String status,
										String createdby,String attach_files,String wsi_Initator,String initator_comments,String loginOwner,
										String OwnerStatus,String wsi_owner,String loginApprover1,String approver1Status,
										String wsi_l1approver,String loginApprover2,String wsi_l2approver,String approver2Status
										) throws IOException, InterruptedException{
		
		
		//login to application with user name and password
		app.login(loginName, password);
		
		//Open the form
		app.openForm(sheetName);
		
		//form title
		app.verifyFormTitle(sheetName);
		
		// -------------------- Initiator Stage Verification ---------------------
		test.log(LogStatus.INFO, "Enter data in name field");
		app.enterValueInFormTextField("AOCname_xpath", name);
		app.enterValueInRTF("aoc_description_xpath", description);
		app.selectValueInDropdownMLOV("aoc_type_xpath", "aoc_type_values_xpath", types);
		app.setDate("aoc_validfrom_id",validFrom);
		app.setDate("aoc_validuntil_id",validUntil);
		app.SelectValueInMLOV("owner_org_xpath", ownerOrganizations);
		app.SelectValueInMLOV("owners_xpath", owners);
		app.selectValueInSLOV("level1_approver_xpath", level1approver);
		app.selectValueInSLOV("level2_approver_xpath", level2approver);
		app.selectValueInDropdown("restrict_access_xpath", restrictAccessTo);
		
		//relationship section
		test.log(LogStatus.INFO, "Adding area of compliance as relationship");
		app.addRelationship(rel_aoc);
		test.log(LogStatus.INFO, "Adding standard as relationship");
		app.addRelationship(rel_std);
		
		//additional details section
		test.log(LogStatus.INFO, "Verify the status data");
		app.verifyTextInPage(status);
		test.log(LogStatus.INFO, "Verify created by data");
		app.verifyTextInPage(createdby);
		test.log(LogStatus.INFO, "attaching files");
		app.attachFile("attachFile_xpath", attach_files);
		
		//verify summary section
//		test.log(LogStatus.INFO, "verify types field data in summary section");
//		app.verifySummaryData(types);
		
		test.log(LogStatus.INFO, "verify status field data in summary section");
		app.verifySummaryData(status);
		//app.WSI(wsi_Initator);
		
		//actions
		test.log(LogStatus.INFO, "Saving the form");
		app.clickOnMenuButton("Save");
		test.log(LogStatus.INFO, "invoke the saved form from my tasks");
		app.invokeAssignmentInMyTasks(name);
		app.clickOnMenuButton("Send for Approval");
		test.log(LogStatus.INFO, "Submitting the form");
		app.enterValueInTextArea("aoc_popup_window_textarea_xpath",initator_comments);
		app.clickOnButtonInPopup("Submit");
		app.logout();
		
		// -------------------- Owner Stage Verification ---------------------
		test.log(LogStatus.INFO, "Login as owner");
		app.login(loginOwner, password);
		test.log(LogStatus.INFO, "Invoke data from my task section");
		app.invokeAssignmentInMyTasks(name);
		test.log(LogStatus.INFO, "Verify name field data");
		app.verifyDataInFormTextBox("AOCname_xpath",name);
		test.log(LogStatus.INFO, "Verify valid from field data");
		app.verifyDataInFormTextBox("aoc_validfrom_id", validFrom);
		test.log(LogStatus.INFO, "Verify owner organization field data");
		app.verifyDataInMLOV("owner_org_values_xpath", ownerOrganizations);
		test.log(LogStatus.INFO, "Verify owner status field in addtional details");
		app.verifyTextInPage(OwnerStatus);
		app.verifySummaryData(types);
		test.log(LogStatus.INFO, "Verify data in status field in summary section");
		app.verifySummaryData(OwnerStatus);
		test.log(LogStatus.INFO, "Verify workflow stage indicator");
		app.WSI(wsi_owner);
		app.clickOnMenuButton("Approve");
		test.log(LogStatus.INFO, "Submitting form");
		app.clickOnButtonInPopup("Submit");
		test.log(LogStatus.INFO, "logging out");
		app.logout();
		
		// -------------------- Level 1 Approver Stage Verification ---------------------
		test.log(LogStatus.INFO, "login as approver 1");
		app.login(loginApprover1, password);
		test.log(LogStatus.INFO, "Invoke data from my task section");
		app.invokeAssignmentInMyTasks(name);
		test.log(LogStatus.INFO, "Verify name field data");
		app.verifyDataInFormTextBox("AOCname_xpath",name);
		test.log(LogStatus.INFO, "Verify valid from field data");
		app.verifyDataInFormTextBox("aoc_validfrom_id", validFrom);
		test.log(LogStatus.INFO, "Verify owner organization field data");
		app.verifyDataInMLOV("owner_org_values_xpath", ownerOrganizations);
		test.log(LogStatus.INFO, "Verify owner status field in addtional details");
		app.verifyTextInPage(OwnerStatus);
		test.log(LogStatus.INFO, "Verify data in type field in summary section");
		app.verifySummaryData(types);
		test.log(LogStatus.INFO, "Verify data in status field in summary section");
		app.verifySummaryData(OwnerStatus);
		test.log(LogStatus.INFO, "Verify workflow stage indicator at level1 approver stage");
		app.WSI(wsi_l1approver);
		app.clickOnMenuButton("Approve");
		test.log(LogStatus.INFO, "Submitting form");
		app.clickOnButtonInPopup("Submit");
		test.log(LogStatus.INFO, "logging out");
		app.logout();
		
		// -------------------- Level 2 Approver Stage Verification ---------------------
		test.log(LogStatus.INFO, "login as approver1");
		app.login(loginApprover2, password);
		test.log(LogStatus.INFO, "Invoke data from my task section");
		app.invokeAssignmentInMyTasks(name);
		app.verifyDataInFormTextBox("AOCname_xpath",name);
		test.log(LogStatus.INFO, "Verify name field data");
		app.verifyDataInFormTextBox("AOCname_xpath",name);
		test.log(LogStatus.INFO, "Verify valid from field data");
		app.verifyDataInFormTextBox("aoc_validfrom_id", validFrom);
		test.log(LogStatus.INFO, "Verify owner organization field data");
		app.verifyDataInMLOV("owner_org_values_xpath", ownerOrganizations);
		test.log(LogStatus.INFO, "Verify owner status field in addtional details");
		app.verifyTextInPage(OwnerStatus);
		test.log(LogStatus.INFO, "Verify data in type field in summary section");
		app.verifySummaryData(types);
		test.log(LogStatus.INFO, "Verify workflow stage indicator at level2 approver stage");
		app.WSI(wsi_l2approver);
		test.log(LogStatus.INFO, "click on approve button");
		app.clickOnMenuButton("Approve");
		test.log(LogStatus.INFO, "Submitting form");
		app.clickOnButtonInPopup("Submit");
		test.log(LogStatus.INFO, "logging out");
		app.logout();
		
	}
	
	@AfterMethod(alwaysRun=true)
	public void closing_browser(){
		if(ex!=null){
			ex.endTest(test);
			ex.flush();
		}
		driver=app.driver;
		if(driver!=null){
			driver.quit();
		}
	}
	
	
	@DataProvider
	public static Object[][] getData() throws BiffException, IOException{
			return reading_xls.readData(constants.regressionforms_filePath, sheetName);
	}
	
}

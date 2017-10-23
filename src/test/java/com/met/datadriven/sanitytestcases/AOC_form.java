package com.met.datadriven.sanitytestcases;

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


public class AOC_form {
	
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
		test.log(LogStatus.INFO, "starting"+sheetName+"test");
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
	
	
	@Test(dataProvider="getData",groups={"sanity"})
	public void areaOfComplianceAllData(String LoginName,String Password,String Name,String Description,String Types
			                            ,String OwnerOrganizations,String Owners,String level1approver,String level2approver
			                            ,String restrictAccessTo) throws IOException, InterruptedException{
		
		
		//login to application with user name and password
		test.log(LogStatus.INFO, "Login page");
		app.login(LoginName, Password);
		
		
		//Open the form
		test.log(LogStatus.INFO, "Opening form");
		app.openForm(sheetName);
		
		//form data
		test.log(LogStatus.INFO, "entering data name field ");
		app.enterValueInFormTextField("AOCname_xpath", Name);
		
		test.log(LogStatus.INFO, "entering data in organization field ");
		app.SelectValueInMLOV("owner_org_xpath", OwnerOrganizations);
		
		test.log(LogStatus.INFO, "entering data in owners field ");
		app.SelectValueInMLOV("owners_xpath", Owners);
		
		test.log(LogStatus.INFO, "selecting data in restrict access to field ");
		app.selectValueInDropdown("restrict_access_xpath", restrictAccessTo);
		
		test.log(LogStatus.INFO, "submitting form");
		app.clickOnMenuButton("Send for Approval");
		app.clickOnButtonInPopup("Submit");
		
		test.log(LogStatus.INFO, "logging out");
		app.logout();
	}
	
	@AfterMethod (alwaysRun=true)
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
			return reading_xls.readData(constants.sanityforms_filePath, sheetName);
	}
	
}

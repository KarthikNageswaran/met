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


public class AssetClass_form{

	static String sheetName="Asset Class";
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
		
		if(driver==null){
			app.openBrowser("chrome");
			app.navigate("url");	
		}
	}
	
	
	@Test(dataProvider="getData",groups={"sanity"})
	public void assetclass_sanity_form(String LoginName,String Password,String Name,String Hierarchy,String OwnerOrganizations,String Owners,String restrictAccessTo) throws IOException, InterruptedException{
		
//		test.log(LogStatus.INFO, sheetName+"test started");
		//login to application with user name and password
		app.login(LoginName, Password);
		
		//Open the form
		app.openForm(sheetName);
		
		//form data
		app.enterValueInFormTextField("assetclass_name_xpath", Name);
		app.selectValueInDropdown("assetclass_Hierarchy_xpath", Hierarchy);
		app.SelectValueInMLOV("owner_org_xpath", OwnerOrganizations);
		app.SelectValueInMLOV("owners_xpath", Owners);
		app.selectValueInDropdown("restrict_access_xpath", restrictAccessTo);
		app.clickOnMenuButton("Send for Approval");
		app.clickOnButtonInPopup("Submit");
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
			return reading_xls.readData(constants.sanityforms_filePath, sheetName);
	}
	
}

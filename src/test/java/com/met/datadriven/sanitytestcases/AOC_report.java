package com.met.datadriven.sanitytestcases;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
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

public class AOC_report{

	static String sheetName="Areas of Compliance";
	keywords app;
	public ExtentReports ex;
	public ExtentTest test;
	WebDriver driver;
	
	@BeforeMethod(alwaysRun=true)
	public void openbrowser_url() throws IOException{
		ex=ExtentManager.getInstance();
		test=ex.startTest(sheetName);
		app=new keywords(test);
		test.log(LogStatus.INFO, "starting"+sheetName+"test");
		
		if(driver==null){
			app.openBrowser("chrome");
			app.navigate("url");
		}
	}
		
	@Test(dataProvider="getData",groups="sanity",dependsOnMethods={"com.met.datadriven.sanitytestcases.AOC_form.areaOfComplianceAllData"})
	public void aoc_report_validation(String LoginName,String Password,String name) throws InterruptedException, IOException{
		
		//login to application with user name and password
		app.login(LoginName, Password);
		
		//Open the report
		app.openReport(sheetName);
		
		//filter report
		app.reportFilter("report_filter_name_xpath", name);
		app.reportDataVerify("report_name_column_xpath", name);
		app.logout();
	}
	
	@AfterMethod
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
			return reading_xls.readData(constants.sanityreports_filepath, sheetName);
	}
}

package com.met.datadriven.regressiontestcases;

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

public class AOC_DataBrowser_Validation {

	
	static String sheetName="Areas of Compliance";
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
										String Orphan,String Created_in_the_last_No_of_Days,String Expiring_in_the_next_No_of_Days,
										String Expired_in_the_last_No_of_Days,String Expired_After,String Expired_Before,
										String validFrom,String validUntil,String ownerOrganizations,String owners,
										String level1approver,String level2approver,String rel_aoc,
										String rel_ctl,String rel_fwr,String rel_fun,String rel_prod,String rel_ref,
										String rel_regb,String rel_req,String rel_rsk,String rel_std
										) throws IOException, InterruptedException{
		
		
		//login to application with user name and password
		app.login(loginName, password);
		
		//Open the data browser
		app.openDataBrowser(sheetName);
		app.showFiltersInDataBrowser();
		app.enterDataInDataBrowserFilters("aoc_name_databrowser_filter_xpath",name,"TEXTBOX");
		app.clickOnDataInDatabrowser(name);
		
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
			return reading_xls.readData(constants.regressiondatabrowser_filePath, sheetName);
	}

}

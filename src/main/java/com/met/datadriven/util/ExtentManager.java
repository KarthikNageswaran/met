package com.met.datadriven.util;

import java.io.File;
import java.util.Date;

import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;

public class ExtentManager {
	 private static ExtentReports extent;
	 
	 public static ExtentReports getInstance() {
		 if(extent==null) {
			 
			 Date d=new Date();
			 String filename=d.toString().replace(":","_").replace(" ","_")+".html";
			 extent=new ExtentReports(constants.reportpath+filename,true,DisplayOrder.NEWEST_FIRST);
			 
			 extent.loadConfig(new File(System.getProperty("user.dir")+"\\ReportsConfig.xml"));
			 
			 
		 }return extent;
		 
	 }



	}
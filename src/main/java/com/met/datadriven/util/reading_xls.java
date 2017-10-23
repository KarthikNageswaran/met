package com.met.datadriven.util;

import java.io.FileInputStream;
import java.io.IOException;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class reading_xls {
	public static Object[][] readData(String filePath,String sheetName) throws BiffException, IOException{
		//	String sheetName="LoginValidData";
	    //FileInputStream in=new FileInputStream(System.getProperty("user.dir")+"\\Data\\LoginTest.xls");
//		String sheetName="LoginValidData";
//		String filePath="E:\\RSKTesting\\Test\\Data\\LoginTest.xls";
		FileInputStream in=new FileInputStream(filePath);
		Workbook wb=Workbook.getWorkbook(in);
		Sheet sh=wb.getSheet(sheetName);
		
		int totalNoOfRows=sh.getRows();
		int totalNoOfColumns=sh.getColumns();
		Object[][] data=new Object[totalNoOfRows-1][totalNoOfColumns];
		int n=0;
		for(int i=1;i<totalNoOfRows;i++){
			
			for(int j=0;j<totalNoOfColumns;j++){
				data[n][j]=sh.getCell(j,i).getContents();
				System.out.println(data[n][j]);					
			}
			n++;
		}
		return data;		
	 }
}

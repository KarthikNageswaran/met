package com.met.datadriven.generic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.*;
import org.testng.asserts.SoftAssert;

import com.met.datadriven.util.constants;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

//@SuppressWarnings("unused")
public class keywords {
	public WebDriver driver = null;
//	public static WebDriver driver = null;
	Properties prop;
	Actions act;
	WebDriverWait wait;
	JavascriptExecutor jse;
	SoftAssert softassertion;
	ExtentTest test;

	public keywords(ExtentTest test) throws IOException{
		prop=new Properties();
		FileInputStream fs=new FileInputStream(constants.properties_filepath);
		prop.load(fs);
		softassertion=new SoftAssert();
		this.test=test;
//		this.driver=driver;
	}
	
	public void openBrowser(String browserName) throws IOException{
//		System.out.println("Browser opening");
		test.log(LogStatus.INFO, "Browser opening");
		try{
			
			if(browserName.equalsIgnoreCase("firefox")){
				driver=new FirefoxDriver();
			}else if(browserName.equalsIgnoreCase("chrome")){
				System.setProperty("webdriver.chrome.driver", constants.chromedriver_path);
				driver=new ChromeDriver();
			}else if(browserName.equalsIgnoreCase("ie")){
				System.setProperty("webdriver.ie.driver", constants.ieDriver_path);
				driver=new InternetExplorerDriver();
			}else{
				test.log(LogStatus.ERROR, "enterted browser name not matching");
//				System.out.println("enterted browser name not matching");
			}
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			test.log(LogStatus.INFO, "openBrowser > browser initializing success");
		}catch(Exception e){
//			System.out.println("browser initializing failed");
			test.log(LogStatus.FAIL, browserName+" --> browser initializing failed "+e.getMessage());
//			reportFailure("browser initializing failed");
		}
		
	}
	
	public void navigate(String url){
		try{
		driver.get(prop.getProperty(url));
		test.log(LogStatus.INFO, "navigate > navigated to url");
		}catch(Exception e){
			test.log(LogStatus.FAIL, url+" --> navigating to url failed ");
		}
//		System.out.println("navigate > navigated to url");
	}
	
	public void entervalueIntextBox(String locator,String data) throws InterruptedException, IOException{
		Thread.sleep(2000L);
		try{
		getElement(locator).sendKeys(data);
		}catch(Exception e){
			test.log(LogStatus.FAIL, locator+","+data+"--> unable to enter the value "+e.getMessage());
		}

	}

	public void clickOnButton(String locator) throws InterruptedException, IOException{
		Thread.sleep(4000L);
		try{
		getElement(locator).click();
		}catch(Exception e){
			test.log(LogStatus.FAIL, locator+"--> unable to click the element"+e.getMessage());
		}
	}
	
	public void clickOnLinkText(String data) throws InterruptedException, IOException{
		try{
		driver.findElement(By.partialLinkText(data)).click();
		Thread.sleep(4000L);
		}catch(Exception e){
//			System.out.println(data+" not found ----->"+e.getMessage());
			
			test.log(LogStatus.FAIL, data+"--> unable to click the element"+e.getMessage());
			reportFailure("unable to click the element/Element not found");
			
		}
		Thread.sleep(6000L);
	}
	
	
	public WebElement getElement(String locator) throws IOException {
		
		WebElement e=null;
		try{
			if(locator.endsWith("_id")){
				e=driver.findElement(By.id(prop.getProperty(locator)));
			}else if(locator.endsWith("_name")){
				e=driver.findElement(By.name(prop.getProperty(locator)));
			}else if(locator.endsWith("_xpath")){
				e=driver.findElement(By.xpath(prop.getProperty(locator)));
			}else{
//				System.out.println("locator naming conversion incorrect");
				test.log(LogStatus.ERROR, "locator naming conversion incorrect");
			}	
		}catch(Exception e1){
			
//			System.out.println(locator+"--"+"element not found"+e1.getMessage());
			test.log(LogStatus.FAIL, locator+"--> unable to find element"+e1.getMessage());
			reportFailure(locator+"--"+"element not found"+e1.getMessage());			
		}
	return e;		
	}
	
	
	//*************************************App Keywords***************************
	public void login(String username,String password) throws InterruptedException, IOException{
		
		try{
			driver.findElement(By.id("username")).sendKeys(username);
			driver.findElement(By.id("password")).sendKeys(password);
			Thread.sleep(1000L);
			driver.findElement(By.xpath("//*[@id='rightPanel']/div[3]/form/div/div[3]/button")).click();
			Thread.sleep(6000L);
			
		}catch(Exception e){
//			System.out.println("Login failed"+e.getMessage());
			test.log(LogStatus.FAIL, username+","+password+"--> username/Password incorrect"+e.getMessage());
			reportFailure("Login failed"+e.getMessage());
		}
	}

	public void openForm(String formLinkName) throws InterruptedException, IOException{
		Thread.sleep(4000L);
		try{
			act=new Actions(driver);
			act.moveToElement(getElement("home_id")).build().perform();
			Thread.sleep(4000L);
			driver.findElement(By.linkText("GRC Libraries")).click();
			Thread.sleep(4000L);
			driver.findElement(By.linkText("Manage")).click();
			Thread.sleep(1000L);
			getElement("forms_button_xpath").click();
			Thread.sleep(1000L);
			driver.findElement(By.linkText(formLinkName)).click();
			test.log(LogStatus.INFO, formLinkName+"--> form link clicked successfully");
			Thread.sleep(6000L);
		}catch(Exception e){
			test.log(LogStatus.FAIL, formLinkName+"--> unable to open the form");
			reportFailure("unable to perform on actions");
		}
	}
	
	public void enterValueInRTF(String locator,String data) throws InterruptedException{
		try{
			Thread.sleep(4000L);
			driver.findElement(By.xpath(prop.getProperty(locator))).click();
			// enter value 
			//save it
			Thread.sleep(1000L);
			driver.switchTo().frame("mce_0_ifr");
			driver.findElement(By.xpath("//*[@id='tinymce']")).sendKeys(data);
			Thread.sleep(1000L);
			driver.switchTo().defaultContent();
			driver.findElement(By.xpath("//*[@id='myRTF']/div/div/div[3]/button[2]")).click();
			Assert.assertEquals(driver.findElement(By.xpath("//*[@id='msiStatus']/div")).getText().trim(), "Saved successfully");
			driver.switchTo().activeElement();
			test.log(LogStatus.INFO, locator+","+data+" -->"+"enter value in rtf field");
			Thread.sleep(4000L);
		}catch(Exception e){
			test.log(LogStatus.FAIL, locator+","+data+" -->"+"unable to enter value in rtf field"+e.getMessage());
//			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	public void expandAndColapseSection(String locator) throws InterruptedException{
		driver.findElement(By.xpath(locator)).click();
		Thread.sleep(6000L);
	}
	
	public void enterValueInFormTextField(String locator,String data) throws InterruptedException{
//		jse = (JavascriptExecutor) driver;
//		WebElement inputField = driver.findElement(By.xpath(prop.getProperty(locator)));
//		jse.executeScript("arguments[0].setAttribute('value', '" +data+"')", inputField);
		try{
			Thread.sleep(2000L);
//			driver.findElement(By.xpath(prop.getProperty(locator))).sendKeys(data);
			getElement(locator).sendKeys(data);
			test.log(LogStatus.INFO, locator+","+data+"--> entered value in Form Text Field");
			Thread.sleep(2000L);
		}catch(Exception e){
//			System.out.println(e.getMessage());
			test.log(LogStatus.FAIL, locator+","+data+"--> unable to enter value in Form Text Field");
		}
	}
	
	
	public void selectValueInDropdownMLOV(String locator1,String locator2,String data) throws InterruptedException{
		List<WebElement> allelements=null;
		try{
			driver.findElement(By.xpath(prop.getProperty(locator1))).click();
		}catch(Exception e){
			test.log(LogStatus.FAIL, locator1+" --> unable to click an element"+e.getMessage());
		}
			Thread.sleep(1000L);
		try{
			allelements=driver.findElements(By.xpath(prop.getProperty(locator2)));
		}catch(Exception e){
			test.log(LogStatus.FAIL, locator2+" --> unable to extract the elements"+e.getMessage());
			System.out.println(e.getMessage());
		}
		if(data!=null){
			if(data.contains(",")){
				String[] str=data.split(",");
				for(int j=0;j<str.length;j++){
					for(int i=0;i<allelements.size();i++){
						try{	
							if((allelements.get(i).getText()).equals(str[j])){
								allelements.get(i).click();
								Thread.sleep(2000L);
							}
						}catch(Exception e){
							test.log(LogStatus.FAIL, allelements.get(i)+"--> element not found"+e.getMessage());
							System.out.println(e.getMessage());
						}
					}
				}
		    }
			act.sendKeys(Keys.ESCAPE).build().perform();
			Thread.sleep(1000L);
	   }
	}
	
	public void SelectValueInMLOV(String locator,String data) throws InterruptedException, IOException{
		if(data!=null){
				if(data.contains(",")){
						String[] str=data.split(",");
		//				driver.findElement(By.xpath(prop.getProperty(locator))).click();
						try{
						getElement(locator).click();
						}catch(NullPointerException e){
							System.out.println("getElement returns null.unable to perform actions");
						}catch(Exception e){
							System.out.println(e.getMessage());
							reportFailure(e.getMessage());
						}	
						for(int i=0;i<str.length;i++){
							try{
								Thread.sleep(2000L);
								getElement("searchbox_xpath").clear();
								getElement("searchbox_xpath").sendKeys(str[i]);
								getElement("searchbox_xpath").sendKeys(Keys.ENTER);
								Thread.sleep(2000L);
								getElement("checkbox_xpath").click();
							}catch(NullPointerException e){
//								System.out.println("getElement returns null.unable to perform actions");
								reportFailure("getElement returns null.unable to perform actions");
							}catch(Exception e){
//								System.out.println(e.getMessage());
								reportFailure(e.getMessage());
							}	
						}
						try{
							getElement("Done_button_id").click();
							Thread.sleep(4000L);
						}catch(NullPointerException e){
							System.out.println("getElement returns null.unable to perform actions");
						}catch(Exception e){
							System.out.println(e.getMessage());
						}	
					}
				else{
					try{
						getElement(locator).click();
						Thread.sleep(2000L);
						getElement("searchbox_xpath").sendKeys(data);
						getElement("searchbox_xpath").sendKeys(Keys.ENTER);
						Thread.sleep(2000L);
						getElement("checkbox_xpath").click();
						getElement("Done_button_id").click();
						Thread.sleep(4000L);
					}catch(NullPointerException e){
						System.out.println("getElement returns null.unable to perform actions");
					}catch(Exception e){
						System.out.println(e.getMessage());
					}	
				}
		}
	}
	
	public void selectValueInSLOV(String locator,String data) throws InterruptedException, IOException{
		if(data!=null){
			try{
				getElement(locator).click();
				Thread.sleep(2000L);
				getElement("searchbox_xpath").clear();
				getElement("searchbox_xpath").sendKeys(data);
				getElement("searchbox_xpath").sendKeys(Keys.ENTER);
				Thread.sleep(2000L);
				getElement("slov_radio_xpath").click();
				getElement("Done_button_id").click();
				Thread.sleep(4000L);
			}catch(NullPointerException e){
				test.log(LogStatus.FAIL, locator+","+data+" --> getElement returns null.unable to perform actions"+e.getMessage());
//				System.out.println("getElement returns null.unable to perform actions");
			}catch(Exception e){
				System.out.println(e.getMessage());
			}	
		}
	}
	
	public void selectValueInDropdown(String locator,String data) throws InterruptedException, IOException{
		List<WebElement> allelements=null;
		if(data!=null){
			try{
			getElement(locator).click();
			}catch(Exception e){
				test.log(LogStatus.FAIL, locator+" --> unable to click an element"+e.getMessage());
			}
			Thread.sleep(2000L);
			try{
			allelements=driver.findElements(By.xpath(prop.getProperty("listbox_values_xpath")));
			}catch(Exception e1){
				test.log(LogStatus.FAIL, "unable to extract the elements"+e1.getMessage());
			}
			
			try{
				for(int i=0;i<allelements.size();i++){
					if((allelements.get(i).getText()).equals(data)){
						allelements.get(i).click();
						break;
					}
				}
				}catch(Exception e2){
					test.log(LogStatus.FAIL, "unable to click an element"+e2.getMessage());
				}
			
		}
		Thread.sleep(3000L);
	}
	
	public void clickOnMenuButton(String buttonText) throws InterruptedException, IOException{
		try{
			if(buttonText.equals("Save")){
				getElement("Save_button_xpath").click();
				test.log(LogStatus.INFO, buttonText+" --> clicked successfully ");
			}else if(buttonText.equals("Send for Approval")){
				getElement("SendforApproval_button_xpath").click();
				test.log(LogStatus.INFO, buttonText+" --> clicked successfully ");
			}else if(buttonText.equals("Close")){
				getElement("Close_button_xpath").click();
				test.log(LogStatus.INFO, buttonText+" --> clicked successfully ");
			}else if(buttonText.equals("Approve")){
				getElement("submit_xpath").click();
				test.log(LogStatus.INFO, buttonText+" --> clicked successfully ");
				Thread.sleep(1000L);
				getElement("submit_approve_xpath").click();
			}else{
//			System.out.println(buttonText+"not found");
				reportFailure(buttonText+"not found");
			}
		}catch(Exception e){
			reportFailure(e.getMessage());
		}
		Thread.sleep(4000L);
	}
	
	public void clickOnButtonInPopup(String buttonName) throws IOException, InterruptedException{
	
		try{
			if(buttonName.equals("Submit")){
				driver.findElement(By.xpath("//*[@id='submit']")).click();
			}else if(buttonName.equals("Cancel")){
				driver.findElement(By.xpath("//*[@id='cancelSubmit']")).click();
			}else if(buttonName.equals("Close")){
				driver.findElement(By.xpath("//*[@id='msi-modal']/div/div/div[3]/button")).click();
			}else{
//			System.out.println("Button not found");
				test.log(LogStatus.ERROR, buttonName+" --> not found");
				reportFailure("Button not found");
			}
		}catch(Exception e){
			
		}
		Thread.sleep(4000L);
	}
	
public void openReport(String reportName) throws InterruptedException{
		try{
			Thread.sleep(4000L);
			act=new Actions(driver);
			act.moveToElement(driver.findElement(By.id("dropdownOpened"))).build().perform();
			Thread.sleep(2000L);
			driver.findElement(By.linkText("GRC Libraries")).click();
			Thread.sleep(2000L);
			driver.findElement(By.linkText("Manage")).click();
			driver.findElement(By.xpath("//*[@id='btn-ReportsLink']")).click();
			driver.findElement(By.linkText(reportName)).click();
			Thread.sleep(6000L);	
		}catch(NullPointerException e){
			System.out.println("getElement returns null.unable to perform actions");
		}catch(Exception e){
			System.out.println(e.getMessage());
		}	
	}

	public void reportFilter(String locator,String data) throws InterruptedException, IOException{
		try{
			getElement("filter_button_xpath").click();
			Thread.sleep(1000L);
			getElement(locator).sendKeys(data);
			getElement("apply_filters_button_xpath").click();
			Thread.sleep(4000L);
		}catch(NullPointerException e){
			System.out.println("getElement returns null.unable to perform actions");
		}catch(Exception e){
			System.out.println(e.getMessage());
		}	
	}
	
	public void reportDataVerify(String locator,String data) throws IOException{
		if(data!=null){
				try{
					String actual_text=getElement(locator).getText();
					Assert.assertEquals(actual_text, data);
					System.out.println("text present in report");
//					test.log(LogStatus.INFO, "text present in report");
				}catch(AssertionError e){
//					System.out.println(data+"not present in report"+e.getMessage());
					reportFailure(data+"not present in report"+e.getMessage());
					e.printStackTrace();
				}catch(ElementNotVisibleException e){
//					System.out.println("Element not visible"+e.getMessage());
					reportFailure("Element not visible"+e.getMessage());
					e.printStackTrace();
				}catch(Exception e){
//					System.out.println(data+"not present in report"+e.getMessage());
					reportFailure(data+"not present in report"+e.getMessage());
					e.printStackTrace();
				}
		}
	}
	
	public void logout() throws InterruptedException, IOException{
		try{
			act=new Actions(driver);
			act.moveToElement(driver.findElement(By.xpath("//*[@id='bs-example-navbar-collapse-1']/ul[2]/li[6]/a"))).build().perform();
			Thread.sleep(1000L);
			driver.findElement(By.xpath("//*[@id='bs-example-navbar-collapse-1']/ul[2]/li[6]/ul/li[8]/a")).click();
			Thread.sleep(1000L);
			driver.findElement(By.xpath("//*[@id='modal-util']/div/div/div/div/button[1]")).click();
//			test.log(LogStatus.PASS, "logged out successfully");
		}catch(NoSuchElementException e){
//			System.out.println("Element not found");
			test.log(LogStatus.FAIL, "unable to logout");
			reportFailure("Element not found");
		}catch(Exception e){
//			System.out.println(e.getMessage());
			test.log(LogStatus.FAIL, "unable to logout");
			reportFailure(e.getMessage());
		}finally{
			try{
			softassertion.assertAll();
			}catch(Throwable t){
				test.log(LogStatus.FAIL, "testcase failed");
			}
		}
		Thread.sleep(6000L);
	}
	
	public void attachFile(String locator,String fileName) throws InterruptedException, IOException{
		if(fileName!=null){
			if(fileName.contains(",")){
				String[] fileNames=fileName.split(",");
				for(int i=0;i<fileNames.length;i++){
					try{	
						getElement(locator).sendKeys(fileNames[i]);
						Thread.sleep(4000L);
					}catch(NullPointerException e){
						System.out.println("getElement returns null.unable to perform actions");
					}catch(Exception e){
						System.out.println(e.getMessage());
					}	
				}
			}
			
			else{
				try{
					getElement(locator).sendKeys(fileName);
				}catch(NullPointerException e){
					System.out.println("getElement returns null.unable to perform actions");
				}catch(Exception e){
					System.out.println(e.getMessage());
				}	
			}
			Thread.sleep(4000L);
		}
	}
	
	
//	public void addRelationship(String data) throws InterruptedException, IOException{
//		if(data!=null){
//			String linkname=data.split(":")[0];
//			String DATA=data.split(":")[1];
//			try{
//				driver.findElement(By.partialLinkText(linkname)).click();
//				Thread.sleep(4000L);
//				driver.findElement(By.xpath("//*[@id='filter-toolbar']/div/div[2]/button")).click();
//				Thread.sleep(2000L);
//				driver.findElement(By.xpath("//*[contains(@id,'ame')]")).sendKeys(DATA);
//				Thread.sleep(2000L);
//				driver.findElement(By.xpath("//*[@id='report-filter-sidebar']/div/div[2]/button[2]")).click();
//				Thread.sleep(2000L);
//				driver.findElement(By.xpath("//*[@id='report-view']/div[1]/div[2]/div[1]/table/tbody/tr/td[1]")).click();
//				Thread.sleep(2000L);
//				driver.findElement(By.xpath("//*[@id='done']")).click();
//				Thread.sleep(4000L);
//			}catch(Exception e){
//				reportFailure(e.getMessage());
//			}
//			
//		}
//			
//	}

	
	public void addRelationship(String data) throws InterruptedException, IOException{
		if(data!=null){
			
			String linkname=data.split(":")[0];
			String DATA=data.split(":")[1];
			
//***************************** Applies to Organizations *********************************
			if(data.contains(",")){
				if(linkname.equals("Applies to Organizations")){
					
					String[] data_split=DATA.split(",");
					
					String name=null;
					String stakeholder=null;
					String applicableTo=null;
					String comments=null;
					
					switch(data_split.length){
					case 1:
						name=DATA.split(",")[0];
						break;
					case 2:
						name=DATA.split(",")[0];
						stakeholder=DATA.split(",")[1];
						break;
					case 3:
						name=DATA.split(",")[0];
						stakeholder=DATA.split(",")[1];
						applicableTo=DATA.split(",")[2];
						break;
					default:
						name=DATA.split(",")[0];
						stakeholder=DATA.split(",")[1];
						applicableTo=DATA.split(",")[2];
						comments=DATA.split(",")[3];
						break;
					}
					
					//adding relationships
					
						try{
						driver.findElement(By.partialLinkText(linkname)).click();
						}catch(Exception e){
							reportFailure(e.getMessage());
						}
						Thread.sleep(4000L);
						try{
							driver.findElement(By.xpath("//*[@id='filter-toolbar']/div/div[2]/button")).click();
						}catch(Exception e){
							reportFailure(e.getMessage());
						}
						Thread.sleep(2000L);
						try{
						driver.findElement(By.xpath("//*[contains(@id,'ame')]")).sendKeys(name);
						}catch(Exception e){
							reportFailure(e.getMessage());
						}
						Thread.sleep(2000L);
						try{
						driver.findElement(By.xpath("//*[@id='report-filter-sidebar']/div/div[2]/button[2]")).click();
						}catch(Exception e){
							reportFailure(e.getMessage());
						}
						Thread.sleep(3000L);
						try{
						driver.findElement(By.xpath("//*[@id='report-view']/div[1]/div[2]/div[1]/table/tbody/tr[1]/td[1]")).click();
						}catch(Exception e){
							test.log(LogStatus.INFO, "unable to check the checkbox");
							reportFailure(e.getMessage());
						}
						Thread.sleep(3000L);
						try{
							driver.findElement(By.xpath("//*[@id='done']")).click();
						Thread.sleep(4000L);
						}catch(Exception e){
							reportFailure(e.getMessage());
						}
					
					
					// clicking column filter
					try{
						driver.findElement(By.xpath("//*[@id='ORB']/thead/tr/th[7]/div/span")).click();
					}catch(Exception e){
						reportFailure(e.getMessage());
					}
					Thread.sleep(2000L);
					//column filter - name data entering
					try{
					driver.findElement(By.xpath("//*[@id='ORB']/thead/tr/th[7]/div/ul/li[1]/form/div/input")).sendKeys(name);
					driver.findElement(By.xpath("//*[@id='ORB']/thead/tr/th[7]/div/ul/li[1]/form/div/input")).sendKeys(Keys.ENTER);
					}catch(Exception e){
						reportFailure(e.getMessage());
					}
					Thread.sleep(3000L);
					
					//title of organization - grid organization name
					if(driver.findElement(By.xpath("//*[@id='ORB']/tbody/tr[2]/td[7]")).getAttribute("title").equals(name)){
						try{
						//stake holders field
							if(stakeholder!=null){
								try{
									driver.findElement(By.xpath("//*[@id='ORB']/tbody/tr[2]/td[21]")).click();
								}catch(Exception e){
									reportFailure(e.getMessage());
								}
								Thread.sleep(3000L);
								try{
									driver.findElement(By.xpath("//*[@id='searchTab']/form/input")).sendKeys(stakeholder);
								}catch(Exception e){
									reportFailure(e.getMessage());
								}
								Thread.sleep(1000L);

								try{
									driver.findElement(By.xpath("//*[@id='searchTab']/form/input")).sendKeys(Keys.ENTER);
								}catch(Exception e){
									reportFailure(e.getMessage());
								}
								
								try{
									driver.findElement(By.xpath("//*[@id='admin-view']/div[2]/table/tbody/tr[1]/td[1]")).click();
								}catch(Exception e){
									reportFailure(e.getMessage());
								}
								Thread.sleep(1000L);
								try{
									driver.findElement(By.xpath("//*[@id='done']")).click();
								}catch(Exception e){
									reportFailure(e.getMessage());
								}
							}
						}catch(Exception e){
							driver.switchTo().defaultContent();
							Thread.sleep(4000L);
//							e.printStackTrace();
						}
						
						// applicable to column data selection
						if(applicableTo!=null){
							Thread.sleep(3000L);
							try{
							driver.findElement(By.xpath("//*[@id='ORB']/tbody/tr[2]/td[22]")).click();
							}catch(Exception e){
								test.log(LogStatus.FAIL, "unable to click the checkbox element");
								reportFailure(e.getMessage());
							}
							Thread.sleep(3000L);
							//get all data from applicable to drop down
							List<WebElement> allData=driver.findElements(By.xpath("//*[@id='select2-drop']/ul/child::li/div"));
							Thread.sleep(2000L);
							for(int i=0;i<allData.size();i++){
								//compare data 
								if(allData.get(i).getText().equals(applicableTo)){
									allData.get(i).click();
									break;
									
								}
							}
							Thread.sleep(4000L);
						}
						
						//entering comments data in ORB grid for applies to organization
						if(comments!=null){
							try{
							driver.findElement(By.xpath("//*[@id='ORB']/tbody/tr[2]/td[31]")).click();
							}catch(Exception e){
								reportFailure(e.getLocalizedMessage());
							}
							Thread.sleep(2000L);
							try{
								driver.findElement(By.xpath("//*[@id='ORB']/tbody/tr[2]/td[31]/div/div[2]/div/div[2]/textarea")).sendKeys(comments);
							}catch(Exception e){
								reportFailure(e.getLocalizedMessage());
							}
							Thread.sleep(2000L);
							try{
								driver.findElement(By.xpath("//*[@id='ORB']/tbody/tr[2]/td[31]/div/div[2]/div/div[3]/button[1]")).click();
							}catch(Exception e){
								reportFailure(e.getMessage());
							}
							Thread.sleep(2000L);
						}	
					}
				}	
			}
			else{			
					//adding relationships
						try{
							driver.findElement(By.partialLinkText(linkname)).click();
							Thread.sleep(4000L);
							driver.findElement(By.xpath("//*[@id='filter-toolbar']/div/div[2]/button")).click();
							Thread.sleep(2000L);
							driver.findElement(By.xpath("//*[contains(@id,'ame')]")).sendKeys(DATA);
							Thread.sleep(2000L);
							driver.findElement(By.xpath("//*[@id='report-filter-sidebar']/div/div[2]/button[2]")).click();
							Thread.sleep(2000L);
							driver.findElement(By.xpath("//*[@id='report-view']/div[1]/div[2]/div[1]/table/tbody/tr/td[1]")).click();
							Thread.sleep(2000L);
							driver.findElement(By.xpath("//*[@id='done']")).click();
							Thread.sleep(4000L);
						}catch(Exception e){
							reportFailure(e.getMessage());
						}
				
				}
			
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	/*************** Reporting functions **************************/
	
	public void reportFailure(String message) throws IOException{
		takeScreenShot();
		test.log(LogStatus.FAIL, message);
		try{
		softassertion.fail();
		}catch(AssertionError ae){
			test.log(LogStatus.FAIL, "assertion failed");
		}
	}
	
	
	public void takeScreenShot() throws IOException{
		Date d=new Date();
		String date1=d.toString();
		date1=date1.replaceAll(" ", "_").replaceAll(":", "_").toString();
		String path=constants.reportpath+date1+".jpg";
		try{
		File src=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(src, new File(path));
		test.log(LogStatus.ERROR, test.addScreenCapture(path));
		}catch(Exception e){
			test.log(LogStatus.INFO, "unable to take a screenshot");
		}
		
	}

	public void setDate(String locator, String data) throws IOException, InterruptedException {
		if(data!=null){
			Thread.sleep(2000L);
			try{
				getElement(locator).sendKeys(data);
				act.sendKeys(Keys.ESCAPE).build().perform();
				Thread.sleep(4000L);
			}catch(Exception e){
				System.out.println(e.getMessage());
				e.printStackTrace();
			}

		}
	}

	public void verifyTextInPage(String data) throws IOException, InterruptedException {
		Thread.sleep(4000L);
		if(data!=null){
			try{
				softassertion.assertTrue(driver.getPageSource().contains(data), "text not present");
			}catch(AssertionError e){
				reportFailure("text not present");
			}
		}
		Thread.sleep(4000L);
	}

	public void invokeAssignmentInMyTasks(String data) throws IOException, InterruptedException {
		if(data!=null){
			Thread.sleep(4000L);
			try{
				act=new Actions(driver);
				act.moveToElement(getElement("my_tasks_xpath")).build().perform();
				Thread.sleep(3000L);
				getElement("mytasks_link_xpath").click();
				Thread.sleep(3000L);
				getElement("mytasks_search_text_xpath").sendKeys(data);
				Thread.sleep(3000L);
				getElement("mytasks_applyfilters_xpath").click();
				Thread.sleep(3000L);
			}catch(Exception e){
				reportFailure(e.getMessage());
			}
			try{
				driver.findElement(By.partialLinkText(data)).click();
			}catch(Exception e){
				System.out.println(data+" not found");
			}
			Thread.sleep(6000L);
		}
	}

	public void verifyDataInFormTextBox(String locator, String expectedData) throws IOException, InterruptedException {
		if(expectedData!=null){
			try{
				String actual_text=getElement(locator).getText();
				softassertion.assertEquals(actual_text, expectedData);
				System.out.println(expectedData+"verified successfully");
			}catch(AssertionError ae){
				reportFailure(ae.getMessage());
			}catch(Exception e){
				reportFailure(e.getMessage());
			}
			Thread.sleep(4000L);
		}
	}
	
	public void verifyDataInMLOV(String locator,String data) throws IOException, InterruptedException{
		if(data!=null){
			String actual_text="";
			List<WebElement> allData=driver.findElements(By.xpath(prop.getProperty(locator)));
			Thread.sleep(3000L);
			System.out.println(allData.size());
			if(allData.size()>1){
				for(int i=0;i<allData.size();i++){
					actual_text=actual_text+allData.get(i).getText();
					System.out.println(actual_text);
				}
				String text=actual_text.trim();
				data=data.replaceAll(",", "");
				try{
					softassertion.assertEquals(text, data);
//					System.out.println("assertion pass text are equal");
				}catch(AssertionError e){
					reportFailure(e.getMessage());
				}
			Thread.sleep(6000L);
			}
			
			if(allData.size()==1){
				for(int i=0;i<allData.size();i++){
					actual_text=allData.get(i).getText();
				}
				String text=actual_text.trim();
				try{
					softassertion.assertEquals(text, data);
//					System.out.println("assertion pass text are equal");
				}catch(AssertionError e){
					reportFailure("MLOV field actual and expected not matching"+text+" "+data+" "+e.getMessage());
				}
			}
		}
		Thread.sleep(6000L);
	}

	public void verifySummaryData(String expectedData) throws IOException, InterruptedException {
		if(expectedData!=null){	
			Thread.sleep(2000L);
			try{
				getElement("form_summary_expand_xpath").click();
				Thread.sleep(2000L);
				List<WebElement> alldata=driver.findElements(By.xpath(prop.getProperty("form_summary_xpath")));
				
				for(int i=0;i<alldata.size();i++){
					if(alldata.get(i).getText().equals(expectedData)){
						test.log(LogStatus.PASS, expectedData+" --> present in summary section");
					}
				}
				
			}catch(AssertionError err){
				reportFailure(expectedData+" not found");
			}catch(Exception e){
				reportFailure(e.getMessage());
			}
			Thread.sleep(4000L);
		}
	}

	public void WSI(String data) throws IOException {
		if(data!=null){
			try{
				String displayValue=getElement("forms_wsi_xpath").getText().toString().trim();
				System.out.println(displayValue);
				if(displayValue.equals(data)){
					test.log(LogStatus.PASS, "LSI displaying correctly");
				}else{
					
					test.log(LogStatus.FAIL, data+" not found");
					reportFailure(data+" not found");
				}
				
			}catch(Exception e){
				reportFailure(e.getMessage());
			}
		}
		
	}

	public void verifyFormTitle(String data) throws InterruptedException, IOException {
		Thread.sleep(3000L);
		try{
		Assert.assertEquals(getElement("form_title_id").getAttribute("title"), data);
		}catch(Exception e){
			reportFailure(e.getMessage());
		}catch(AssertionError ae){
			reportFailure(ae.getMessage());
		}
		Thread.sleep(4000L);
	}

	public void enterValueInTextArea(String locator, String data) throws InterruptedException, IOException {
		Thread.sleep(2000L);
		getElement(locator).sendKeys(data);
		Thread.sleep(4000L);
	}

	public void openDataBrowser(String data) throws IOException, InterruptedException {
		
		test.log(LogStatus.INFO, "Opening databrowser");
		List<WebElement> allDataBrowsers=null;
		try{
		getElement("databrowser_open_icon_xpath").click();
		}catch(Exception e){
			test.log(LogStatus.FAIL, "unable to expand data browser");
			reportFailure(e.getMessage());
		}
		Thread.sleep(3000L);
		try{
			allDataBrowsers=driver.findElements(By.xpath(prop.getProperty("all_databrowser_xpath")));
		}catch(Exception e){
			test.log(LogStatus.FAIL, "unable to extract all data browser values");
		}
		for(int i=0;i<allDataBrowsers.size();i++){
			System.out.println(allDataBrowsers.get(i).getText());
			try{
				if(data.equals(allDataBrowsers.get(i).getText())){
					allDataBrowsers.get(i).click();
					break;
				}
			}catch(Exception e){
				test.log(LogStatus.FAIL, "unable to click the"+data);
				reportFailure(e.getMessage());
			}
		}
		Thread.sleep(6000L);
	}

	public void showFiltersInDataBrowser() throws IOException, InterruptedException{
		try{
			getElement("databrowser_show_filters_xpath").click();
		}catch(Exception e){
			test.log(LogStatus.FAIL, "unable to click the filters icon in databrowser");
			reportFailure(e.getMessage());
		}Thread.sleep(6000L);
	}
	
	
	public void enterDataInDataBrowserFilters(String locator,String data,String fieldType) throws IOException, InterruptedException {
		if(data!=null){
		test.log(LogStatus.INFO, "enter data in databrowser filters");
			Thread.sleep(2000L);
			if(fieldType.equals("TEXTBOX")){
				try{
					getElement(locator).sendKeys(data);
				}catch(Exception e1){
					reportFailure(e1.getMessage());
				}
			}
			try{
				getElement("data_browser_apply_filter_button_xpath").click();
			}catch(Exception e2){
				reportFailure("unable to click the apply filter button in databrowser filter section --> "+e2.getMessage());
			}
			Thread.sleep(6000L);
		}
	}

	public void clickOnDataInDatabrowser(String data) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		test.log(LogStatus.INFO, "Clicking data in the databrowser area");
		List<WebElement> allData=null;
		Thread.sleep(3000L);
		
		try{
			allData=driver.findElements(By.xpath("//*[@id='dbtree']/div/child::div/div/span[1]"));
		}catch(Exception e){
			
			reportFailure("unable to extract the elements");
		}
		try{
			for(int i=0;i<allData.size();i++){
				if(data.equals(allData.get(i).getText())){
					allData.get(i).click();
					break;
				}
			}
		}catch(Exception e){
			reportFailure(e.getMessage());
		}
		Thread.sleep(4000L);
	}
	
	
	

}

	

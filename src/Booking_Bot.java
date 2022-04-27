import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.JavascriptExecutor;


public class Booking_Bot extends Thread{
	
	WebDriver driver;
	SimpleDateFormat dateformat;
	Statement st;
	String Hotels[] = {
						"B Ocean Resort, Fort Lauderdale, Florida, United States",
						"Sea Club Ocean Resort, Fort Lauderdale, Florida, United States",
						"Bahia Mar - Fort Lauderdale Beach - DoubleTree by Hilton, Fort Lauderdale, Florida, United States",
						"Premiere Hotel, Fort Lauderdale, Florida, United States"};
	
	String links[] = {"//*[@id=\\\"search_results_table\\\"]/div/div/div/div/div[6]/div[2]/div[1]/div[2]/div/div[2]/div[2]/div/div[1]/div/div/div[2]/span",
			          "//*[@id=\"search_results_table\"]/div/div/div/div/div[6]/div[1]/div[1]/div[2]/div/div[2]/div[2]/div/div[1]/div/div/div[2]/span",
			          "//*[@id=\"search_results_table\"]/div/div/div/div/div[6]/div[2]/div[1]/div[2]/div/div[2]/div[2]/div/div[1]/div/div/div[2]/span"};
					
	public void setBrowser() {		
		System.setProperty("webdriver.chrome.driver", "C:\\Jars\\chromedriver.exe");		
		HashMap<String,Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("plugins.always_open_pdf_externally", true);
		chromePrefs.put("download.default_directory", "C:"+File.separator+"Square_download");
		chromePrefs.put("excludeSwitches", "enable-popup-blocking");	
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("prefs", chromePrefs);
		
		
		driver = new ChromeDriver(options);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().window().maximize();
	}
	Booking_Bot(){
		dateformat = new SimpleDateFormat("yyyy-MM-dd");
	}
	public void run() {
		String curDate ="";		
		System.out.println(curDate);
		
		while(true) {
			try {
				Thread.sleep(2000);
			}catch(Exception ee) {ee.printStackTrace();}
			System.out.println("waiting");
			
			if(!curDate.equalsIgnoreCase(dateformat.format(new Date()))) {
				setBrowser();
			    String queryMe = "INSERT INTO Booking (Date, B_Ocean_Price, Sea_Club_Price, Bahia_Mar_Price, Premiere_Price) VALUES (value1, value2, value3,)";
				curDate = dateformat.format(new Date());
				for(int i=0;i<Hotels.length;i++) {
					System.out.print("Proccessing "+Hotels[i]);
					driver.get("https://www.booking.com/");
					driver.findElement(By.id("ss")).clear();
					driver.findElement(By.id("ss")).sendKeys(Hotels[i]);
					
					
					try {
						Thread.sleep(5000);
					}catch(Exception ee) {ee.printStackTrace();}
					
					
					if(i == 0) {
						driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/form/div[1]/div[2]/div[1]/div[3]/div/div/div/div/span")).click();
								
								WebElement table = driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/form/div[1]/div[2]/div[2]/div/div/div[3]/div[1]/table"));
								List<WebElement> col = table.findElements(By.xpath(".//tbody/tr"));
								
								int col2;
								for(int i1=0;i1<col.size()+1;i1++) {
								
								col2=i1;
								List<WebElement> date = table.findElements(By.xpath(".//tbody/tr["+i1+"]/td"));
									for(int ii=0;ii<date.size();ii++) {    		
										
										if(date.get(ii).getText().equalsIgnoreCase(new Date().getDate()+"")) {
										driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/form/div[1]/div[2]/div[2]/div/div/div[3]/div[1]/table/tbody/tr["+col2+"]/td["+(ii+1)+"]")).click();																		
										break;
										}
									}
								}
						}
						
						driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/form/div[1]/div[4]/div[2]/button/span[1]")).click();
						String value = "";
						int uselink = 0;
						while(true) {
							
							try {
								Thread.sleep(2000);
							}catch(Exception ee) {ee.printStackTrace();}
							
							try {
								value = driver.findElement(By.xpath(links[uselink])).getText();
								                                     
								break;
							}catch(Exception ee) {uselink++;
													if(uselink==links.length) {
														value = "N/A";
														break;
													}
													else {
														System.out.print(" new link"+uselink);
													} 
												}
						}
						
						
						
						System.out.println(" - "+ value);
						
						try {
							Thread.sleep(4000);
						}catch(Exception ee) {ee.printStackTrace();}
					}
					
					
					
				}
			}
	}
	
	
	public void setDataBaseConnection() {
		while(true) {
			System.out.println("Database connecting");
			//gui.textAppend("Database connecting\n");
			try{  
				Thread.sleep(1500);
				Class.forName("com.mysql.jdbc.Driver");  
				Connection con=DriverManager.getConnection(  
				"jdbc:mysql://localhost:3306/Booking_db","root","");  	
				 st=con.createStatement();
				 break;
				
			   }catch(Exception e){}
		}
	}
	
	public static void main(String[] args) {
		new Booking_Bot().start();

	}

}

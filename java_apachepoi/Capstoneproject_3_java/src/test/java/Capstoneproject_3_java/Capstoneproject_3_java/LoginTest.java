package Capstoneproject_3_java.Capstoneproject_3_java;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import junit.framework.Assert;

public class LoginTest {
	public WebDriver driver;

	@DataProvider
	public String[][] getExcelData() throws InvalidFormatException, IOException {
		ExcelConfig read = new ExcelConfig(System.getProperty("user.dir") + "/testdata/logincredential.xlsx");
		int rows = read.getRowCount(0);
		String[][] signin_credentials = new String[rows][2];

		for(int i=0;i<rows;i++)
		{
		signin_credentials[i][0] = read.readData("Sheet1", i, 0);
		signin_credentials[i][1] = read.readData("Sheet1", i, 1);
		}
		return signin_credentials;
		
	}

	@BeforeMethod
	public void beforeMethod() {
		System.out.println("Executing on Chrome");
		String driver_path = System.getProperty("user.dir") + "/chromedriver/chromedriver.exe";
		System.setProperty("webdriver.chrome.driver", driver_path);
		driver = new ChromeDriver();
		driver.get("https://www.saucedemo.com/");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@AfterMethod
	public void afterMethod() {
		driver.close();
	}

	@Test(description = "This test verify successful and unsuccessful login", dataProvider = "getExcelData")
	public void verifyLogin(String Username, String Password){
		driver.findElement(By.id("user-name")).clear();
		driver.findElement(By.id("user-name")).sendKeys(Username);
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(Password);
		driver.findElement(By.id("login-button")).click();
		
		
		if(!driver.findElements(By.xpath("//div[contains(@class, 'error-message-container')]")).isEmpty())
		{
			String error_msg = driver.findElement(By.xpath("//form//h3[@data-test ='error']")).getText();
			Assert.assertTrue(error_msg.equals("Epic sadface: Username and password do not match any user in this service") || error_msg.equals("Epic sadface: Sorry, this user has been locked out."));
		}
		else
		{
			Assert.assertEquals("Swag Labs", driver.getTitle());
		}
	}
}
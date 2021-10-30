package com.dummy.scripts;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.poi.util.SystemOutLogger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.io.Files;

public class dummyscipt_Test {
WebDriver driver;

@BeforeMethod
public void preCondition() throws InterruptedException
{
	System.setProperty("webdriver.chrome.driver","./exefiles/chromedriver.exe");
	driver = new ChromeDriver();
	driver.manage().window().maximize();
	driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
	driver.navigate().to("https://www.flipkart.com/");
}

@Test
public void stepdefinitions() throws InterruptedException
{
	//closed the initial popup and used hord code wait just to show the popup 
	Thread.sleep(2000);
	driver.findElement(By.xpath("//button[text()='✕']")).click();	
	System.out.println("closed the initial popup passed");
	
	//enter product name and click on search icon
	driver.findElement(By.name("q")).sendKeys("fastrack watches men");
	driver.findElement(By.xpath("//button[@type='submit']")).submit();
	System.out.println("enter product name and click on search icon passed");
	
	//set the min price value
	Thread.sleep(2000);
	WebElement minPrice = driver.findElement(By.xpath("//option[@value='Min']/.."));
	Select minPricesel = new Select(minPrice);
	minPricesel.selectByValue("500");
	System.out.println("set the min price value passed");
	
	//set the max price value
	Thread.sleep(2000);
	WebElement maxPrice = driver.findElement(By.xpath("//option[@value='Max']/.."));
	Select maxPricesel = new Select(maxPrice);
	maxPricesel.selectByValue("1000");
	System.out.println("set the max price value passed");
	
	//validate that first product price is under 1000 and take screenshot
	String firstwatchPrice = driver.findElement(By.xpath("//div[@data-id='WATF2VTY73GHJ4PY']//div[text()='₹765']")).getText();
	Assert.assertEquals(firstwatchPrice, "₹765", "validated First watch price is less 1000");
	System.out.println("validate that first product price is under 1000 passed");
	
	EventFiringWebDriver efw=new EventFiringWebDriver(driver);
	File srcfile= efw.getScreenshotAs(OutputType.FILE);
	File destfile= new File("./screenshots/productscreenshot.png");
	try {
		Files.copy(srcfile,destfile);
		System.out.println("screenshot has been taken");
	} catch (IOException e) {
		
		e.printStackTrace();
	}
	
	//click on first watch
	driver.findElement(By.xpath("//div[@data-id='WATF2VTY73GHJ4PY']//div[text()='₹765']")).click();
	System.out.println("click on first watch passed");
	
	//move driver to second (childtab) window
	Set<String> session = driver.getWindowHandles();
	int count = session.size();
	
	//fetch first sessionid
	Iterator<String> itr = session.iterator();
	String parentsessionid = itr.next();
	
	//fetch second sessionid and moved driver to it
	String childsessionid = itr.next();
	driver.switchTo().window(childsessionid);
	System.out.println("driver control sucesfully moved to child tab");
	
	//scroll to an webelement
	Thread.sleep(3000);
	String jscode="arguments[0].scrollIntoView(true)";
	WebElement scrollpoint= driver.findElement(By.xpath("//div[text()='Services']"));
	JavascriptExecutor je = (JavascriptExecutor)driver;
	je.executeScript(jscode, scrollpoint);
	System.out.println("scroll to an webelement");
	
	
	//choose different color of watch via mouse operation
	Actions act = new Actions(driver);
	WebElement blackbluewatch= driver.findElement(By.xpath("//li[@id='swatch-1-strap_color']"));
	act.moveToElement(blackbluewatch).perform();
	System.out.println("mouse over blackbluewatch to see color passed");
	
	//provide hard code wait to whether driver click on another watch on not
	Thread.sleep(3000);
	WebElement blackgreywatch= driver.findElement(By.xpath("//li[@id='swatch-3-strap_color']"));
	act.moveToElement(blackgreywatch).click().perform();
	System.out.println("select blackbluewatch passed");
	
	//click on add to cart button
	driver.findElement(By.xpath("//div[@id='container']//ul[@class='row']//li/button")).click();
	System.out.println("add to cart button passed");
	
	//click on place order button
	driver.findElement(By.xpath("//button//span[text()='Place Order']")).click();
	System.out.println("place order button passed");
}

@AfterMethod()
public void postCondition() 
{
	driver.close();
	System.out.println("postCondition condition passed");
}
}

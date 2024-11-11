package Temp_pack;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Login {
	@Test(invocationCount = 60)
	public void User01_email01_20hits() throws InterruptedException {

		WebDriver driver = new ChromeDriver();

		driver.get("https://www.startupindia.gov.in/bhaskar");
		driver.manage().window().maximize();
		driver.findElement(By.xpath("//button[normalize-space()='Login']")).click();
		driver.findElement(By.xpath("//input[@placeholder='Please enter your email address']"))
				.sendKeys("prodqa01@gmail.com");
		driver.findElement(By.xpath("//input[@placeholder='Please enter your password']")).sendKeys("Test@123");
		driver.findElement(By.xpath("//div[@class='styles_loginbtn__8y8bm']//button[contains(text(),'Login')]"))
				.click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		WebElement element = wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath("//h4[normalize-space()='IN-0924-9071LK']")));

		// Perform any action on the element (e.g., clicking)
		System.out.println("Element found: " + element.getText());

		String currenturl = driver.getCurrentUrl();
		System.out.println(currenturl);

		Assert.assertEquals(currenturl, "https://www.startupindia.gov.in/bhaskar/profile");

		driver.quit();

	}

	@Test(invocationCount = 60)
	public void User02_email02_20hits() throws InterruptedException {
		WebDriver driver = new ChromeDriver();
		driver.get("https://www.startupindia.gov.in/bhaskar");
		driver.manage().window().maximize();
		driver.findElement(By.xpath("//button[normalize-space()='Login']")).click();
		driver.findElement(By.xpath("//input[@placeholder='Please enter your email address']"))
				.sendKeys("aaaza@gmail.com");
		driver.findElement(By.xpath("//input[@placeholder='Please enter your password']")).sendKeys("Test@123");
		driver.findElement(By.xpath("//div[@class='styles_loginbtn__8y8bm']//button[contains(text(),'Login')]"))
				.click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		WebElement element = wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath("//h4[normalize-space()='IN-1124-9095JB']")));

		// Perform any action on the element (e.g., clicking)
		System.out.println("Element found: " + element.getText());

		String currenturl = driver.getCurrentUrl();
		System.out.println(currenturl);

		Assert.assertEquals(currenturl, "https://www.startupindia.gov.in/bhaskar/profile");

		driver.quit();
	}

	@Test(invocationCount = 60)
	public void User03_email03_20hits() throws InterruptedException {
		WebDriver driver = new ChromeDriver();
		driver.get("https://www.startupindia.gov.in/bhaskar");
		driver.manage().window().maximize();
		driver.findElement(By.xpath("//button[normalize-space()='Login']")).click();
		driver.findElement(By.xpath("//input[@placeholder='Please enter your email address']"))
				.sendKeys("prodstartupfounder@yopmail.com");
		driver.findElement(By.xpath("//input[@placeholder='Please enter your password']")).sendKeys("Test@123");
		driver.findElement(By.xpath("//div[@class='styles_loginbtn__8y8bm']//button[contains(text(),'Login')]"))
				.click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		WebElement element = wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//h2[normalize-space()='Complete Your Public Profile']")));

		// Perform any action on the element (e.g., clicking)
		System.out.println("Element found: " + element.getText());

		String currenturl = driver.getCurrentUrl();
		System.out.println(currenturl);

		Assert.assertEquals(currenturl,
				"https://www.startupindia.gov.in/bhaskar/register/stepfour?role=StartupFounder&bhaskarid=IN-1024-9082CY");

		driver.quit();
	}

	@Test(invocationCount = 60)
	public void User04_email04_20hits() throws InterruptedException {
		WebDriver driver = new ChromeDriver();
		driver.get("https://www.startupindia.gov.in/bhaskar");
		driver.manage().window().maximize();
		driver.findElement(By.xpath("//button[normalize-space()='Login']")).click();
		driver.findElement(By.xpath("//input[@placeholder='Please enter your email address']"))
				.sendKeys("prodtredbazowner@yopmail.com");
		driver.findElement(By.xpath("//input[@placeholder='Please enter your password']")).sendKeys("Test@123");
		driver.findElement(By.xpath("//div[@class='styles_loginbtn__8y8bm']//button[contains(text(),'Login')]"))
				.click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		WebElement element = wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//h2[normalize-space()='Complete Your Public Profile']")));

		// Perform any action on the element (e.g., clicking)
		System.out.println("Element found: " + element.getText());

		String currenturl = driver.getCurrentUrl();
		System.out.println(currenturl);

		Assert.assertEquals(currenturl,
				"https://www.startupindia.gov.in/bhaskar/register/stepfour/org?role=Startup&bhaskarid=OI-1024-9082DN");

		driver.quit();
	}

	@Test(invocationCount = 60)
	public void User05_email05_20hits() throws InterruptedException {
		WebDriver driver = new ChromeDriver();
		driver.get("https://www.startupindia.gov.in/bhaskar");
		driver.manage().window().maximize();
		driver.findElement(By.xpath("//button[normalize-space()='Login']")).click();
		driver.findElement(By.xpath("//input[@placeholder='Please enter your email address']"))
				.sendKeys("prodmentor@yopmail.com");
		driver.findElement(By.xpath("//input[@placeholder='Please enter your password']")).sendKeys("Test@123");
		driver.findElement(By.xpath("//div[@class='styles_loginbtn__8y8bm']//button[contains(text(),'Login')]"))
				.click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		WebElement element = wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//h2[normalize-space()='Complete Your Public Profile']")));

		// Perform any action on the element (e.g., clicking)
		System.out.println("Element found: " + element.getText());

		String currenturl = driver.getCurrentUrl();
		System.out.println(currenturl);

		Assert.assertEquals(currenturl,
				"https://www.startupindia.gov.in/bhaskar/register/stepfour?role=Mentor&bhaskarid=IN-1024-9082DF");

		driver.quit();
	}

}
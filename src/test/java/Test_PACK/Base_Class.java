package Test_PACK;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class Base_Class {
	WebDriver driver;
	
	 @BeforeMethod
	    public void setUp() {
	        driver = new ChromeDriver();
	        driver.manage().window().maximize();
	        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
	        driver.get("https://tutorialsninja.com/demo/");
	    }

	 
	 
	 @AfterMethod
	    public void tearDown() throws InterruptedException {
	        // Close the browser after the test
		 Thread.sleep(1000);
	        if (driver != null) {
	            driver.quit();
	        }
	    }

}

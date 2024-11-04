package POM_PACK;

import java.util.Date;
import java.util.Random;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class C_account_success_page {
	private WebDriver driver;

	public static String reusableEmail() {
		return new Date().toString().replace(" ", "").replace(":", "") + "@gmail.com";
	}

	public static String reusableMobile() {
		return "9" + (100000000L + (long) (new Random().nextDouble() * 900000000L));
	}

	// Constructor
	public C_account_success_page(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
//----------------------------------------------------------------------
	
	@FindBy(xpath="//a[normalize-space()='Continue']")
	WebElement succ_ContinueCTA;
	
	public void click_ucc_ContinueCTA()
	{
		succ_ContinueCTA.click();
	}
	
}
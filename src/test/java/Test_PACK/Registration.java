package Test_PACK;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.util.Properties;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import POM_PACK.A_Navigation_bar;
import POM_PACK.B_Registration_page;
import POM_PACK.C_account_success_page;
import POM_PACK.D_MyAccount_page;
import POM_PACK.D_RHS_Menu_bar;
import POM_PACK.E_account_newsletter;
import POM_PACK.F_LoginPage;
import POM_PACK.G_Account_EditPage;
import Utility_PACK.PropertyFileUtil;
import Utility_PACK.Reusable_details;
import Utility_PACK.TakeScreenshotClass;

public class Registration {

	public static WebDriver driver;
	String browserName;
	static SoftAssert Softass = new SoftAssert();
	private static final Logger logger = LogManager.getLogger(Registration.class);

	static A_Navigation_bar navbarmenu;
	static B_Registration_page RegistrationPG;
	static C_account_success_page AccountSuccessPage;
	static D_RHS_Menu_bar RHS_Menu_bar;
	static D_MyAccount_page myAcpage;
	static E_account_newsletter NewsLetterpage;
	static F_LoginPage loginpage;
	static G_Account_EditPage MyAC_Editpage;

	@BeforeMethod()
	public void setUp() {

		logger.info("Starting test execution");

		browserName = PropertyFileUtil.getProperty("browserName");

		if (browserName.equalsIgnoreCase("chrome")) {
			driver = new ChromeDriver();

		}

		else if (browserName.equalsIgnoreCase("Edge")) {
			driver = new EdgeDriver();
		} else if (browserName.equalsIgnoreCase("Firefox")) {

			driver = new FirefoxDriver();
		} else {
			System.out.println("Unknown browser:" + browserName);
		}

		logger.info("Driver initialized");

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.get(PropertyFileUtil.getProperty("baseUrl"));

		navbarmenu = new A_Navigation_bar(driver);
		navbarmenu.click_NavBara_MyAccountCTA();
		navbarmenu.click_NavBara_Register_MyAccount();

	}

	@AfterMethod
	public void tearDown(ITestResult result) {
		if (result.getStatus() == ITestResult.FAILURE) {
			logger.error("Test failed: " + result.getName());
			// Add screenshot capture code here
		} else if (result.getStatus() == ITestResult.SUCCESS) {
			logger.info("Test passed: " + result.getName());
		}

		if (driver != null) {
			driver.quit();
			logger.info("Driver closed");
		}
	}

//=======================================================================================================	
	@Test(priority = 1)
	public void TC_RF_001_verify_Registering_Account_Using_MandatoryFields() {
		logger.info("Starting test : TC_RF_001 ");
		RegistrationPG = new B_Registration_page(driver);
		RegistrationPG.Send_FirstName(PropertyFileUtil.getProperty("FirstName"));
		RegistrationPG.Send_LastName(PropertyFileUtil.getProperty("LastName"));
		RegistrationPG.Send_Email(Reusable_details.reusableEmail());
		RegistrationPG.Send_Mobile(Reusable_details.reusableMobile());
		RegistrationPG.Send_password(PropertyFileUtil.getProperty("ValidPasword"));
		RegistrationPG.Send_ConfirmPassword(PropertyFileUtil.getProperty("ValidComfirmPasword"));
		RegistrationPG.Select_disclaimer();
		RegistrationPG.clickON_Continue();
		AccountSuccessPage = new C_account_success_page(driver);

		Assert.assertEquals(AccountSuccessPage.GetTest_Success_messgae_on_Accountpage(),
				"Your Account Has Been Created!");

		RHS_Menu_bar = new D_RHS_Menu_bar(driver);

		Softass.assertEquals(RHS_Menu_bar.Logout_RHSmenu_isDisplayed(), true);
		Softass.assertAll();
	}

//================================================================================================	
	@Test(priority = 2)
	public void TC_RF_002_verifyThankYourConfirmationEmailOnSuccessfulRegistration() throws InterruptedException {

		RegistrationPG = new B_Registration_page(driver);
		RegistrationPG.Send_FirstName(PropertyFileUtil.getProperty("FirstName"));
		RegistrationPG.Send_LastName(PropertyFileUtil.getProperty("LastName"));

		String emailText = Reusable_details.reusableEmail();
		RegistrationPG.Send_Email(Reusable_details.reusableEmail());
		RegistrationPG.Send_Mobile(Reusable_details.reusableMobile());
		RegistrationPG.Send_password(PropertyFileUtil.getProperty("ValidPasword"));
		RegistrationPG.Send_ConfirmPassword(PropertyFileUtil.getProperty("ValidComfirmPasword"));
		RegistrationPG.Select_disclaimer();
		RegistrationPG.clickON_Continue();

		String email = emailText;
		String appPasscode = "dbmm vncw rtja ewoo";

		Thread.sleep(2000);

		// Gmail IMAP configuration
		String host = "imap.gmail.com";
		String port = "993";
		String username = email; // Your Gmail address
		String appPassword = appPasscode; // Your app password
		String expectedSubject = "Welcome To TutorialNinja";
		String expectedFromEmail = "tutorialsninja<account-update@tn.in>";
		String expectedBodyContent = "Your account has been successfully created.";

		try {
			// Mail server connection properties
			Properties properties = new Properties();
			properties.put("mail.store.protocol", "imaps");
			properties.put("mail.imap.host", host);
			properties.put("mail.imap.port", port);
			properties.put("mail.imap.ssl.enable", "true");

			// Connect to the mail server
			Session emailSession = Session.getDefaultInstance(properties);
			Store store = emailSession.getStore("imaps");
			store.connect(host, username, appPassword); // replace email password with App password

			// Open the inbox folder
			Folder inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_ONLY);

			// Search for unread emails
			Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

			boolean found = false;
			for (int i = messages.length - 1; i >= 0; i--) {

				Message message = messages[i];

				if (message.getSubject().contains(expectedSubject)) {
					found = true;
					Assert.assertEquals(message.getSubject(), expectedSubject);
					Assert.assertEquals(message.getFrom()[0].toString(), expectedFromEmail);
					// Assert.assertTrue(CommonUtilities.getTextFromMessage(message).contains(expectedBodyContent));
					break;
				}
			}

			if (!found) {
				System.out.println("No confirmation email found.");
			}

			// Close the store and folder objects
			inbox.close(false);
			store.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		Softass.assertAll();
	}

//============================================================================================================	
	@Test(priority = 3)
	public void TC_RF_003_verifyRegisteringAnAccountproviding_all_fields() throws IOException {
		// Entering user details

		RegistrationPG = new B_Registration_page(driver);
		RegistrationPG.Send_FirstName(PropertyFileUtil.getProperty("FirstName"));
		RegistrationPG.Send_LastName(PropertyFileUtil.getProperty("LastName"));
		RegistrationPG.Send_Email(Reusable_details.reusableEmail());
		RegistrationPG.Send_Mobile(Reusable_details.reusableMobile());
		RegistrationPG.Send_password(PropertyFileUtil.getProperty("ValidPasword"));
		RegistrationPG.Send_ConfirmPassword(PropertyFileUtil.getProperty("ValidComfirmPasword"));
		RegistrationPG.select_SubscribeRadio_YES();
		RegistrationPG.Select_disclaimer();
		RegistrationPG.clickON_Continue();
		AccountSuccessPage = new C_account_success_page(driver);

		Softass.assertEquals(AccountSuccessPage.GetTest_Success_messgae_on_Accountpage(),
				"Your Account Has Been Created!");

		RHS_Menu_bar = new D_RHS_Menu_bar(driver);

		Softass.assertEquals(RHS_Menu_bar.Logout_RHSmenu_isDisplayed(), true);
		Softass.assertAll();
	}

//===============================================================================================
	@Test(priority = 4)
	public void TC_RF_004_Verify_proper_notification_messages_with_no_data_in_the_madetory_field() {
		RegistrationPG = new B_Registration_page(driver);
		RegistrationPG.clickON_Continue();

		Softass.assertEquals(RegistrationPG.Get_alertmeg_for_Privacy_Policy(),
				"Warning: You must agree to the Privacy Policy!");

		Softass.assertEquals(RegistrationPG.Get_alertmeg_for_Firstname(),
				"First Name must be between 1 and 32 characters!");
		Softass.assertEquals(RegistrationPG.Get_alertmeg_for_Lastname(),
				"Last Name must be between 1 and 32 characters!");
		Softass.assertEquals(RegistrationPG.Get_alertmeg_for_Email(), "E-Mail Address does not appear to be valid!");
		Softass.assertEquals(RegistrationPG.Get_alertmeg_for_telephone(),
				"Telephone must be between 3 and 32 characters!");
		Softass.assertEquals(RegistrationPG.Get_alertmeg_for_Password(),
				"Password must be between 4 and 20 characters!");
		Softass.assertAll();
	}

//=========================================================================================
	@Test(priority = 5)
	public void TC_RF_005_Verify_Registering_an_Accountwith_Newsletter_field_As_YES() throws InterruptedException {

		RegistrationPG = new B_Registration_page(driver);
		RegistrationPG.Send_FirstName(PropertyFileUtil.getProperty("FirstName"));
		RegistrationPG.Send_LastName(PropertyFileUtil.getProperty("LastName"));
		RegistrationPG.Send_Email(Reusable_details.reusableEmail());
		RegistrationPG.Send_Mobile(Reusable_details.reusableMobile());
		RegistrationPG.Send_password(PropertyFileUtil.getProperty("ValidPasword"));
		RegistrationPG.Send_ConfirmPassword(PropertyFileUtil.getProperty("ValidComfirmPasword"));
		RegistrationPG.select_SubscribeRadio_YES();
		RegistrationPG.Select_disclaimer();
		RegistrationPG.clickON_Continue();
		AccountSuccessPage = new C_account_success_page(driver);
		AccountSuccessPage.click_ucc_ContinueCTA();
		myAcpage = new D_MyAccount_page(driver);
		myAcpage.click_MyAC_subsscribe_Opn();
		NewsLetterpage = new E_account_newsletter(driver);
		Softass.assertEquals(NewsLetterpage.isSelectedNewsleterpage_YES(), true);
		Softass.assertEquals(NewsLetterpage.isSelectedNewsleterpage_NO(), false);
		Softass.assertAll();
	}

//============================================================================================
	@Test(priority = 6)
	public void TC_RF_006_Verify_Registering_an_Accountwith_Newsletter_field_As_NO() {
		RegistrationPG = new B_Registration_page(driver);
		RegistrationPG.Send_FirstName(PropertyFileUtil.getProperty("FirstName"));
		RegistrationPG.Send_LastName(PropertyFileUtil.getProperty("LastName"));
		RegistrationPG.Send_Email(Reusable_details.reusableEmail());
		RegistrationPG.Send_Mobile(Reusable_details.reusableMobile());
		RegistrationPG.Send_password(PropertyFileUtil.getProperty("ValidPasword"));
		RegistrationPG.Send_ConfirmPassword(PropertyFileUtil.getProperty("ValidComfirmPasword"));
		RegistrationPG.select_SubscribeRadio_NO();
		RegistrationPG.Select_disclaimer();
		RegistrationPG.clickON_Continue();
		AccountSuccessPage = new C_account_success_page(driver);
		AccountSuccessPage.click_ucc_ContinueCTA();
		myAcpage = new D_MyAccount_page(driver);
		myAcpage.click_MyAC_subsscribe_Opn();
		NewsLetterpage = new E_account_newsletter(driver);
		Softass.assertEquals(NewsLetterpage.isSelectedNewsleterpage_YES(), false);
		Softass.assertEquals(NewsLetterpage.isSelectedNewsleterpage_NO(), true);
		Softass.assertAll();
	}

//================================================================================================
	@Test(priority = 7)
	public void TC_RF_007_Verify__diff_ways_to_navigating_to_Register_page() {

		RegistrationPG = new B_Registration_page(driver);
		Softass.assertEquals(RegistrationPG.Get_Text_RegisterAccount(), "Register Account");
		Softass.assertEquals(driver.getCurrentUrl(),
				"https://tutorialsninja.com/demo/index.php?route=account/register");
//----------------------------------------------------------------------------------------
		navbarmenu = new A_Navigation_bar(driver);
		navbarmenu.click_NavBara_MyAccountCTA();
		navbarmenu.click_NavBara_Register_MyAccount();
		Softass.assertEquals(RegistrationPG.Get_Text_RegisterAccount(), "Register Account");
		Softass.assertEquals(driver.getCurrentUrl(),
				"https://tutorialsninja.com/demo/index.php?route=account/register");
//--------------------------------------------------------------------------------------
		driver.get(PropertyFileUtil.getProperty("baseUrl"));
		navbarmenu.click_NavBara_MyAccountCTA();
		navbarmenu.click_Login_MyAccount();

		loginpage = new F_LoginPage(driver);
		loginpage.click_ContinueBBTN_loginpage();
		Softass.assertEquals(RegistrationPG.Get_Text_RegisterAccount(), "Register Account");
		Softass.assertEquals(driver.getCurrentUrl(),
				"https://tutorialsninja.com/demo/index.php?route=account/register");

//---------------------------------------------------------------------------------------------
		driver.get(PropertyFileUtil.getProperty("baseUrl"));
		navbarmenu.click_NavBara_MyAccountCTA();
		navbarmenu.click_Login_MyAccount();
		RHS_Menu_bar = new D_RHS_Menu_bar(driver);
		RHS_Menu_bar.click_register_rightbar_register();

		Softass.assertEquals(RegistrationPG.Get_Text_RegisterAccount(), "Register Account");
		Softass.assertEquals(driver.getCurrentUrl(),
				"https://tutorialsninja.com/demo/index.php?route=account/register");
		Softass.assertAll();
	}

//=========================================================================================
	@Test(priority = 8)
	public void TC_RF_008_Verify_Registering_Account_by_entering_different_passwords_into_Password_and_Password_Confirm_fields() {

		RegistrationPG = new B_Registration_page(driver);

		RegistrationPG = new B_Registration_page(driver);
		RegistrationPG.Send_FirstName(PropertyFileUtil.getProperty("FirstName"));
		RegistrationPG.Send_LastName(PropertyFileUtil.getProperty("LastName"));
		RegistrationPG.Send_Email(Reusable_details.reusableEmail());
		RegistrationPG.Send_Mobile(Reusable_details.reusableMobile());
		RegistrationPG.Send_password(PropertyFileUtil.getProperty("ValidPasword"));
		RegistrationPG.Send_ConfirmPassword(PropertyFileUtil.getProperty("InValidComfirmPasword"));

		RegistrationPG.select_SubscribeRadio_NO();
		RegistrationPG.Select_disclaimer();
		RegistrationPG.clickON_Continue();

		Softass.assertEquals(RegistrationPG.Get_alrtConfirmPassword(),
				"Password confirmation does not match password!");
		Softass.assertAll();

	}

//============================================================================================================
	@Test(priority = 9)
	public void TC_RF_009_Verify_Registering_Account_by_providing_the_existing_account_details() {

		RegistrationPG = new B_Registration_page(driver);
		RegistrationPG.Send_FirstName(PropertyFileUtil.getProperty("FirstName"));
		RegistrationPG.Send_LastName(PropertyFileUtil.getProperty("LastName"));
		RegistrationPG.Send_Email(PropertyFileUtil.getProperty("ExEmail"));
		RegistrationPG.Send_Mobile(PropertyFileUtil.getProperty("Extelephone"));
		RegistrationPG.Send_password(PropertyFileUtil.getProperty("ValidPasword"));
		RegistrationPG.Send_ConfirmPassword(PropertyFileUtil.getProperty("ValidComfirmPasword"));
		RegistrationPG.select_SubscribeRadio_YES();
		RegistrationPG.Select_disclaimer();
		RegistrationPG.clickON_Continue();

		Softass.assertEquals(RegistrationPG.Get_Worning_Emailexits(), "Warning: E-Mail Address is already registered!");
		Softass.assertAll();
	}

//=================================================================================================
	@Test(priority = 10)
	public void TC_RF_010_Verify_registraton_With_invalidEmail() throws InterruptedException {

		RegistrationPG = new B_Registration_page(driver);
		RegistrationPG.Send_FirstName(PropertyFileUtil.getProperty("FirstName"));
		RegistrationPG.Send_LastName(PropertyFileUtil.getProperty("LastName"));
		RegistrationPG.Send_Email(PropertyFileUtil.getProperty("Invalidemail01"));
		RegistrationPG.Send_Mobile(Reusable_details.reusableMobile());
		RegistrationPG.Send_password(PropertyFileUtil.getProperty("ValidPasword"));
		RegistrationPG.Send_ConfirmPassword(PropertyFileUtil.getProperty("ValidComfirmPasword"));
		RegistrationPG.select_SubscribeRadio_YES();
		RegistrationPG.Select_disclaimer();
		RegistrationPG.clickON_Continue();

		System.out.println("Validation message for Email - amotoori---->" + RegistrationPG.get_getDomProperty_Email());
		Softass.assertEquals(RegistrationPG.get_getDomProperty_Email(),
				"Please include an '@' in the email address. 'amotoori' is missing an '@'.");

		Thread.sleep(1000);
//---------------------------------------------------------------------------------------
		RegistrationPG.ClearEmail();
		RegistrationPG.Send_Email(PropertyFileUtil.getProperty("Invalidemail02"));
		RegistrationPG.clickON_Continue();

		System.out
				.println("Validation message for Email - amotoori@ ----> " + RegistrationPG.get_getDomProperty_Email());
		Softass.assertEquals(RegistrationPG.get_getDomProperty_Email(),
				"Please enter a part following '@'. 'amotoori@' is incomplete.");
//------------------------------------------------------------------------------------------------------
		RegistrationPG.ClearEmail();
		RegistrationPG.Send_Email(PropertyFileUtil.getProperty("Invalidemail03"));
		RegistrationPG.clickON_Continue();
		Thread.sleep(1000);

		Softass.assertEquals(RegistrationPG.get_getDomProperty_Email(), "'.' is used at a wrong position in 'gmail.'.");
		System.out.println(
				"Validation message for Email - amotoori@gmail. ----> " + RegistrationPG.get_getDomProperty_Email());

		Thread.sleep(1000);
//------------------------------------------------------------------------------------------------------------
		RegistrationPG.ClearEmail();
		RegistrationPG.Send_Email(PropertyFileUtil.getProperty("Invalidemail04"));
		RegistrationPG.clickON_Continue();

		Softass.assertEquals(RegistrationPG.getWorninginvalidemail04(), "E-Mail Address does not appear to be valid!");
		System.out.println(
				"Validation message for Email - amotoori@gmail ----> " + RegistrationPG.getWorninginvalidemail04());
		Thread.sleep(1000);
		Softass.assertAll();
	}
//====================================================================================================

	@Test(dataProvider = "InvalidMobilenumber", priority = 11)
	public void TC_RF_011Verify_Registering_Account_by_providing_invalid_phone_number(String invalidMobile) {

		RegistrationPG = new B_Registration_page(driver);
		RegistrationPG.Send_FirstName(PropertyFileUtil.getProperty("FirstName"));
		RegistrationPG.Send_LastName(PropertyFileUtil.getProperty("LastName"));
		RegistrationPG.Send_Email(Reusable_details.reusableEmail());
		RegistrationPG.Send_Mobile(PropertyFileUtil.getProperty("InvalidTelephone"));
		RegistrationPG.Send_password(PropertyFileUtil.getProperty("ValidPasword"));
		RegistrationPG.Send_ConfirmPassword(PropertyFileUtil.getProperty("ValidComfirmPasword"));
		RegistrationPG.select_SubscribeRadio_YES();
		RegistrationPG.Select_disclaimer();
		RegistrationPG.clickON_Continue();

		Assert.assertEquals(driver.getCurrentUrl(), "https://tutorialsninja.com/demo/index.php?route=account/register");

		Softass.assertAll();

	}

	@DataProvider(name = "InvalidMobilenumber")

	public Object[][] supplyInvalidMobileNumber() {
		Object[][] invalidmobile = { { "111" }, { "AAAAA" } };
		return invalidmobile;
	}

//==================================================================================================================
	@Test(priority = 12)
	public void TC_RF_012Verify_Registering_Account_by_using_Keyboard_keys() throws InterruptedException {
		Actions act = new Actions(driver);

		act.sendKeys(Keys.TAB).sendKeys(Keys.TAB).sendKeys(Keys.TAB).pause(Duration.ofSeconds(2))
				.sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).pause(Duration.ofSeconds(2)).sendKeys(Keys.ENTER)
				.build().perform();

		Thread.sleep(1000);

		for (int i = 1; i <= 23; i++) {
			act.sendKeys(Keys.TAB).perform();
		}

		act.sendKeys("somesh").sendKeys(Keys.TAB).sendKeys("landge").sendKeys(Keys.TAB)
				.sendKeys(Reusable_details.reusableEmail()).sendKeys(Keys.TAB)
				.sendKeys(Reusable_details.reusableMobile()).sendKeys(Keys.TAB)
				.sendKeys(PropertyFileUtil.getProperty("ValidPasword")).sendKeys(Keys.TAB)
				.sendKeys(PropertyFileUtil.getProperty("ValidComfirmPasword")).sendKeys(Keys.TAB).sendKeys(Keys.TAB)
				.sendKeys(Keys.TAB).sendKeys(Keys.SPACE).sendKeys(Keys.TAB).sendKeys(Keys.ENTER).build().perform();

		AccountSuccessPage = new C_account_success_page(driver);
		Assert.assertEquals(AccountSuccessPage.GetTest_Success_messgae_on_Accountpage(),
				"Your Account Has Been Created!");

		RHS_Menu_bar = new D_RHS_Menu_bar(driver);

		Softass.assertEquals(RHS_Menu_bar.Logout_RHSmenu_isDisplayed(), true);
		Softass.assertAll();

	}

//===============================================================================================
	@Test(priority = 13)
	public static void TC_RF_013Verify_all_fields_in_the_Register_Account_page_have_proper_placeholders() {

		RegistrationPG = new B_Registration_page(driver);

		Softass.assertEquals(RegistrationPG.get_placeholder_firstname(), "First Name");
		Softass.assertEquals(RegistrationPG.get_placeholder_lastname(), "Last Name");
		Softass.assertEquals(RegistrationPG.get_placeholder_email(), "E-Mail");
		Softass.assertEquals(RegistrationPG.get_placeholder_mobile(), "Telephone");
		Softass.assertEquals(RegistrationPG.get_placeholder_password(), "Password");
		Softass.assertEquals(RegistrationPG.get_placeholder_confirmpass(), "Password Confirm");
		Softass.assertAll();
	}

//========================================================================================================
	@Test(priority = 14)
	public static void TC_RF_014_Verify_all_mandatory_fields_in_Register_Account() {

		String expectedContent = "\"* \"";
		String expectedColor = "rgb(255, 0, 0)";

		WebElement firstname = driver.findElement(By.cssSelector("label[for='input-firstname']"));

		JavascriptExecutor jsee = (JavascriptExecutor) driver;

		String ashstrictFN = (String) jsee.executeScript(
				"return window.getComputedStyle(arguments[0],'::before').getPropertyValue('content','color');",
				firstname);

		String ashstrictcolorFN = (String) jsee.executeScript(
				"return window.getComputedStyle(arguments[0],'::before').getPropertyValue('color');", firstname);

		Softass.assertEquals(ashstrictFN, expectedContent);
		Softass.assertEquals(ashstrictcolorFN, expectedColor);

		Softass.assertAll();

	}
//================================================================================================

	@Test(priority = 15)
	public void TC_RF_015_Verify_details_that_are_provided_while_Registering_Account_are_stored_in_Database() {
		RegistrationPG = new B_Registration_page(driver);
		RegistrationPG.Send_FirstName(PropertyFileUtil.getProperty("FirstName"));
		RegistrationPG.Send_LastName(PropertyFileUtil.getProperty("LastName"));

		String enteredEmailData = Reusable_details.reusableEmail();
		RegistrationPG.Send_Email(enteredEmailData);

		RegistrationPG.Send_password(PropertyFileUtil.getProperty("ValidPasword"));
		RegistrationPG.Send_ConfirmPassword(PropertyFileUtil.getProperty("ValidComfirmPasword"));

		RegistrationPG.select_SubscribeRadio_NO();

		RegistrationPG.Select_disclaimer();

		RegistrationPG.clickON_Continue();

		// Database credentials
		String jdbcURL = "jdbc:mysql://localhost:3306/opencart_db";
		String dbUser = "root";
		String dbPassword = "";

		// SQL query
		String sqlQuery = "SELECT * FROM oc_customer";

		// JDBC objects
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		String firstName = null;
		String lastName = null;
		String email = null;
		int newsletter = 0;

		try {
			// Step 1: Register JDBC driver (optional in newer versions)

			// Step 2: Open a connection
			connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
			System.out.println("Connected to the database!");

			// Step 3: Create a statement
			statement = connection.createStatement();

			// Step 4: Execute the query
			resultSet = statement.executeQuery(sqlQuery);

			// Step 5: Process the ResultSet
			while (resultSet.next()) {
				firstName = resultSet.getString("firstname"); // Replace with your column name
				lastName = resultSet.getString("lastname"); // Replace with your column name
				email = resultSet.getString("email");
				newsletter = resultSet.getInt("newsletter");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// Step 6: Close resources
			try {
				if (resultSet != null)
					resultSet.close();
				if (statement != null)
					statement.close();
				if (connection != null)
					connection.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		Softass.assertEquals(firstName, PropertyFileUtil.getProperty("FirstName"));
		Softass.assertEquals(lastName, PropertyFileUtil.getProperty("LastName"));
		Softass.assertEquals(email, enteredEmailData);
		Softass.assertEquals(newsletter, 1);
		Softass.assertAll();
	}

//==================================================================================================
	@Test(priority = 16)
	public void TC_RF_016_verify_Registering_Account_By_Entering_Only_Spaces() {

		RegistrationPG = new B_Registration_page(driver);
		RegistrationPG.Send_FirstName("     ");
		RegistrationPG.Send_LastName("     ");
		RegistrationPG.Send_Email("     ");
		RegistrationPG.Send_Mobile("     ");
		RegistrationPG.Send_password("     ");
		RegistrationPG.Send_ConfirmPassword("     ");
		RegistrationPG.Select_disclaimer();
		RegistrationPG.clickON_Continue();

		if (browserName.equals("chrome") || browserName.equals("Edge")) {
			Softass.assertEquals(RegistrationPG.Get_alertmeg_for_Firstname(),
					"First Name must be between 1 and 32 characters!");
			Softass.assertEquals(RegistrationPG.Get_alertmeg_for_Lastname(),
					"Last Name must be between 1 and 32 characters!");
			Softass.assertEquals(RegistrationPG.Get_alertmeg_for_Email(),
					"E-Mail Address does not appear to be valid!");

		} else {

			Softass.assertEquals(RegistrationPG.get_getDomProperty_Email(), "Please enter an email address.");
		}
		Softass.assertAll();
	}

//==================================================================================================================
	@Test(dataProvider = "passwordcomplixitycheck", priority = 17)
	public void TC_RF_017_Verify_whether_the_Password_fields_in_the_Register_Account_page_are_following_Password_Complexity_Standards(
			String password) {

		RegistrationPG = new B_Registration_page(driver);

		RegistrationPG.Send_FirstName(PropertyFileUtil.getProperty("FirstName"));
		RegistrationPG.Send_LastName(PropertyFileUtil.getProperty("LastName"));
		RegistrationPG.Send_Email(Reusable_details.reusableEmail());
		RegistrationPG.Send_Mobile(Reusable_details.reusableMobile());
		RegistrationPG.Send_password(password);
		RegistrationPG.Send_ConfirmPassword(password);
		RegistrationPG.select_SubscribeRadio_YES();
		RegistrationPG.Select_disclaimer();
		RegistrationPG.clickON_Continue();
		AccountSuccessPage = new C_account_success_page(driver);

		Softass.assertEquals(RegistrationPG.Get_alertmeg_for_Password(),
				"Password must be between 4 and 20 characters!");

		Softass.assertAll();
	}

	@DataProvider(name = "passwordcomplixitycheck")

	public Object[][] supplypassword() {
		Object[][] passwords = { { "123" }, { "12345" }, { "abcdefghi" }, { "abcd1234" }, { "abcd123$" },
				{ "ABCD456#" } };
		return passwords;
	}

//======================================================================================================	
	@Test(priority = 19)
	public void TC_RF_019_Verify_whether_the_leading_and_trailing_spaces_entered_into_the_Register_Account_fields_are_trimmed() {

		RegistrationPG = new B_Registration_page(driver);

		String firstname = " somesh  ";
		String Lastname = " landge   ";
		String Email = "    " + Reusable_details.reusableEmail() + "   ";
		String Mobile = "  " + Reusable_details.reusableMobile() + "    ";

		RegistrationPG.Send_FirstName(firstname);
		RegistrationPG.Send_LastName(Lastname);
		RegistrationPG.Send_Email(Email);
		RegistrationPG.Send_Mobile(Mobile);
		RegistrationPG.Send_password("  Test@123  ");
		RegistrationPG.Send_ConfirmPassword("  Test@123  ");
		RegistrationPG.Select_disclaimer();
		RegistrationPG.clickON_Continue();

		if (browserName.equals("chrome") || browserName.equals("Edge")) {
			AccountSuccessPage = new C_account_success_page(driver);
			AccountSuccessPage.click_ucc_ContinueCTA();

			myAcpage = new D_MyAccount_page(driver);
			myAcpage.click_MyAC_editAccouintInfo();

			MyAC_Editpage = new G_Account_EditPage(driver);
			Softass.assertEquals(MyAC_Editpage.getFirstName_Editpg(), firstname.trim());
			Softass.assertEquals(MyAC_Editpage.getLastName_Editpg(), Lastname.trim());
			Softass.assertEquals(MyAC_Editpage.getEmail_Editpg(), Email.trim());
			Softass.assertEquals(MyAC_Editpage.getMobile_Editpg(), Mobile.trim());
		} else {
			Softass.assertEquals(RegistrationPG.get_getDomProperty_Email(), "Please enter an email address.");
			logger.info(
					"Test excecuted in firefox - Registration is not allowed due heading and tailing space in email");
		}

		Softass.assertAll();
	}

//==============================================================================================
	@Test(priority = 20)
	public void TC_RF_020_Verify_whether_the_Privacy_Policy_checkbox_option_is_not_selected_by_default() {

		boolean PrivacyPolicy_isselected = driver.findElement(By.xpath("//input[@name='agree']")).isSelected();

		Softass.assertEquals(PrivacyPolicy_isselected, false);
		Softass.assertAll();

	}

//==========================================================================================
	@Test(priority = 21)
	public void TC_RF_021_Verify_Registering_the_Account_without_selecting_the_Privacy_Policy_checkbox_option() {

		RegistrationPG = new B_Registration_page(driver);
		RegistrationPG.Send_FirstName(PropertyFileUtil.getProperty("FirstName"));
		RegistrationPG.Send_LastName(PropertyFileUtil.getProperty("LastName"));
		RegistrationPG.Send_Email(Reusable_details.reusableEmail());
		RegistrationPG.Send_Mobile(Reusable_details.reusableMobile());
		RegistrationPG.Send_password(PropertyFileUtil.getProperty("ValidPasword"));
		RegistrationPG.Send_ConfirmPassword(PropertyFileUtil.getProperty("ValidComfirmPasword"));
		RegistrationPG.select_SubscribeRadio_YES();
		RegistrationPG.clickON_Continue();

		Softass.assertEquals("https://tutorialsninja.com/demo/index.php?route=account/register",
				"https://tutorialsninja.com/demo/index.php?route=account/register");

		Softass.assertEquals(RegistrationPG.Get_WorningDisclaimer(), "Warning: You must agree to the Privacy Policy!");
		Softass.assertAll();
	}

//========================================================================================
	@Test(priority = 22)
	public void TC_RF_022_Verify_the_Password_text_entered_into_the_Password_and_Password_Confirm_field_of_Register_Account_functionality_is_toggled_to_hide_its_visibility() {

		RegistrationPG = new B_Registration_page(driver);
		Softass.assertEquals(RegistrationPG.get_DomAttributfotVisibility_password(), "password");
		Softass.assertEquals(RegistrationPG.get_DomAttributfotVisibility_CNF_password(), "password");
		Softass.assertAll();
	}

//================================================================================================
	@Test(priority = 23)
	public void TC_RF_023_Verify_navigating_to_other_pages_using_options_or_links_provided_on_the_Register_Account_page()
			throws InterruptedException {

		navbarmenu = new A_Navigation_bar(driver);
		navbarmenu.click_NavBara_contactust();
		String contactusurl = driver.getCurrentUrl();
		driver.navigate().back();

		navbarmenu.click_Header_withlist();
		String wishlist = driver.getCurrentUrl();
		driver.navigate().back();

		navbarmenu.click_Header_shopppingcard();
		String cardurl = driver.getCurrentUrl();
		driver.navigate().back();

		navbarmenu.click_Header_checkout();
		String checkouturl = driver.getCurrentUrl();
		driver.navigate().back();

		navbarmenu.click_Header_QAfoxLogo();
		String QAhomeurl = driver.getCurrentUrl();
		driver.navigate().back();

		navbarmenu.click_breadc_homeicon();
		String breadhome = driver.getCurrentUrl();
		driver.navigate().back();

		navbarmenu.click_breadc_myaccount();
		String breadMyAC = driver.getCurrentUrl();
		driver.navigate().back();

//		Thread.sleep(1000);
//		navbarmenu.click_breadc_register();
//		String breadRegister = driver.getCurrentUrl();

		RegistrationPG = new B_Registration_page(driver);
		RegistrationPG.click_Header_loginhyperlink();
		String loginhyperlink = driver.getCurrentUrl();
		driver.navigate().back();

		RHS_Menu_bar = new D_RHS_Menu_bar(driver);
		RHS_Menu_bar.click_register_rightbar_login();
		String login_rightbox = driver.getCurrentUrl();
		driver.navigate().back();

		RHS_Menu_bar.click_register_rightbar_register();
		String register_rightbox = driver.getCurrentUrl();

		RHS_Menu_bar.click_register_rightbar_forgotpass();
		String forgotpass_rightbox = driver.getCurrentUrl();
		driver.navigate().back();

		RHS_Menu_bar.click_register_rightbar_myAC();
		String myac_rightbox = driver.getCurrentUrl();
		driver.navigate().back();

		RHS_Menu_bar.click_register_rightbar_addressbook();
		String addressbook_rightbox = driver.getCurrentUrl();
		driver.navigate().back();

		RHS_Menu_bar.click_register_rightbar_wishlist();
		String wishlist_rightbox = driver.getCurrentUrl();
		driver.navigate().back();

		RHS_Menu_bar.click_register_rightbar_orderHistory();
		String orderhistory_rightbox = driver.getCurrentUrl();
		driver.navigate().back();

		RHS_Menu_bar.click_register_rightbar_Download();
		String download_rightbox = driver.getCurrentUrl();
		driver.navigate().back();

		RHS_Menu_bar.click_register_rightbar_RecurrinhPaymnet();
		String recurringpayment_rightbox = driver.getCurrentUrl();
		driver.navigate().back();

		RHS_Menu_bar.click_register_rightbar_rewardPoint();
		String rewardpoint_rightbox = driver.getCurrentUrl();
		driver.navigate().back();

		RHS_Menu_bar.click_register_rightbar_Return();
		String return_rightbox = driver.getCurrentUrl();
		driver.navigate().back();

		RHS_Menu_bar.click_register_rightbar_Transaction();
		String transaction_rightbox = driver.getCurrentUrl();
		driver.navigate().back();

		RHS_Menu_bar.click_register_rightbar_newsletter();
		String newletter_rightbox = driver.getCurrentUrl();
		driver.navigate().back();

		Softass.assertEquals(contactusurl, PropertyFileUtil.getProperty("Headercontactusurl"));
		Softass.assertEquals(wishlist, PropertyFileUtil.getProperty("wishlistlink"));
		Softass.assertEquals(cardurl, PropertyFileUtil.getProperty("cardURL"));
		Softass.assertEquals(checkouturl, PropertyFileUtil.getProperty("checkouturl"));
		Softass.assertEquals(QAhomeurl, PropertyFileUtil.getProperty("QAhomeurl"));
		Softass.assertEquals(breadhome, PropertyFileUtil.getProperty("breadhome"));
		Softass.assertEquals(breadMyAC, PropertyFileUtil.getProperty("breadMyAC"));
//		 Softass.assertEquals(breadRegister,
//		 "https://tutorialsninja.com/demo/index.php?route=account/register");
		Softass.assertEquals(loginhyperlink, PropertyFileUtil.getProperty("loginhyperlink"));
		Softass.assertEquals(login_rightbox, PropertyFileUtil.getProperty("loginrightbox"));
		Softass.assertEquals(register_rightbox, PropertyFileUtil.getProperty("register_rightbox"));
		Softass.assertEquals(forgotpass_rightbox, PropertyFileUtil.getProperty("contactusurl"));
		Softass.assertEquals(myac_rightbox, PropertyFileUtil.getProperty("forgotpass_rightbox"));
		Softass.assertEquals(addressbook_rightbox, PropertyFileUtil.getProperty("addressbook_rightbox"));
		Softass.assertEquals(wishlist_rightbox, PropertyFileUtil.getProperty("wishlist_rightbox"));
		Softass.assertEquals(orderhistory_rightbox, PropertyFileUtil.getProperty("orderhistory_rightbox"));
		Softass.assertEquals(download_rightbox, PropertyFileUtil.getProperty("download_rightbox"));
		Softass.assertEquals(recurringpayment_rightbox, PropertyFileUtil.getProperty("recurringpayment_rightbox"));
		Softass.assertEquals(rewardpoint_rightbox, PropertyFileUtil.getProperty("rewardpoint_rightbox"));
		Softass.assertEquals(return_rightbox, PropertyFileUtil.getProperty("return_rightbox"));
		Softass.assertEquals(transaction_rightbox, PropertyFileUtil.getProperty("transaction_rightbox"));
		Softass.assertEquals(newletter_rightbox, PropertyFileUtil.getProperty("newletter_rightbox"));
		Softass.assertAll();

	}

//===================================================================================================
	@Test(priority = 24)
	public void TC_RF_024_Verify_Registring_Account_by_filling_Password_field_and_not_filling_Password_Confirm_field() {

		RegistrationPG = new B_Registration_page(driver);
		RegistrationPG.Send_FirstName(PropertyFileUtil.getProperty("FirstName"));
		RegistrationPG.Send_LastName(PropertyFileUtil.getProperty("LastName"));
		RegistrationPG.Send_Email(Reusable_details.reusableEmail());
		RegistrationPG.Send_Mobile(Reusable_details.reusableMobile());
		RegistrationPG.Send_password(PropertyFileUtil.getProperty("ValidPasword"));
		RegistrationPG.Select_disclaimer();
		RegistrationPG.clickON_Continue();

		Softass.assertEquals(RegistrationPG.Get_Worning_NOConfiPassword(),
				"Password confirmation does not match password!");

		Softass.assertEquals(driver.getCurrentUrl(),
				"https://tutorialsninja.com/demo/index.php?route=account/register");
		Softass.assertAll();

	}

//======================================================================================================
	@Test(priority = 25)
	public void TC_RF_025_Verify_Breadcrumb_PageHeading_PageURL_PageTitle_of_Register_AccountPage() {

		RegistrationPG = new B_Registration_page(driver);
		Softass.assertEquals(RegistrationPG.isDisplyBreadcrum_Register(), true);
		Softass.assertEquals(driver.getTitle(), "Register Account");
		Softass.assertEquals(driver.getCurrentUrl(),
				"https://tutorialsninja.com/demo/index.php?route=account/register");
		Softass.assertAll();
	}

//=======================================================================
	@Test(priority = 26)
	public void TC_RF_026_Verify_the_UI_of_Register_Account_page() throws IOException {

		TakeScreenshotClass.takeScreenshot("registration page1", driver);
		Softass.assertAll();

	}

//==========================================================================
	@Test(dataProvider = "DataProviderforTest", priority = 27)
	public void TC_RF_027_Verify_Register_Account_functionality_in_all_the_supported_environments(String browserName) {

		RegistrationPG = new B_Registration_page(driver);
		RegistrationPG.Send_FirstName(PropertyFileUtil.getProperty("FirstName"));
		RegistrationPG.Send_LastName(PropertyFileUtil.getProperty("LastName"));
		RegistrationPG.Send_Email(Reusable_details.reusableEmail());
		RegistrationPG.Send_Mobile(Reusable_details.reusableMobile());
		RegistrationPG.Send_password(PropertyFileUtil.getProperty("ValidPasword"));
		RegistrationPG.Send_ConfirmPassword(PropertyFileUtil.getProperty("ValidComfirmPasword"));
		RegistrationPG.Select_disclaimer();
		RegistrationPG.clickON_Continue();

		AccountSuccessPage = new C_account_success_page(driver);
		RHS_Menu_bar = new D_RHS_Menu_bar(driver);
		Assert.assertTrue(RHS_Menu_bar.Logout_RHSmenu_isDisplayed());
		Assert.assertTrue(AccountSuccessPage.IsDisply_Success_messgae_on_Accountpage());
		Softass.assertAll();

	}

	@DataProvider(name = "DataProviderforTest")

	public Object[][] DataProviderforTest() {
		Object[][] data = { { "chrome" }, { "Firefox" }, { "Edge" } };
		return data;

	}

}

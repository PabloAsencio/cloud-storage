package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	private static final String SIGNUP_ERROR_USER_ALREADY_EXISTS = "The username already exists";
	private static final String SIGNUP_SUCCESS = "You successfully signed up! Please continue to the login page.";

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private final String serverURL = "http://localhost:";

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get(serverURL + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void getSignUpPage() {
		driver.get(serverURL + this.port + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());
	}

	@Test
	public void logInInvalidUser() {
		driver.get(serverURL + this.port + "/login");
		LoginPage loginPage = new LoginPage(driver);
		Assertions.assertEquals("Invalid username or password", loginPage.loginInvalidUser("username", "password").getErrorMessage());
	}

	@Test
	public void logInValidUser() {
		driver.get(serverURL + this.port + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.loginValidUser("testUser", "testPassword");
		Assertions.assertEquals("Home", driver.getTitle());
	}

	@Test
	public void signUpExistingUser() {
		driver.get(serverURL + this.port + "/signup");
		SignUpPage signUpPage = new SignUpPage(driver);
		String errorMessage = signUpPage.registerNewUser("John", "Doe", "testUser", "testPassword").getErrorMessage();
		Assertions.assertEquals(SIGNUP_ERROR_USER_ALREADY_EXISTS, errorMessage);
	}

	@Test
	public void signUpNewValidUser() {
		driver.get(serverURL + this.port + "/signup");
		SignUpPage signUpPage = new SignUpPage(driver);
		String successMessage = signUpPage.registerNewUser("Jane", "Smith", "jane_smith", "newPassword").getSuccessMessage();
		Assertions.assertEquals(SIGNUP_SUCCESS, successMessage);
	}

}

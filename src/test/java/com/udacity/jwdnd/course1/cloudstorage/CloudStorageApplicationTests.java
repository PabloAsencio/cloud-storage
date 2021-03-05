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
	private static final String LOGOUT_MESSAGE = "You have been logged out";

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
	public void getLoginPageFromSignUpPage() {
		driver.get(serverURL + this.port + "/signup");
		SignUpPage signUpPage = new SignUpPage(driver);
		signUpPage.goToLoginPage();
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void getSignUpPageFromLoginPage() {
		driver.get(serverURL + this.port + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.goToSignUp();
		Assertions.assertEquals("Sign Up", driver.getTitle());
	}

	@Test
	public void goingToHomePageUnauthenticatedRedirectsToLoginPage() {
		driver.get(serverURL + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
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

	@Test
	public void signUpNewValidUserAndGoToLoginPage() {
		String username = "original";
		String password = "yetAnotherPassword";
		driver.get(serverURL + this.port + "/signup");
		SignUpPage signUpPage = new SignUpPage(driver);
		signUpPage.registerNewUser("Guenther", "Frager", username, password).goToLoginPageAfterSuccess();
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void signUpNewValidUserAndLogin() {
		String username = "maestro";
		String password = "anotherPassword";
		driver.get(serverURL + this.port + "/signup");
		SignUpPage signUpPage = new SignUpPage(driver);
		SignUpPage afterSignup = signUpPage.registerNewUser("Johann Sebastian", "Mastropiero", username, password);
		LoginPage loginPage = afterSignup.goToLoginPageAfterSuccess();
		loginPage.loginValidUser(username, password);
		Assertions.assertEquals("Home", driver.getTitle());
	}

	@Test
	public void goToSignUpPageFromLoginPageThenSignValidUserUp() {
		String username = "corto-maltese";
		String password = "andYetOneMorePassword";
		driver.get(serverURL + this.port + "/login");
		LoginPage start = new LoginPage(driver);
		SignUpPage signUpPage = start.goToSignUp();
		String successMessage = signUpPage.registerNewUser("Corto", "Maltese", username, password).getSuccessMessage();
		Assertions.assertEquals(SIGNUP_SUCCESS, successMessage);

	}

	@Test
	public void goToSignUpPageFromLoginPageThenSignNewUserUpAndLogin() {
		String username = "superlopez";
		String password = "oneMorePassword";
		driver.get(serverURL + this.port + "/login");
		LoginPage start = new LoginPage(driver);
		SignUpPage signUpPage = start.goToSignUp();
		LoginPage loginPage = signUpPage.registerNewUser("Juan", "Lopez", username, password).goToLoginPageAfterSuccess();
		loginPage.loginValidUser(username, password);
		Assertions.assertEquals("Home", driver.getTitle());
	}

	@Test
	public void logInValidUserAndLogOut() {
		driver.get(serverURL + this.port + "/login");
		LoginPage loginPage = new LoginPage(driver);
		HomePage homePage = loginPage.loginValidUser("testUser", "testPassword");
		String logoutMessage = homePage.logout().getLogoutMessage();
		Assertions.assertEquals(LOGOUT_MESSAGE, logoutMessage);
	}

	@Test
	public void logOutAndNavigateToHomeRedirectsToLogin() {
		driver.get(serverURL + this.port + "/login");
		LoginPage loginPage = new LoginPage(driver);
		HomePage homePage = loginPage.loginValidUser("testUser", "testPassword");
		homePage.logout();
		driver.get(serverURL + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void signUpNewUserLogInAndLogOut() {
		String username = "maestro";
		String password = "anotherPassword";
		driver.get(serverURL + this.port + "/signup");
		SignUpPage signUpPage = new SignUpPage(driver);
		SignUpPage afterSignup = signUpPage.registerNewUser("Johann Sebastian", "Mastropiero", username, password);
		LoginPage loginPage = afterSignup.goToLoginPageAfterSuccess();
		HomePage homePage = loginPage.loginValidUser(username, password);
		String logoutMessage = homePage.logout().getLogoutMessage();
		Assertions.assertEquals(LOGOUT_MESSAGE, logoutMessage);
	}

}

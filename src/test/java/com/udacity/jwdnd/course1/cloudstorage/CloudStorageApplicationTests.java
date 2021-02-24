package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

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

}

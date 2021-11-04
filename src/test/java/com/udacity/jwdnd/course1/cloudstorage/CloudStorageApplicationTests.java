package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

    private static final String SIGNUP_ERROR_USER_ALREADY_EXISTS = "The username already exists";
    private static final String SIGNUP_SUCCESS = "You successfully signed up! Please continue to the login page.";
    private static final String LOGOUT_MESSAGE = "You have been logged out";
    public static final String DEFAULT_NOTE_TITLE = "My new note title";
    public static final String DEFAULT_NOTE_DESCRIPTION = "My new note description";
    public static final String DEFAULT_CREDENTIAL_URL = "https://www.example.com";
    public static final String DEFAULT_CREDENTIAL_USERNAME = "admin";
    public static final String DEFAULT_CREDENTIAL_PASSWORD = "password";

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
        assertEquals("Login", driver.getTitle());
    }

    @Test
    public void getSignUpPage() {
        driver.get(serverURL + this.port + "/signup");
        assertEquals("Sign Up", driver.getTitle());
    }

    @Test
    public void getLoginPageFromSignUpPage() {
        driver.get(serverURL + this.port + "/signup");
        SignUpPage signUpPage = new SignUpPage(driver);
        signUpPage.goToLoginPage();
        assertEquals("Login", driver.getTitle());
    }

    @Test
    public void getSignUpPageFromLoginPage() {
        driver.get(serverURL + this.port + "/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.goToSignUp();
        assertEquals("Sign Up", driver.getTitle());
    }

    @Test
    public void goingToHomePageUnauthenticatedRedirectsToLoginPage() {
        driver.get(serverURL + this.port + "/home");
        assertEquals("Login", driver.getTitle());
    }

    @Test
    public void logInInvalidUser() {
        driver.get(serverURL + this.port + "/login");
        LoginPage loginPage = new LoginPage(driver);
        assertEquals("Invalid username or password", loginPage.loginInvalidUser("username", DEFAULT_CREDENTIAL_PASSWORD).getErrorMessage());
    }

    @Test
    public void logInValidUser() {
        driver.get(serverURL + this.port + "/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginValidUser("testUser", "testPassword");
        assertEquals("Home", driver.getTitle());
    }

    @Test
    public void signUpExistingUser() {
        driver.get(serverURL + this.port + "/signup");
        SignUpPage signUpPage = new SignUpPage(driver);
        String errorMessage = signUpPage.registerNewUser("John", "Doe", "testUser", "testPassword").getErrorMessage();
        assertEquals(SIGNUP_ERROR_USER_ALREADY_EXISTS, errorMessage);
    }

    @Test
    public void signUpNewValidUser() {
        driver.get(serverURL + this.port + "/signup");
        SignUpPage signUpPage = new SignUpPage(driver);
        String successMessage = signUpPage.registerNewUser("Jane", "Smith", "jane_smith", "newPassword").getSuccessMessage();
        assertEquals(SIGNUP_SUCCESS, successMessage);
    }

    @Test
    public void signUpNewValidUserAndGoToLoginPage() {
        String username = "original";
        String password = "yetAnotherPassword";
        driver.get(serverURL + this.port + "/signup");
        SignUpPage signUpPage = new SignUpPage(driver);
        signUpPage.registerNewUser("Guenther", "Frager", username, password).goToLoginPageAfterSuccess();
        assertEquals("Login", driver.getTitle());
    }

    @Test
    public void signUpNewValidUserAndLogin() {
        String username = "kermit";
        String password = "soManyPasswords";
        driver.get(serverURL + this.port + "/signup");
        SignUpPage signUpPage = new SignUpPage(driver);
        SignUpPage afterSignup = signUpPage.registerNewUser("Kermit", "The Frog", username, password);
        LoginPage loginPage = afterSignup.goToLoginPageAfterSuccess();
        loginPage.loginValidUser(username, password);
        assertEquals("Home", driver.getTitle());
    }

    @Test
    public void goToSignUpPageFromLoginPageThenSignValidUserUp() {
        String username = "filemon";
        String password = "dontForgetYourPassword";
        driver.get(serverURL + this.port + "/login");
        LoginPage start = new LoginPage(driver);
        SignUpPage signUpPage = start.goToSignUp();
        String successMessage = signUpPage.registerNewUser("Filemon", "Pi", username, password).getSuccessMessage();
        assertEquals(SIGNUP_SUCCESS, successMessage);

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
        assertEquals("Home", driver.getTitle());
    }

    @Test
    public void logInValidUserAndLogOut() {
        driver.get(serverURL + this.port + "/login");
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = loginPage.loginValidUser("testUser", "testPassword");
        String logoutMessage = homePage.logout().getLogoutMessage();
        assertEquals(LOGOUT_MESSAGE, logoutMessage);
    }

    @Test
    public void logOutAndNavigateToHomeRedirectsToLogin() {
        driver.get(serverURL + this.port + "/login");
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = loginPage.loginValidUser("testUser", "testPassword");
        homePage.logout();
        driver.get(serverURL + this.port + "/home");
        assertEquals("Login", driver.getTitle());
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
        assertEquals(LOGOUT_MESSAGE, logoutMessage);
    }

    @Sql(statements = "DELETE FROM NOTES;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void createNote() {
        HomePage homePage = loginDefaultUser();
		homePage.openNotesTab();
        final String title = DEFAULT_NOTE_TITLE;
        final String description = DEFAULT_NOTE_DESCRIPTION;
        ResultPage resultPage = homePage.createNote(title, description);
        assertTrue(resultPage.isSuccess());
        homePage = resultPage.goToHomePage();
        homePage.openNotesTab();
        assertTrue(homePage.noteExists(title, description));
    }

    @Sql(statements = "DELETE FROM NOTES;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void createNoteWithDuplicateTitleFails() {
        HomePage homePage = createDefaultNote();
        homePage.openNotesTab();
        ResultPage resultPage = homePage.createNote(DEFAULT_NOTE_TITLE, "New description");
        assertFalse(resultPage.isSuccess());
        homePage = resultPage.goToHomePage();
        homePage.openNotesTab();
        assertTrue(homePage.noteExists(DEFAULT_NOTE_TITLE, DEFAULT_NOTE_DESCRIPTION));
    }

    @Sql(statements = "DELETE FROM NOTES;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void editNote() {
        final String newTitle = "My new title";
        final String newDescription = "My new description";
        HomePage homePage = createDefaultNote();
        homePage.openNotesTab();
        ResultPage resultPage = homePage.editNote(DEFAULT_NOTE_TITLE, newTitle, newDescription);
        assertTrue(resultPage.isSuccess());
        homePage = resultPage.goToHomePage();
        homePage.openNotesTab();
        assertFalse(homePage.noteExists(DEFAULT_NOTE_TITLE, DEFAULT_NOTE_DESCRIPTION));
        assertTrue(homePage.noteExists(newTitle, newDescription));
    }

    @Test
    public void deleteNote() {
        HomePage homePage = createDefaultNote();
        homePage.openNotesTab();
        ResultPage resultPage = homePage.deleteNote(DEFAULT_NOTE_TITLE);
        assertTrue(resultPage.isSuccess());
        homePage = resultPage.goToHomePage();
        homePage.openNotesTab();
        assertFalse(homePage.noteExists(DEFAULT_NOTE_TITLE, DEFAULT_NOTE_DESCRIPTION));
    }

    @Sql(statements = "DELETE FROM CREDENTIALS;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void createCredential() {
        HomePage homePage = loginDefaultUser();
        homePage.openCredentialsTab();
        Credential credential = new Credential();
        credential.setUrl(DEFAULT_CREDENTIAL_URL);
        credential.setUsername(DEFAULT_CREDENTIAL_USERNAME);
        credential.setPassword(DEFAULT_CREDENTIAL_PASSWORD);
        ResultPage resultPage = homePage.createCredential(credential);
        assertTrue(resultPage.isSuccess());
        homePage = resultPage.goToHomePage();
        homePage.openCredentialsTab();
        assertTrue(homePage.credentialExists(credential));
        assertEquals(DEFAULT_CREDENTIAL_PASSWORD, homePage.getPlainTextPassword(credential));
    }

    @Sql(statements = "DELETE FROM CREDENTIALS;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void updateCredential() {
        Credential defaultCredential = new Credential();
        defaultCredential.setUrl(DEFAULT_CREDENTIAL_URL);
        defaultCredential.setUsername(DEFAULT_CREDENTIAL_USERNAME);
        defaultCredential.setPassword(DEFAULT_CREDENTIAL_PASSWORD);
        Credential updatedCredential = new Credential();
        updatedCredential.setUrl("https://www.test.com");
        updatedCredential.setUsername("administrator");
        updatedCredential.setPassword("different-password");
        HomePage homePage = createDefaultCredential();
        homePage.openCredentialsTab();
        ResultPage resultPage = homePage.updateCredential(defaultCredential, updatedCredential);
        assertTrue(resultPage.isSuccess());
        homePage = resultPage.goToHomePage();
        homePage.openCredentialsTab();
        assertFalse(homePage.credentialExists(defaultCredential));
        assertTrue(homePage.credentialExists(updatedCredential));
        assertNotEquals(DEFAULT_CREDENTIAL_PASSWORD, homePage.getPlainTextPassword(updatedCredential));
        assertEquals("different-password", homePage.getPlainTextPassword(updatedCredential));
    }

    @Test
    public void deleteCredential() {
        Credential credential = new Credential();
        credential.setUrl(DEFAULT_CREDENTIAL_URL);
        credential.setUsername(DEFAULT_CREDENTIAL_USERNAME);
        credential.setPassword(DEFAULT_CREDENTIAL_PASSWORD);
        HomePage homePage = createDefaultCredential();
        homePage.openCredentialsTab();
        ResultPage resultPage = homePage.deleteCredential(credential);
        assertTrue(resultPage.isSuccess());
        homePage = resultPage.goToHomePage();
        homePage.openCredentialsTab();
        assertFalse(homePage.credentialExists(credential));
    }

    private HomePage createDefaultNote() {
        HomePage homePage = loginDefaultUser();
        homePage.openNotesTab();
        final String title = DEFAULT_NOTE_TITLE;
        final String description = DEFAULT_NOTE_DESCRIPTION;
        ResultPage resultPage = homePage.createNote(title, description);
        return resultPage.goToHomePage();
    }

    private HomePage createDefaultCredential() {
        HomePage homePage = loginDefaultUser();
        homePage.openCredentialsTab();
        Credential credential = new Credential();
        credential.setUrl(DEFAULT_CREDENTIAL_URL);
        credential.setUsername(DEFAULT_CREDENTIAL_USERNAME);
        credential.setPassword(DEFAULT_CREDENTIAL_PASSWORD);
        ResultPage resultPage = homePage.createCredential(credential);
        return resultPage.goToHomePage();
    }

    private HomePage loginDefaultUser() {
        driver.get(serverURL + this.port + "/login");
        LoginPage loginPage = new LoginPage(driver);
        return loginPage.loginValidUser("testUser", "testPassword");
    }


}

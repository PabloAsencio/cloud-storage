package com.udacity.jwdnd.course1.cloudstorage;

import org.apache.juli.logging.Log;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
    @FindBy(id = "inputUsername")
    @CacheLookup
    private WebElement inputUsername;

    @FindBy(id = "inputPassword")
    @CacheLookup
    private WebElement inputPassword;

    @FindBy(id = "submitButton")
    @CacheLookup
    private WebElement submitButton;

    @FindBy(id = "signup-link")
    @CacheLookup
    private WebElement signupLink;

    @FindBy(id = "message-error")
    private WebElement errorMessage;

    @FindBy(id = "message-logout")
    private WebElement logoutMessage;

    private final WebDriver driver;
    // The native methods sendKeys and click do not work properly when switching pages several times
    // due to race conditions. Using JavascriptExecutor to click elements and set the value of
    // the input fields circumvents this issue.
    private final JavascriptExecutor executor;
    private final WebDriverWait wait;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.executor = (JavascriptExecutor) driver;
        this.wait = new WebDriverWait(driver, 5);
        PageFactory.initElements(driver, this);
    }

    /**
     * Clicks on the sign up link
     * @return a new SignUpPage
     */
    public SignUpPage goToSignUp() {
        // For some reason clicking the signupLink directly with signupLink.click() will not always work, particularly
        // in a sequence of more than two page loads inside a single test. This seems to be a problem with
        // ChromeDriver when the driver is reused (see https://github.com/SeleniumHQ/selenium/issues/4075#issuecomment-456297277).
        // Simulating the click with a javascript script seems to circumvent this issue.
        executor.executeScript("arguments[0].click();", signupLink);
        return new SignUpPage(driver);
    }

    /**
     * Tries to log in a valid user and returns a new HomePage.
     * Requires the username-password combination to be in the database
     * @param username
     * @param password
     * @return a new HomePage
     */
    public HomePage loginValidUser(final String username, final String password) {
        login(username, password);
        return new HomePage(driver);
    }

    /**
     * Tries to log in an invalid user and returns a new LoginPage.
     * Requires the username-password combination to not be in the database.
     * @param username
     * @param password
     * @return
     */
    public LoginPage loginInvalidUser(final String username, final String password) {
        login(username, password);
        return new LoginPage(driver);
    }

    /**
     * Sets the login fields and clicks on the submit button
     * @param username
     * @param password
     */
    public void login(final String username, final String password) {
        setUsername(username);
        setPassword(password);
        executor.executeScript("arguments[0].click()", submitButton);
    }

    /**
     * Sets the contents of the username field
     * @param username
     */
    public void setUsername(final String username) {
        executor.executeScript("arguments[0].value='" + username + "';", inputUsername);
    }

    /**
     * Sets the contents of the password field
     * @param password
     */
    public void setPassword(final String password) {
        executor.executeScript("arguments[0].value='" + password + "';", inputPassword);
    }

    /**
     * Tries to get the error message and returns it
     * @return an error message
     */
    public String getErrorMessage() {
        return errorMessage.getText();
    }

    /**
     * Tries to get the logout message and returns it
     * @return a logout message
     */
    public String getLogoutMessage() {
        wait.until(ExpectedConditions.elementToBeClickable(logoutMessage));
        return logoutMessage.getText();
    }
}

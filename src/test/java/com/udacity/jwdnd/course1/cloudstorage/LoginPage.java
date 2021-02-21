package com.udacity.jwdnd.course1.cloudstorage;

import org.apache.juli.logging.Log;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

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

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Clicks on the sign up link
     * @return a new SignUpPage
     */
    public SignUpPage goToSignUp() {
        signupLink.click();
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
        submitButton.click();
    }

    /**
     * Sets the contents of the username field
     * @param username
     */
    public void setUsername(final String username) {
        inputUsername.clear();
        inputUsername.sendKeys(username);
    }

    /**
     * Sets the contents of the password field
     * @param password
     */
    public void setPassword(final String password) {
        inputPassword.clear();
        inputPassword.sendKeys(password);
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
        return logoutMessage.getText();
    }
}

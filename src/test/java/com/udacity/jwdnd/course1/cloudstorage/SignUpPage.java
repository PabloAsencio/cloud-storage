package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SignUpPage {
    @FindBy(id = "inputFirstName")
    @CacheLookup
    private WebElement inputFirstName;

    @FindBy(id = "inputLastName")
    @CacheLookup
    private WebElement inputLastName;

    @FindBy(id = "inputUsername")
    @CacheLookup
    private WebElement inputUsername;

    @FindBy(id = "inputPassword")
    @CacheLookup
    private WebElement inputPassword;

    @FindBy(id = "submit-button")
    @CacheLookup
    private WebElement submitButton;

    @FindBy(id = "message-error")
    private WebElement errorMessage;

    @FindBy(id = "login-link")
    @CacheLookup
    private WebElement loginLink;

    @FindBy(id = "login-link--success")
    private WebElement successLoginLink;

    private final WebDriver driver;
    private final WebDriverWait wait;
    // The native methods sendKeys and click do not work properly when switching pages several times
    // due to race conditions. Using JavascriptExecutor to click elements and set the value of
    // the input fields circumvents this issue.
    private final JavascriptExecutor executor;

    public SignUpPage(final WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver,10);
        this.executor = (JavascriptExecutor) driver;
        PageFactory.initElements(driver, this);
    }


    public SignUpPage registerInvalidUser(final String firstName, final String lastName, final String username, final String password) {
        registerNewUser(firstName, lastName, username, password);
        return new SignUpPage(driver);
    }

    public LoginPage registerValidUser(final String firstName, final String lastName, final String username, final String password) {
        registerNewUser(firstName, lastName, username, password);
        return new LoginPage(driver);
    }

    /**
     * Sets the contents of the First Name field
     * @param firstName
     */
    public void setFirstName(final String firstName) {
        executor.executeScript("arguments[0].value='" + firstName + "';", inputFirstName);
    }

    /**
     * Sets the contents of the Last Name field
     * @param lastName
     */
    public void setLastName(final String lastName) {
        executor.executeScript("arguments[0].value='" + lastName + "';", inputLastName);
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
     * Submits the signup form
     */
    public void submitForm() {
        executor.executeScript("arguments[0].click()", submitButton);
    }

    /**
     * Tries to click on the link to the login page
     * @return a new LoginPage
     */
    public LoginPage goToLoginPage() {
        loginLink.click();
        return new LoginPage(driver);
    }


    /**
     * Tries to get the error message and returns it
     * @return an error message
     */
    public String getErrorMessage() {
        return errorMessage.getText();
    }

    private void registerNewUser(final String firstName, final String lastName, final String username, final String password) {
        setFirstName(firstName);
        setLastName(lastName);
        setUsername(username);
        setPassword(password);
        submitForm();
    }
}

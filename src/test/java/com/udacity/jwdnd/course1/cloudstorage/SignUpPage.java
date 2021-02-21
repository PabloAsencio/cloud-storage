package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

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

    @FindBy(id = "message-success")
    private WebElement successMessage;

    @FindBy(id = "message-error")
    private WebElement errorMessage;

    @FindBy(id = "login-link")
    private WebElement loginLink;

    private final WebDriver driver;

    public SignUpPage(final WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Tries to register a new user
     * @param firstName
     * @param lastName
     * @param username
     * @param password
     * @return a new SignUpPage where the results of the registering process are shown
     */
    public SignUpPage registerNewUser(final String firstName, final String lastName, final String username, final String password) {
        setFirstName(firstName);
        setLastName(lastName);
        setUsername(username);
        setPassword(password);
        submitButton.click();
        return  new SignUpPage(driver);
    }

    /**
     * Sets the contents of the First Name field
     * @param firstName
     */
    public void setFirstName(final String firstName) {
        inputFirstName.clear();
        inputFirstName.sendKeys(firstName);
    }

    /**
     * Sets the contents of the Last Name field
     * @param lastName
     */
    public void setLastName(final String lastName) {
        inputLastName.clear();
        inputLastName.sendKeys(lastName);
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
     * Tries to click on the link to the login page
     * @return a new LoginPage
     */
    public LoginPage goToLoginPage() {
        loginLink.click();
        return new LoginPage(driver);
    }

    /**
     * Tries to get the success message and returns it
     * @return a success message
     */
    public String getSuccessMessage() {
        return successMessage.getText();
    }

    /**
     * Tries to get the error message and returns it
     * @return an error message
     */
    public String getErrorMessage() {
        return errorMessage.getText();
    }

}

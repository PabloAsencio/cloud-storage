package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {

    @FindBy(id = "logout-button")
    @CacheLookup
    private WebElement logoutButton;

    private final WebDriver driver;
    private final JavascriptExecutor executor;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.executor = (JavascriptExecutor) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Tries to log out the current user
     * @return a new LoginPage
     */
    public LoginPage logout() {
        executor.executeScript("arguments[0].click()", logoutButton);
        return new LoginPage(driver);
    }
}

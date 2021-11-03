package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ResultPage {

    @FindBy(id = "success-link")
    private WebElement successLink;

    @FindBy(id = "error-link")
    private WebElement errorLink;

    private final WebDriver driver;
    private final JavascriptExecutor executor;

    public ResultPage(WebDriver driver) {
        this.driver = driver;
        this.executor = (JavascriptExecutor) driver;
        PageFactory.initElements(driver, this);
    }

    public boolean isSuccess() {
        return !driver.findElements(By.id("success-link")).isEmpty();
    }

    public HomePage goToHomePage() {
        WebElement link = (isSuccess()) ? successLink : errorLink;
        executor.executeScript("arguments[0].click()", link);
        return new HomePage(driver);
    }
}

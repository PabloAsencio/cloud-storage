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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePage {

    @FindBy(id = "logout-button")
    @CacheLookup
    private WebElement logoutButton;

    @FindBy(id = "nav-notes-tab")
    @CacheLookup
    private WebElement notesTab;

    @FindBy(id = "create-note-button")
    private WebElement createNoteButton;

    @FindBy(id = "note-title")
    private WebElement inputNoteTitle;

    @FindBy(id = "note-description")
    private WebElement inputNoteDescription;

    @FindBy(id = "note-submit-button")
    private WebElement noteSubmitButton;

    @FindBy(className = "note")
    private List<WebElement> rawNotes;


    private final WebDriver driver;
    private final JavascriptExecutor executor;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.executor = (JavascriptExecutor) driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Tries to log out the current user
     *
     * @return a new LoginPage
     */
    public LoginPage logout() {
        executor.executeScript("arguments[0].click()", logoutButton);
        return new LoginPage(driver);
    }

    public void openNotesTab() {
        executor.executeScript("arguments[0].click()", notesTab);
        WebDriverWait wait = new WebDriverWait(driver, 1);
        wait.until(ExpectedConditions.elementToBeClickable(createNoteButton));
    }

    public ResultPage createNote(String noteTitle, String noteDescription) {
        executor.executeScript("arguments[0].click();", createNoteButton);
        changeModalContentAndSubmit(noteTitle, noteDescription);
        return new ResultPage(driver);
    }

    public ResultPage editNote(String oldTitle, String newTitle, String newDescription) {
        WebElement editButton = getEditButton(oldTitle);
        executor.executeScript("arguments[0].click()", editButton);
        changeModalContentAndSubmit(newTitle, newDescription);
        return new ResultPage(driver);
    }

    public ResultPage deleteNote(String noteTitle) {
        WebElement deleteButton = getDeleteButton(noteTitle);
        executor.executeScript("arguments[0].click()", deleteButton);
        return new ResultPage(driver);
    }

    public boolean noteExists(String noteTitle, String noteDescription) {
        Map<String, String> notes = getNotes();
        return notes.containsKey(noteTitle) && notes.get(noteTitle).equals(noteDescription);
    }

    private void changeModalContentAndSubmit(String title, String description) {
        executor.executeScript("arguments[0].value ='" + title + "';", inputNoteTitle);
        executor.executeScript("arguments[0].value = '" + description + "';", inputNoteDescription);
        executor.executeScript("arguments[0].click();", noteSubmitButton);
    }

    private Map<String, String> getNotes() {
        /*WebDriverWait wait = new WebDriverWait(driver, 1);
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.className("note"), 0));*/
        Map<String, String> notes = new HashMap<>();
        for (WebElement note : rawNotes) {
            String title = note.findElement(By.className("note__title")).getText();
            String description = note.findElement(By.className("note__description")).getText();
            notes.put(title, description);
        }
        return notes;
    }

    private WebElement getEditButton(String noteTitle) {
        return driver.findElement(By.cssSelector("button[data-note-title = \"" + noteTitle+ "\"]"));
    }

    private WebElement getDeleteButton(String noteTitle) {
        return driver.findElement(By.cssSelector("button[data-note-title = \"" + noteTitle + "\"]+form button"));
    }


}

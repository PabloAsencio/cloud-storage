package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
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

    @FindBy(id = "nav-credentials-tab")
    @CacheLookup
    private WebElement credentialsTab;

    @FindBy(id = "create-credential-button")
    private WebElement createCredentialButton;

    @FindBy(id = "credential-url")
    private WebElement inputCredentialUrl;

    @FindBy(id = "credential-username")
    private WebElement inputCredentialUsername;

    @FindBy(id = "credential-password")
    private WebElement inputCredentialPassword;

    @FindBy(id = "credentialSubmit")
    private WebElement submitCredentialButton;

    @FindBy(className = "credential")
    private List<WebElement> rawCredentials;


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
        changeNoteModalContentAndSubmit(noteTitle, noteDescription);
        return new ResultPage(driver);
    }

    public ResultPage editNote(String oldTitle, String newTitle, String newDescription) {
        WebElement editButton = getEditNoteButton(oldTitle);
        executor.executeScript("arguments[0].click()", editButton);
        changeNoteModalContentAndSubmit(newTitle, newDescription);
        return new ResultPage(driver);
    }

    public ResultPage deleteNote(String noteTitle) {
        WebElement deleteButton = getDeleteNoteButton(noteTitle);
        executor.executeScript("arguments[0].click()", deleteButton);
        return new ResultPage(driver);
    }

    public boolean noteExists(String noteTitle, String noteDescription) {
        Map<String, String> notes = getNotes();
        return notes.containsKey(noteTitle) && notes.get(noteTitle).equals(noteDescription);
    }

    public void openCredentialsTab() {
        executor.executeScript("arguments[0].click()", credentialsTab);
        WebDriverWait wait = new WebDriverWait(driver, 1);
        wait.until(ExpectedConditions.elementToBeClickable(createCredentialButton));
    }

    public ResultPage createCredential(Credential credential) {
        executor.executeScript("arguments[0].click()", createCredentialButton);
        changeCredentialModalAndSubmit(credential);
        return new ResultPage(driver);
    }

    public ResultPage deleteCredential(Credential credential) {
        WebElement deleteButton = getDeleteCredentialButton(credential);
        executor.executeScript("arguments[0].click()", deleteButton);
        return new ResultPage(driver);
    }

    public String getPlainTextPassword(Credential credential) {
        WebElement editButton = getEditCredentialButton(credential);
        executor.executeScript("arguments[0].click()", editButton);
        WebDriverWait wait = new WebDriverWait(driver, 1);
        wait.until(ExpectedConditions.elementToBeClickable(inputCredentialPassword));
        return inputCredentialPassword.getAttribute("value");
    }

    public ResultPage updateCredential(Credential oldCredential, Credential newCredential) {
        WebElement editButton = getEditCredentialButton(oldCredential);
        executor.executeScript("arguments[0].click()", editButton);
        changeCredentialModalAndSubmit(newCredential);
        return new ResultPage(driver);
    }

    public boolean credentialExists(Credential credential) {
        List<Credential> credentials = getCredentials();
        for (Credential otherCredential : credentials) {
            if (otherCredential.getUrl().equals(credential.getUrl())
                    && otherCredential.getUsername().equals(credential.getUsername())) {
                return true;
            }
        }
        return false;
    }

    private void changeNoteModalContentAndSubmit(String title, String description) {
        executor.executeScript("arguments[0].value ='" + title + "';", inputNoteTitle);
        executor.executeScript("arguments[0].value = '" + description + "';", inputNoteDescription);
        executor.executeScript("arguments[0].click();", noteSubmitButton);
    }

    private Map<String, String> getNotes() {
        Map<String, String> notes = new HashMap<>();
        for (WebElement note : rawNotes) {
            String title = note.findElement(By.className("note__title")).getText();
            String description = note.findElement(By.className("note__description")).getText();
            notes.put(title, description);
        }
        return notes;
    }

    private void changeCredentialModalAndSubmit(Credential credential) {
        executor.executeScript("arguments[0].value = '" + credential.getUrl() + "';", inputCredentialUrl);
        executor.executeScript("arguments[0].value = '" + credential.getUsername() + "';", inputCredentialUsername);
        executor.executeScript("arguments[0].value = '" + credential.getPassword() + "';", inputCredentialPassword);
        executor.executeScript("arguments[0].click()", submitCredentialButton);
    }

    private List<Credential> getCredentials() {
        List<Credential> credentials = new ArrayList<>();

        for (WebElement rawCredential : rawCredentials) {
            Credential credential = new Credential();
            credential.setUrl(rawCredential.findElement(By.className("credential__url")).getText());
            credential.setUsername(rawCredential.findElement(By.className("credential__username")).getText());
            credential.setPassword(rawCredential.findElement(By.className("credential__password")).getText());
            credentials.add(credential);
        }

        return credentials;
    }

    private WebElement getEditNoteButton(String noteTitle) {
        return driver.findElement(By.cssSelector("button[data-note-title = \"" + noteTitle+ "\"]"));
    }

    private WebElement getDeleteNoteButton(String noteTitle) {
        return driver.findElement(By.cssSelector("button[data-note-title = \"" + noteTitle + "\"]+form button"));
    }

    private WebElement getEditCredentialButton(Credential credential) {
        return driver.findElement(By.cssSelector("button[data-credential-url = \"" + credential.getUrl() + "\"][data-credential-username = \"" + credential.getUsername() + "\"]"));
    }

    private WebElement getDeleteCredentialButton(Credential credential) {
        return driver.findElement(By.cssSelector("button[data-credential-url = \"" + credential.getUrl() + "\"][data-credential-username = \"" + credential.getUsername() + "\"]+form button"));
    }


}

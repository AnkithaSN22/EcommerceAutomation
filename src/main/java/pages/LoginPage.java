package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object Model for SauceDemo Login Page.
 */
public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("[data-test='error']");
    private final By loginLogo = By.className("login_logo");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public LoginPage enterUsername(String username) {
        WebElement userElem = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
        userElem.clear();
        if (username != null && !username.isEmpty()) {
            userElem.sendKeys(username);
        }
        return this;
    }

    public LoginPage enterPassword(String password) {
        WebElement passElem = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
        passElem.clear();
        if (password != null && !password.isEmpty()) {
            passElem.sendKeys(password);
        }
        return this;
    }

    public void clickLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
    }

    /**
     * Helper method to perform login in a single step.
     */
    public ProductsPage loginAsValidUser(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
        return new ProductsPage(driver);
    }

    /**
     * Helper method to attempt login with invalid credentials.
     */
    public LoginPage loginExpectingFailure(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
        return this;
    }

    public boolean isLoginPageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(loginButton)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessage() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isErrorMessageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getLoginLogoText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(loginLogo)).getText();
    }
}

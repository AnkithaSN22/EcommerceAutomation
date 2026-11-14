package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Page Object Model for SauceDemo Checkout flow:
 * Step 1 (Information), Step 2 (Overview), and Completion.
 */
public class CheckoutPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Step 1: Your Information Locators
    private final By pageTitle = By.cssSelector(".title");
    private final By firstNameInput = By.id("first-name");
    private final By lastNameInput = By.id("last-name");
    private final By postalCodeInput = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By cancelStepOneButton = By.id("cancel");
    private final By errorMessage = By.cssSelector("[data-test='error']");

    // Step 2: Overview Locators
    private final By cartItems = By.className("cart_item");
    private final By itemNames = By.className("inventory_item_name");
    private final By subtotalLabel = By.className("summary_subtotal_label");
    private final By taxLabel = By.className("summary_tax_label");
    private final By totalLabel = By.className("summary_total_label");
    private final By finishButton = By.id("finish");

    // Step 3: Complete Locators
    private final By completeHeader = By.className("complete-header");
    private final By completeText = By.className("complete-text");
    private final By backHomeButton = By.id("back-to-products");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Step 1 Methods
    public boolean isStepOneDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle)).isDisplayed()
                    && getTitle().equalsIgnoreCase("Checkout: Your Information");
        } catch (Exception e) {
            return false;
        }
    }

    public String getTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle)).getText().trim();
    }

    public CheckoutPage enterFirstName(String firstName) {
        WebElement elem = wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameInput));
        elem.clear();
        if (firstName != null && !firstName.isEmpty()) {
            elem.sendKeys(firstName);
        }
        return this;
    }

    public CheckoutPage enterLastName(String lastName) {
        WebElement elem = wait.until(ExpectedConditions.visibilityOfElementLocated(lastNameInput));
        elem.clear();
        if (lastName != null && !lastName.isEmpty()) {
            elem.sendKeys(lastName);
        }
        return this;
    }

    public CheckoutPage enterPostalCode(String postalCode) {
        WebElement elem = wait.until(ExpectedConditions.visibilityOfElementLocated(postalCodeInput));
        elem.clear();
        if (postalCode != null && !postalCode.isEmpty()) {
            elem.sendKeys(postalCode);
        }
        return this;
    }

    public CheckoutPage fillInformation(String firstName, String lastName, String postalCode) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostalCode(postalCode);
        return this;
    }

    public CheckoutPage clickContinue() {
        wait.until(ExpectedConditions.elementToBeClickable(continueButton)).click();
        return this;
    }

    public CartPage clickCancelStepOne() {
        wait.until(ExpectedConditions.elementToBeClickable(cancelStepOneButton)).click();
        return new CartPage(driver);
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

    // Step 2 Methods
    public boolean isOverviewPageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle)).isDisplayed()
                    && getTitle().equalsIgnoreCase("Checkout: Overview");
        } catch (Exception e) {
            return false;
        }
    }

    public int getOverviewItemCount() {
        return driver.findElements(cartItems).size();
    }

    public List<String> getOverviewItemNames() {
        List<WebElement> elements = driver.findElements(itemNames);
        List<String> names = new ArrayList<>();
        for (WebElement elem : elements) {
            names.add(elem.getText().trim());
        }
        return names;
    }

    public double getItemTotal() {
        String text = wait.until(ExpectedConditions.visibilityOfElementLocated(subtotalLabel)).getText();
        // Format: "Item total: $29.99"
        String price = text.replaceAll("[^0-9.]", "").trim();
        return Double.parseDouble(price);
    }

    public double getTax() {
        String text = wait.until(ExpectedConditions.visibilityOfElementLocated(taxLabel)).getText();
        // Format: "Tax: $2.40"
        String tax = text.replaceAll("[^0-9.]", "").trim();
        return Double.parseDouble(tax);
    }

    public double getTotal() {
        String text = wait.until(ExpectedConditions.visibilityOfElementLocated(totalLabel)).getText();
        // Format: "Total: $32.39"
        String total = text.replaceAll("[^0-9.]", "").trim();
        return Double.parseDouble(total);
    }

    public CheckoutPage clickFinish() {
        wait.until(ExpectedConditions.elementToBeClickable(finishButton)).click();
        return this;
    }

    // Step 3 (Complete) Methods
    public boolean isOrderCompleteDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle)).isDisplayed()
                    && getTitle().equalsIgnoreCase("Checkout: Complete!");
        } catch (Exception e) {
            return false;
        }
    }

    public String getConfirmationHeader() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(completeHeader)).getText().trim();
    }

    public String getConfirmationText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(completeText)).getText().trim();
    }

    public ProductsPage clickBackHome() {
        wait.until(ExpectedConditions.elementToBeClickable(backHomeButton)).click();
        return new ProductsPage(driver);
    }
}

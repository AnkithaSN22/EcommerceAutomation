package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Page Object Model for SauceDemo Shopping Cart Page.
 */
public class CartPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By pageTitle = By.cssSelector(".title");
    private final By cartItems = By.className("cart_item");
    private final By itemNames = By.className("inventory_item_name");
    private final By continueShoppingButton = By.id("continue-shopping");
    private final By checkoutButton = By.id("checkout");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isCartPageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle)).isDisplayed()
                    && getTitle().equalsIgnoreCase("Your Cart");
        } catch (Exception e) {
            return false;
        }
    }

    public String getTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle)).getText().trim();
    }

    public int getCartItemCount() {
        return driver.findElements(cartItems).size();
    }

    public List<String> getCartItemNames() {
        List<String> names = new ArrayList<>();
        List<WebElement> elements = driver.findElements(itemNames);
        for (WebElement elem : elements) {
            try {
                names.add(elem.getText().trim());
            } catch (StaleElementReferenceException ignored) {
                // If element became stale during re-render
            }
        }
        return names;
    }

    public boolean isProductInCart(String productName) {
        String xpath = "//div[@class='cart_item'][.//div[contains(@class,'inventory_item_name') and text()='"
                + productName + "']]";
        return !driver.findElements(By.xpath(xpath)).isEmpty();
    }

    public double getProductPrice(String productName) {
        String xpath = "//div[@class='cart_item'][.//div[contains(@class,'inventory_item_name') and text()='"
                + productName + "']]//div[contains(@class,'inventory_item_price')]";
        WebElement priceElem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        String text = priceElem.getText().replace("$", "").trim();
        return Double.parseDouble(text);
    }

    public CartPage removeProduct(String productName) {
        String itemXPath = "//div[@class='cart_item'][.//div[contains(@class,'inventory_item_name') and text()='"
                + productName + "']]";
        String buttonXPath = itemXPath + "//button";

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(buttonXPath))).click();
        // Wait until the item is detached/removed from the DOM
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(itemXPath)));
        return this;
    }

    public ProductsPage clickContinueShopping() {
        wait.until(ExpectedConditions.elementToBeClickable(continueShoppingButton)).click();
        return new ProductsPage(driver);
    }

    public CheckoutPage clickCheckout() {
        wait.until(ExpectedConditions.elementToBeClickable(checkoutButton)).click();
        return new CheckoutPage(driver);
    }
}

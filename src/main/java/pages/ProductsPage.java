package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Page Object Model for SauceDemo Inventory / Products Page.
 */
public class ProductsPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By pageTitle = By.cssSelector(".title");
    private final By inventoryItems = By.className("inventory_item");
    private final By itemNames = By.className("inventory_item_name");
    private final By itemPrices = By.className("inventory_item_price");
    private final By sortDropdown = By.cssSelector("[data-test='product-sort-container']");
    private final By cartIcon = By.className("shopping_cart_link");
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By menuButton = By.id("react-burger-menu-btn");
    private final By logoutLink = By.id("logout_sidebar_link");

    public ProductsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isProductsPageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle)).isDisplayed()
                    && getTitle().equalsIgnoreCase("Products");
        } catch (Exception e) {
            return false;
        }
    }

    public String getTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle)).getText().trim();
    }

    public int getProductCount() {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(inventoryItems)).size();
    }

    public List<String> getAllProductNames() {
        List<WebElement> elements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(itemNames));
        List<String> names = new ArrayList<>();
        for (WebElement elem : elements) {
            names.add(elem.getText().trim());
        }
        return names;
    }

    public List<Double> getAllProductPrices() {
        List<WebElement> elements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(itemPrices));
        List<Double> prices = new ArrayList<>();
        for (WebElement elem : elements) {
            String text = elem.getText().replace("$", "").trim();
            prices.add(Double.parseDouble(text));
        }
        return prices;
    }

    public double getProductPrice(String productName) {
        String xpath = "//div[@class='inventory_item'][.//div[contains(@class,'inventory_item_name') and text()='" 
                + productName + "']]//div[contains(@class,'inventory_item_price')]";
        WebElement priceElem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        String text = priceElem.getText().replace("$", "").trim();
        return Double.parseDouble(text);
    }

    public ProductsPage addProductToCart(String productName) {
        String xpath = "//div[@class='inventory_item'][.//div[contains(@class,'inventory_item_name') and text()='"
                + productName + "']]//button";
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        if (button.getText().equalsIgnoreCase("Add to cart")) {
            button.click();
            // Wait for React UI state to update to Remove
            wait.until(ExpectedConditions.textToBe(By.xpath(xpath), "Remove"));
        }
        return this;
    }

    public ProductsPage removeProductFromCart(String productName) {
        String xpath = "//div[@class='inventory_item'][.//div[contains(@class,'inventory_item_name') and text()='"
                + productName + "']]//button";
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        if (button.getText().equalsIgnoreCase("Remove")) {
            button.click();
            // Wait for React UI state to update to Add to cart
            wait.until(ExpectedConditions.textToBe(By.xpath(xpath), "Add to cart"));
        }
        return this;
    }

    public boolean isProductAdded(String productName) {
        String xpath = "//div[@class='inventory_item'][.//div[contains(@class,'inventory_item_name') and text()='"
                + productName + "']]//button";
        try {
            return wait.until(ExpectedConditions.textToBe(By.xpath(xpath), "Remove"));
        } catch (Exception e) {
            return false;
        }
    }

    public void selectSortOption(String optionTextOrValue) {
        WebElement dropdownElem = wait.until(ExpectedConditions.visibilityOfElementLocated(sortDropdown));
        Select select = new Select(dropdownElem);
        try {
            select.selectByVisibleText(optionTextOrValue);
        } catch (Exception e) {
            select.selectByValue(optionTextOrValue);
        }
    }

    public int getCartBadgeCount() {
        try {
            List<WebElement> badges = driver.findElements(cartBadge);
            if (badges.isEmpty() || !badges.get(0).isDisplayed()) {
                return 0;
            }
            return Integer.parseInt(badges.get(0).getText().trim());
        } catch (Exception e) {
            return 0;
        }
    }

    public CartPage clickCartIcon() {
        wait.until(ExpectedConditions.elementToBeClickable(cartIcon)).click();
        return new CartPage(driver);
    }

    public LoginPage logout() {
        wait.until(ExpectedConditions.elementToBeClickable(menuButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();
        return new LoginPage(driver);
    }
}

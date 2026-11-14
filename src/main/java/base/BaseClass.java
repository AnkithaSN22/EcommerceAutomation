package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import utilities.ConfigReader;

import java.time.Duration;

/**
 * BaseClass initializes WebDriver, configures browser options,
 * manages driver lifecycle using ThreadLocal for parallel execution,
 * and exposes reusable browser interaction helpers.
 */
public class BaseClass {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    private static void setDriver(WebDriver driver) {
        driverThreadLocal.set(driver);
    }

    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser"})
    public void setUp(@Optional("") String browserParam) {
        String browser = (browserParam != null && !browserParam.trim().isEmpty())
                ? browserParam
                : ConfigReader.getBrowser();

        boolean headless = ConfigReader.isHeadless();
        WebDriver driver;

        switch (browser.toLowerCase().trim()) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) {
                    firefoxOptions.addArguments("-headless");
                }
                driver = new FirefoxDriver(firefoxOptions);
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) {
                    edgeOptions.addArguments("--headless=new");
                }
                edgeOptions.addArguments("--remote-allow-origins=*");
                edgeOptions.addArguments("--disable-gpu");
                edgeOptions.addArguments("--no-sandbox");
                driver = new EdgeDriver(edgeOptions);
                break;

            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                if (headless) {
                    chromeOptions.addArguments("--headless=new");
                }
                chromeOptions.addArguments("--remote-allow-origins=*");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--window-size=1920,1080");
                driver = new ChromeDriver(chromeOptions);
                break;
        }

        setDriver(driver);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getImplicitWait()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigReader.getPageLoadTimeout()));
        driver.manage().window().maximize();

        driver.get(ConfigReader.getUrl());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        WebDriver driver = getDriver();
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                System.err.println("Error quitting driver: " + e.getMessage());
            } finally {
                driverThreadLocal.remove();
            }
        }
    }

    // ==========================================
    // Common Helper Methods with Explicit Waits
    // ==========================================

    public WebDriverWait getWait() {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(ConfigReader.getExplicitWait()));
    }

    public WebDriverWait getWait(int timeoutInSeconds) {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(timeoutInSeconds));
    }

    public WebElement waitForVisibility(By locator) {
        return getWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForVisibility(By locator, int timeoutInSeconds) {
        return getWait(timeoutInSeconds).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForClickable(By locator) {
        return getWait().until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void click(By locator) {
        waitForClickable(locator).click();
    }

    public void sendKeys(By locator, String text) {
        WebElement element = waitForVisibility(locator);
        element.clear();
        if (text != null) {
            element.sendKeys(text);
        }
    }

    public String getText(By locator) {
        return waitForVisibility(locator).getText().trim();
    }

    public boolean isDisplayed(By locator) {
        try {
            return waitForVisibility(locator, 3).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void scrollToElement(By locator) {
        WebElement element = getDriver().findElement(locator);
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public String getCurrentUrl() {
        return getDriver().getCurrentUrl();
    }

    public String getPageTitle() {
        return getDriver().getTitle();
    }
}

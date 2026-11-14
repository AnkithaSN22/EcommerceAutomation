package utilities;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class to capture screenshots during test execution.
 */
public class ScreenshotUtil {

    private static final String SCREENSHOT_DIR = "reports/screenshots/";

    /**
     * Capture screenshot and save to disk.
     *
     * @param driver         the WebDriver instance
     * @param screenshotName the base name for the screenshot file
     * @return relative path to the saved screenshot file
     */
    public static String captureScreenshot(WebDriver driver, String screenshotName) {
        if (driver == null) {
            return null;
        }

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
        String fileName = screenshotName.replaceAll("[^a-zA-Z0-9_-]", "_") + "_" + timestamp + ".png";
        String destinationPath = SCREENSHOT_DIR + fileName;

        File destinationFile = new File(destinationPath);
        File parentDir = destinationFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File sourceFile = ts.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(sourceFile, destinationFile);
            return destinationPath;
        } catch (IOException e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }

    /**
     * Capture screenshot as Base64 string for direct embedding in ExtentReports.
     *
     * @param driver the WebDriver instance
     * @return base64 string representation of the screenshot
     */
    public static String captureBase64(WebDriver driver) {
        if (driver == null) {
            return "";
        }
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            return ts.getScreenshotAs(OutputType.BASE64);
        } catch (Exception e) {
            System.err.println("Failed to capture Base64 screenshot: " + e.getMessage());
            return "";
        }
    }
}

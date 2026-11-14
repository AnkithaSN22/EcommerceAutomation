package utilities;

import base.BaseClass;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG listener to automatically log test results, capture screenshots on failure,
 * and populate ExtentReports.
 */
public class TestListener implements ITestListener {

    private static final ExtentReports extent = ExtentReportManager.getInstance();
    private static final ThreadLocal<ExtentTest> testNode = new ThreadLocal<>();

    public static ExtentTest getTest() {
        return testNode.get();
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("========== Test Suite Started: " + context.getName() + " ==========");
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("========== Test Suite Finished: " + context.getName() + " ==========");
        ExtentReportManager.flushReports();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        ExtentTest test = extent.createTest(testName, description);

        // Assign category based on class name
        String className = result.getTestClass().getRealClass().getSimpleName();
        test.assignCategory(className);

        testNode.set(test);
        testNode.get().log(Status.INFO, "Starting test: " + testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        testNode.get().log(Status.PASS, "Test PASSED: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        testNode.get().log(Status.FAIL, "Test FAILED: " + result.getMethod().getMethodName());
        testNode.get().log(Status.FAIL, result.getThrowable());

        WebDriver driver = BaseClass.getDriver();
        if (driver != null) {
            try {
                String base64Screenshot = ScreenshotUtil.captureBase64(driver);
                if (base64Screenshot != null && !base64Screenshot.isEmpty()) {
                    testNode.get().fail("Failure Screenshot:",
                            MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
                }
                // Also save to disk
                ScreenshotUtil.captureScreenshot(driver, result.getMethod().getMethodName() + "_failure");
            } catch (Exception e) {
                testNode.get().log(Status.WARNING, "Failed to capture screenshot: " + e.getMessage());
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        testNode.get().log(Status.SKIP, "Test SKIPPED: " + result.getMethod().getMethodName());
        if (result.getThrowable() != null) {
            testNode.get().log(Status.SKIP, result.getThrowable());
        }
    }
}

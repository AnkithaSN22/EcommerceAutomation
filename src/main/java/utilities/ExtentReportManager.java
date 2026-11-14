package utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;

/**
 * Manages the ExtentReports instance lifecycle.
 */
public class ExtentReportManager {

    private static ExtentReports extent;
    private static final String REPORT_DIR = "reports/";
    private static final String REPORT_FILE = REPORT_DIR + "ExtentReport.html";

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            createInstance();
        }
        return extent;
    }

    private static void createInstance() {
        File dir = new File(REPORT_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(REPORT_FILE);
        sparkReporter.config().setDocumentTitle("SauceDemo Automation Test Report");
        sparkReporter.config().setReportName("End-to-End E-commerce Test Execution Summary");
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setEncoding("UTF-8");
        sparkReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        // System information
        extent.setSystemInfo("Application", "SauceDemo");
        extent.setSystemInfo("Environment", "QA / Staging");
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("User", System.getProperty("user.name"));
        extent.setSystemInfo("Framework", "Selenium WebDriver 4 + TestNG + POM");
    }

    public static synchronized void flushReports() {
        if (extent != null) {
            extent.flush();
        }
    }
}

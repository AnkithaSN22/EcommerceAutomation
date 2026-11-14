package utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigReader loads key-value pairs from config.properties file
 * and allows system property overrides.
 */
public class ConfigReader {

    private static Properties properties;
    private static final String CONFIG_FILE_PATH = "src/test/resources/config.properties";

    static {
        loadProperties();
    }

    private static void loadProperties() {
        properties = new Properties();
        try (InputStream input = new FileInputStream(CONFIG_FILE_PATH)) {
            properties.load(input);
        } catch (IOException e) {
            // Fallback: try loading from classpath
            try (InputStream cpInput = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
                if (cpInput != null) {
                    properties.load(cpInput);
                } else {
                    System.err.println("Could not find config.properties at " + CONFIG_FILE_PATH + " or on classpath.");
                }
            } catch (IOException ex) {
                System.err.println("Failed to load config.properties: " + ex.getMessage());
            }
        }
    }

    /**
     * Retrieve property with system property priority.
     */
    public static String getProperty(String key) {
        String systemProp = System.getProperty(key);
        if (systemProp != null && !systemProp.trim().isEmpty()) {
            return systemProp.trim();
        }
        if (properties == null) {
            loadProperties();
        }
        return properties.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        String val = getProperty(key);
        return (val != null) ? val : defaultValue;
    }

    public static String getUrl() {
        return getProperty("url", "https://www.saucedemo.com/");
    }

    public static String getBrowser() {
        return getProperty("browser", "chrome");
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(getProperty("headless", "false"));
    }

    public static int getImplicitWait() {
        return Integer.parseInt(getProperty("implicitWait", "10"));
    }

    public static int getExplicitWait() {
        return Integer.parseInt(getProperty("explicitWait", "10"));
    }

    public static int getPageLoadTimeout() {
        return Integer.parseInt(getProperty("pageLoadTimeout", "20"));
    }

    public static String getStandardUser() {
        return getProperty("standardUser", "standard_user");
    }

    public static String getPassword() {
        return getProperty("password", "secret_sauce");
    }

    public static String getLockedUser() {
        return getProperty("lockedUser", "locked_out_user");
    }

    public static String getProblemUser() {
        return getProperty("problemUser", "problem_user");
    }

    public static String getInvalidUsername() {
        return getProperty("invalidUsername", "invalid_user");
    }

    public static String getInvalidPassword() {
        return getProperty("invalidPassword", "wrong_password");
    }

    public static String getDefaultFirstName() {
        return getProperty("defaultFirstName", "John");
    }

    public static String getDefaultLastName() {
        return getProperty("defaultLastName", "Doe");
    }

    public static String getDefaultPostalCode() {
        return getProperty("defaultPostalCode", "10001");
    }
}

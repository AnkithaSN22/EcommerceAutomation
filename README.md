# EcommerceAutomation
Automated UI test framework for SauceDemo using Java, Selenium, TestNG and Maven, built on Page Object Model with ExtentReports and failure screenshots.
# E-Commerce Test Automation Framework

Automated UI test suite for [SauceDemo (Swag Labs)](https://www.saucedemo.com) built with the Page Object Model (POM) design pattern.

## Tech Stack

- Java 17+
- Selenium WebDriver 4.29
- TestNG 7.10
- Apache Maven 3.9
- ExtentReports 5.1
- WebDriverManager

## Test Results

23 automated scenarios, 23 passed, 0 failed, 0 skipped (100% pass rate).

## Project Structure

```
EcommerceAutomation/
├── pom.xml                     # Dependencies & Maven build configuration
├── testng.xml                  # TestNG suite & listener configuration
├── reports/
│   ├── ExtentReport.html       # Interactive HTML execution report
│   └── screenshots/            # Failure screenshots
└── src/
    ├── main/java/
    │   ├── base/               # BaseClass: driver lifecycle (ThreadLocal), waits
    │   ├── pages/              # LoginPage, ProductsPage, CartPage, CheckoutPage
    │   └── utilities/          # ConfigReader, ScreenshotUtil, ExtentReportManager, TestListener
    └── test/
        ├── java/tests/         # LoginTest, ProductTest, CartTest, CheckoutTest, EndToEndTest
        └── resources/
            └── config.properties
```

## Test Coverage

| Test Class | Coverage |
|------------|----------|
| LoginTest | Valid/invalid login, locked-out user, empty fields, logout |
| ProductTest | Add/remove items, price verification, sorting (name and price) |
| CartTest | Cart contents, item removal, continue shopping, proceed to checkout |
| CheckoutTest | Form validation, order summary and tax math, order confirmation |
| EndToEndTest | Full journey: login → sort → add items → cart → checkout → finish → logout |

## Prerequisites

- Java 17 or higher
- Maven 3.9 or higher
- Chrome (default), Edge, or Firefox

## How to Run

Run all commands from the project root folder.

**Run the full suite:**
```bash
mvn clean test
```

**Run a specific test class:**
```bash
mvn test -Dtest=LoginTest
mvn test -Dtest=ProductTest
mvn test -Dtest=CartTest
mvn test -Dtest=CheckoutTest
mvn test -Dtest=EndToEndTest
```

**Run in headed mode (visible browser):**
```bash
mvn test -Dheadless=false
```

**Run on a different browser:**
```bash
mvn test -Dbrowser=edge
mvn test -Dbrowser=firefox
```

**Run from an IDE:** right-click `testng.xml` and choose *Run*, or click the green play button next to any test class or method.

## Reports

- **Extent Report:** open `reports/ExtentReport.html` in a browser for the dashboard, timeline, and test logs. Failed tests include embedded screenshots.
- **TestNG Report:** open `target/surefire-reports/index.html`.


## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.9 or higher
- Google Chrome (default), Microsoft Edge, or Firefox
- Git

Verify your setup:

```bash
java -version
mvn -version
```

### Setup

1. Clone the repository:
   ```bash
   git clone <your-repo-url>
   ```
2. Go to the project root folder (the one containing `pom.xml`):
   ```bash
   cd EcommerceAutomation
   ```
3. Download dependencies and run the full suite:
   ```bash
   mvn clean test
   ```

Browser drivers are downloaded automatically by WebDriverManager, so no manual driver setup is needed. Tests run in headless mode by default.

## Running the Tests

**Full suite** (runs everything configured in `testng.xml`):
```bash
mvn clean test
```

**A single test class:**
```bash
mvn test -Dtest=LoginTest
mvn test -Dtest=ProductTest
mvn test -Dtest=CartTest
mvn test -Dtest=CheckoutTest
mvn test -Dtest=EndToEndTest
```

**With a visible browser** (headed mode):
```bash
mvn test -Dheadless=false
```

**On a different browser:**
```bash
mvn test -Dbrowser=edge
mvn test -Dbrowser=firefox
```

**Options can be combined:**
```bash
mvn test -Dtest=EndToEndTest -Dbrowser=firefox -Dheadless=false
```

**From an IDE (IntelliJ IDEA / Eclipse / VS Code):**
1. Open the project as a Maven project.
2. Right-click `testng.xml` and select *Run*, or
3. Open any test class and click the green play button next to the class or a test method.

## Viewing Reports

After the run finishes:

- **Extent Report:** open `reports/ExtentReport.html` in your browser.
- **TestNG Report:** open `target/surefire-reports/index.html`.

Failed tests include a screenshot automatically.

package tests;

import base.BaseClass;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.ProductsPage;
import utilities.ConfigReader;

/**
 * Tests covering User Login scenarios:
 * TC_01: Login with valid credentials
 * TC_02: Login with invalid credentials and edge cases
 */
public class LoginTest extends BaseClass {

    private LoginPage loginPage;

    @BeforeMethod
    public void initPage() {
        loginPage = new LoginPage(getDriver());
    }

    @Test(priority = 1, description = "TC_01: Login with valid credentials - user reaches products page")
    public void testValidLogin() {
        ProductsPage productsPage = loginPage.loginAsValidUser(
                ConfigReader.getStandardUser(),
                ConfigReader.getPassword()
        );

        Assert.assertTrue(productsPage.isProductsPageDisplayed(), "Products page should be displayed after valid login.");
        Assert.assertEquals(productsPage.getTitle(), "Products", "Page header should be 'Products'.");
        Assert.assertTrue(getCurrentUrl().contains("inventory.html"), "URL should contain inventory.html");
    }

    @Test(priority = 2, description = "TC_02: Login with invalid credentials - error message is displayed")
    public void testInvalidLogin() {
        loginPage.loginExpectingFailure(
                ConfigReader.getInvalidUsername(),
                ConfigReader.getInvalidPassword()
        );

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed.");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username and password do not match"),
                "Error message should mention credentials mismatch.");
    }

    @Test(priority = 3, description = "TC_02_A: Login with locked out user")
    public void testLockedOutUserLogin() {
        loginPage.loginExpectingFailure(
                ConfigReader.getLockedUser(),
                ConfigReader.getPassword()
        );

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for locked out user.");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Sorry, this user has been locked out"),
                "Error message should state the user is locked out.");
    }

    @Test(priority = 4, description = "TC_02_B: Login with empty username")
    public void testEmptyUsernameLogin() {
        loginPage.loginExpectingFailure("", ConfigReader.getPassword());

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for empty username.");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username is required"),
                "Error message should require username.");
    }

    @Test(priority = 5, description = "TC_02_C: Login with empty password")
    public void testEmptyPasswordLogin() {
        loginPage.loginExpectingFailure(ConfigReader.getStandardUser(), "");

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for empty password.");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Password is required"),
                "Error message should require password.");
    }

    @Test(priority = 6, description = "TC_02_D: Logout from application returns to login page")
    public void testUserLogout() {
        ProductsPage productsPage = loginPage.loginAsValidUser(
                ConfigReader.getStandardUser(),
                ConfigReader.getPassword()
        );

        LoginPage returnedLoginPage = productsPage.logout();
        Assert.assertTrue(returnedLoginPage.isLoginPageDisplayed(), "User should be redirected to Login page upon logout.");
    }
}

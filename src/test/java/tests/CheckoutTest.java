package tests;

import base.BaseClass;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.CheckoutPage;
import pages.LoginPage;
import pages.ProductsPage;
import utilities.ConfigReader;

/**
 * Tests covering Checkout workflows:
 * TC_06: Checkout with valid details
 * TC_07: Checkout with missing required details (validation errors)
 * TC_08: Verify order summary and pricing calculations
 * TC_09: Complete an order and verify order confirmation
 */
public class CheckoutTest extends BaseClass {

    private CheckoutPage checkoutPage;
    private final String testProduct = "Sauce Labs Backpack";

    @BeforeMethod
    public void setupAndNavigateToCheckout() {
        LoginPage loginPage = new LoginPage(getDriver());
        ProductsPage productsPage = loginPage.loginAsValidUser(
                ConfigReader.getStandardUser(),
                ConfigReader.getPassword()
        );
        productsPage.addProductToCart(testProduct);
        CartPage cartPage = productsPage.clickCartIcon();
        checkoutPage = cartPage.clickCheckout();
    }

    @Test(priority = 1, description = "TC_06: Checkout with valid details - user proceeds to order overview")
    public void testCheckoutWithValidDetails() {
        checkoutPage.fillInformation(
                ConfigReader.getDefaultFirstName(),
                ConfigReader.getDefaultLastName(),
                ConfigReader.getDefaultPostalCode()
        );
        checkoutPage.clickContinue();

        Assert.assertTrue(checkoutPage.isOverviewPageDisplayed(), "User should proceed to Checkout: Overview page.");
        Assert.assertEquals(checkoutPage.getTitle(), "Checkout: Overview");
    }

    @Test(priority = 2, description = "TC_07_A: Checkout with missing First Name displays error")
    public void testCheckoutMissingFirstName() {
        checkoutPage.fillInformation("", "Doe", "12345");
        checkoutPage.clickContinue();

        Assert.assertTrue(checkoutPage.isErrorMessageDisplayed(), "Error should be displayed.");
        Assert.assertTrue(checkoutPage.getErrorMessage().contains("First Name is required"),
                "Validation message should state First Name is required.");
    }

    @Test(priority = 3, description = "TC_07_B: Checkout with missing Last Name displays error")
    public void testCheckoutMissingLastName() {
        checkoutPage.fillInformation("John", "", "12345");
        checkoutPage.clickContinue();

        Assert.assertTrue(checkoutPage.isErrorMessageDisplayed(), "Error should be displayed.");
        Assert.assertTrue(checkoutPage.getErrorMessage().contains("Last Name is required"),
                "Validation message should state Last Name is required.");
    }

    @Test(priority = 4, description = "TC_07_C: Checkout with missing Postal Code displays error")
    public void testCheckoutMissingPostalCode() {
        checkoutPage.fillInformation("John", "Doe", "");
        checkoutPage.clickContinue();

        Assert.assertTrue(checkoutPage.isErrorMessageDisplayed(), "Error should be displayed.");
        Assert.assertTrue(checkoutPage.getErrorMessage().contains("Postal Code is required"),
                "Validation message should state Postal Code is required.");
    }

    @Test(priority = 5, description = "TC_08: Verify order summary - items, item total, tax and final calculation")
    public void testVerifyOrderSummary() {
        checkoutPage.fillInformation(
                ConfigReader.getDefaultFirstName(),
                ConfigReader.getDefaultLastName(),
                ConfigReader.getDefaultPostalCode()
        );
        checkoutPage.clickContinue();

        Assert.assertTrue(checkoutPage.isOverviewPageDisplayed());
        Assert.assertEquals(checkoutPage.getOverviewItemCount(), 1, "There should be 1 item in overview.");
        Assert.assertTrue(checkoutPage.getOverviewItemNames().contains(testProduct), "Overview item name should match.");

        double itemTotal = checkoutPage.getItemTotal();
        double tax = checkoutPage.getTax();
        double total = checkoutPage.getTotal();

        Assert.assertEquals(itemTotal, 29.99, "Item total should match product price.");
        // Verify math: subtotal + tax == total (rounded to 2 decimal places)
        double calculatedTotal = Math.round((itemTotal + tax) * 100.0) / 100.0;
        Assert.assertEquals(total, calculatedTotal, "Total should equal item total plus tax.");
    }

    @Test(priority = 6, description = "TC_09: Complete an order - order confirmation is displayed")
    public void testCompleteOrder() {
        checkoutPage.fillInformation(
                ConfigReader.getDefaultFirstName(),
                ConfigReader.getDefaultLastName(),
                ConfigReader.getDefaultPostalCode()
        );
        checkoutPage.clickContinue();

        checkoutPage.clickFinish();

        Assert.assertTrue(checkoutPage.isOrderCompleteDisplayed(), "Order Complete page should be displayed.");
        Assert.assertTrue(checkoutPage.getConfirmationHeader().toUpperCase().contains("THANK YOU FOR YOUR ORDER"),
                "Confirmation header should thank customer for order.");
        Assert.assertTrue(checkoutPage.getConfirmationText().contains("dispatched"),
                "Confirmation description should mention order dispatch.");

        // Back to products
        ProductsPage returnedHome = checkoutPage.clickBackHome();
        Assert.assertTrue(returnedHome.isProductsPageDisplayed(), "User should be able to return to products page.");
    }
}

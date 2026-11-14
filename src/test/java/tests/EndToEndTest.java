package tests;

import base.BaseClass;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.CheckoutPage;
import pages.LoginPage;
import pages.ProductsPage;
import utilities.ConfigReader;

/**
 * Complete End-to-End E-commerce shopping workflow test:
 * Login -> Catalog Browsing & Sorting -> Multi-item Cart Management -> Checkout -> Summary Validation -> Order Confirmation -> Logout.
 */
public class EndToEndTest extends BaseClass {

    @Test(priority = 1, description = "Comprehensive End-to-End shopping workflow")
    public void testFullEndToEndShoppingWorkflow() {
        // 1. Login
        LoginPage loginPage = new LoginPage(getDriver());
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Login page should be open.");

        ProductsPage productsPage = loginPage.loginAsValidUser(
                ConfigReader.getStandardUser(),
                ConfigReader.getPassword()
        );
        Assert.assertTrue(productsPage.isProductsPageDisplayed(), "Products page should be displayed.");

        // 2. Sort by Price (low to high)
        productsPage.selectSortOption("Price (low to high)");

        // 3. Add items to cart
        String item1 = "Sauce Labs Onesie";
        String item2 = "Sauce Labs Backpack";

        productsPage.addProductToCart(item1);
        productsPage.addProductToCart(item2);
        Assert.assertEquals(productsPage.getCartBadgeCount(), 2, "Cart badge count should reflect 2 added items.");

        // 4. Navigate to Cart
        CartPage cartPage = productsPage.clickCartIcon();
        Assert.assertTrue(cartPage.isCartPageDisplayed(), "Cart page should be displayed.");
        Assert.assertEquals(cartPage.getCartItemCount(), 2, "Cart should contain 2 items.");
        Assert.assertTrue(cartPage.isProductInCart(item1), "Item 1 should be in cart.");
        Assert.assertTrue(cartPage.isProductInCart(item2), "Item 2 should be in cart.");

        // 5. Proceed to Checkout Step 1
        CheckoutPage checkoutPage = cartPage.clickCheckout();
        Assert.assertTrue(checkoutPage.isStepOneDisplayed(), "Checkout Step 1 should be displayed.");

        // 6. Enter customer information and continue
        checkoutPage.fillInformation("Jane", "Smith", "90210");
        checkoutPage.clickContinue();

        // 7. Verify Checkout Step 2 Overview
        Assert.assertTrue(checkoutPage.isOverviewPageDisplayed(), "Checkout Overview should be displayed.");
        Assert.assertEquals(checkoutPage.getOverviewItemCount(), 2, "Overview should list 2 items.");
        Assert.assertTrue(checkoutPage.getOverviewItemNames().contains(item1));
        Assert.assertTrue(checkoutPage.getOverviewItemNames().contains(item2));

        double subtotal = checkoutPage.getItemTotal();
        double tax = checkoutPage.getTax();
        double total = checkoutPage.getTotal();

        double expectedSubtotal = 7.99 + 29.99;
        Assert.assertEquals(subtotal, expectedSubtotal, "Item subtotal should match combined item prices.");
        double expectedTotal = Math.round((subtotal + tax) * 100.0) / 100.0;
        Assert.assertEquals(total, expectedTotal, "Total should equal item total + tax.");

        // 8. Complete Order
        checkoutPage.clickFinish();
        Assert.assertTrue(checkoutPage.isOrderCompleteDisplayed(), "Order Complete confirmation should be displayed.");
        Assert.assertTrue(checkoutPage.getConfirmationHeader().toUpperCase().contains("THANK YOU FOR YOUR ORDER"));

        // 9. Back Home & Logout
        ProductsPage finalProductsPage = checkoutPage.clickBackHome();
        Assert.assertTrue(finalProductsPage.isProductsPageDisplayed());

        LoginPage returnedLoginPage = finalProductsPage.logout();
        Assert.assertTrue(returnedLoginPage.isLoginPageDisplayed(), "User successfully logged out.");
    }
}

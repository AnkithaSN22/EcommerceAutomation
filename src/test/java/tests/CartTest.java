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
 * Tests covering Shopping Cart functionalities:
 * - Adding and verifying items in cart
 * - Removing items from cart
 * - Price and details checks in cart
 * - Cart navigation (continue shopping and proceed to checkout)
 */
public class CartTest extends BaseClass {

    private ProductsPage productsPage;

    @BeforeMethod
    public void login() {
        LoginPage loginPage = new LoginPage(getDriver());
        productsPage = loginPage.loginAsValidUser(
                ConfigReader.getStandardUser(),
                ConfigReader.getPassword()
        );
    }

    @Test(priority = 1, description = "Verify added product appears in the cart with correct price")
    public void testProductAppearsInCart() {
        String product = "Sauce Labs Backpack";
        productsPage.addProductToCart(product);

        CartPage cartPage = productsPage.clickCartIcon();
        Assert.assertTrue(cartPage.isCartPageDisplayed(), "Cart page should be displayed.");
        Assert.assertTrue(cartPage.isProductInCart(product), "Product should be present in cart.");
        Assert.assertEquals(cartPage.getCartItemCount(), 1, "Cart item count should be 1.");
        Assert.assertEquals(cartPage.getProductPrice(product), 29.99, "Product price in cart should match catalog.");
    }

    @Test(priority = 2, description = "Verify removing a product directly from the cart page")
    public void testRemoveProductFromCartPage() {
        String product = "Sauce Labs Bike Light";
        productsPage.addProductToCart(product);

        CartPage cartPage = productsPage.clickCartIcon();
        Assert.assertTrue(cartPage.isProductInCart(product), "Product should initially be in cart.");

        cartPage.removeProduct(product);
        Assert.assertFalse(cartPage.isProductInCart(product), "Product should be removed from cart.");
        Assert.assertEquals(cartPage.getCartItemCount(), 0, "Cart should be empty.");
    }

    @Test(priority = 3, description = "Verify Continue Shopping button returns to Products page")
    public void testContinueShoppingNavigation() {
        CartPage cartPage = productsPage.clickCartIcon();
        Assert.assertTrue(cartPage.isCartPageDisplayed());

        ProductsPage returnedPage = cartPage.clickContinueShopping();
        Assert.assertTrue(returnedPage.isProductsPageDisplayed(), "Should return to products page.");
    }

    @Test(priority = 4, description = "Verify Checkout button proceeds to Checkout Information page")
    public void testProceedToCheckoutNavigation() {
        productsPage.addProductToCart("Sauce Labs Onesie");
        CartPage cartPage = productsPage.clickCartIcon();

        CheckoutPage checkoutPage = cartPage.clickCheckout();
        Assert.assertTrue(checkoutPage.isStepOneDisplayed(), "Should navigate to Checkout: Your Information step.");
    }
}

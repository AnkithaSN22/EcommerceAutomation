package tests;

import base.BaseClass;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.ProductsPage;
import utilities.ConfigReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Tests covering Product Listing, Catalog, Sorting, and Pricing:
 * TC_03: Add a product to the cart
 * TC_04: Remove a product from the cart
 * TC_05: Verify product price
 * Sorting and catalog validations
 */
public class ProductTest extends BaseClass {

    private ProductsPage productsPage;

    @BeforeMethod
    public void loginAndNavigateToProducts() {
        LoginPage loginPage = new LoginPage(getDriver());
        productsPage = loginPage.loginAsValidUser(
                ConfigReader.getStandardUser(),
                ConfigReader.getPassword()
        );
    }

    @Test(priority = 1, description = "Verify all 6 catalog products are displayed")
    public void testProductCatalogCount() {
        int count = productsPage.getProductCount();
        Assert.assertEquals(count, 6, "Expected 6 products to be displayed in catalog.");
    }

    @Test(priority = 2, description = "TC_05: Verify product price matches expected test data")
    public void testVerifyProductPrice() {
        double backpackPrice = productsPage.getProductPrice("Sauce Labs Backpack");
        Assert.assertEquals(backpackPrice, 29.99, "Backpack price should be $29.99");

        double bikeLightPrice = productsPage.getProductPrice("Sauce Labs Bike Light");
        Assert.assertEquals(bikeLightPrice, 9.99, "Bike Light price should be $9.99");

        double onesiePrice = productsPage.getProductPrice("Sauce Labs Onesie");
        Assert.assertEquals(onesiePrice, 7.99, "Onesie price should be $7.99");
    }

    @Test(priority = 3, description = "TC_03: Add a product to the cart - badge increments and button changes")
    public void testAddProductToCart() {
        String product = "Sauce Labs Backpack";
        Assert.assertEquals(productsPage.getCartBadgeCount(), 0, "Cart should initially be empty.");

        productsPage.addProductToCart(product);
        Assert.assertTrue(productsPage.isProductAdded(product), "Button should change to Remove after adding.");
        Assert.assertEquals(productsPage.getCartBadgeCount(), 1, "Cart badge count should be 1.");
    }

    @Test(priority = 4, description = "TC_04: Remove a product from the cart - badge decrements")
    public void testRemoveProductFromCart() {
        String product = "Sauce Labs Backpack";
        productsPage.addProductToCart(product);
        Assert.assertEquals(productsPage.getCartBadgeCount(), 1);

        productsPage.removeProductFromCart(product);
        Assert.assertFalse(productsPage.isProductAdded(product), "Button should revert to Add to cart.");
        Assert.assertEquals(productsPage.getCartBadgeCount(), 0, "Cart badge should be cleared.");
    }

    @Test(priority = 5, description = "Verify sorting products by Name (A to Z) and (Z to A)")
    public void testProductSortingByName() {
        // Sort Z to A
        productsPage.selectSortOption("Name (Z to A)");
        List<String> actualNamesDesc = productsPage.getAllProductNames();
        List<String> expectedNamesDesc = new ArrayList<>(actualNamesDesc);
        expectedNamesDesc.sort(Collections.reverseOrder());
        Assert.assertEquals(actualNamesDesc, expectedNamesDesc, "Products should be sorted Z to A.");

        // Sort A to Z
        productsPage.selectSortOption("Name (A to Z)");
        List<String> actualNamesAsc = productsPage.getAllProductNames();
        List<String> expectedNamesAsc = new ArrayList<>(actualNamesAsc);
        Collections.sort(expectedNamesAsc);
        Assert.assertEquals(actualNamesAsc, expectedNamesAsc, "Products should be sorted A to Z.");
    }

    @Test(priority = 6, description = "Verify sorting products by Price (low to high) and (high to low)")
    public void testProductSortingByPrice() {
        // Sort Price (low to high)
        productsPage.selectSortOption("Price (low to high)");
        List<Double> actualPricesAsc = productsPage.getAllProductPrices();
        List<Double> expectedPricesAsc = new ArrayList<>(actualPricesAsc);
        Collections.sort(expectedPricesAsc);
        Assert.assertEquals(actualPricesAsc, expectedPricesAsc, "Prices should be sorted low to high.");

        // Sort Price (high to low)
        productsPage.selectSortOption("Price (high to low)");
        List<Double> actualPricesDesc = productsPage.getAllProductPrices();
        List<Double> expectedPricesDesc = new ArrayList<>(actualPricesDesc);
        expectedPricesDesc.sort(Collections.reverseOrder());
        Assert.assertEquals(actualPricesDesc, expectedPricesDesc, "Prices should be sorted high to low.");
    }
}

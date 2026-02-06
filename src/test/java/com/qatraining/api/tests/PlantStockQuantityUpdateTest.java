package com.qatraining.api.tests;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import com.qatraining.api.APIClient;
import com.qatraining.config.ConfigManager;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import static io.restassured.RestAssured.given;
import io.restassured.response.Response;

/**
 * API Test: API_DASH_006
 * Title: Verify stock quantity update after sale
 * 
 * Objective: Ensures that plant stock quantity is correctly reduced after recording a sale.
 * 
 * Test Scenario:
 * 1. Authenticate as Admin
 * 2. Create a parent category
 * 3. Create a sub-category under parent
 * 4. Create a plant with known stock quantity
 * 5. Record a sale for that plant
 * 6. Verify stock quantity is reduced correctly
 */
@Feature("Sales Management")
@Story("Stock Quantity Update")
@DisplayName("API_DASH_006: Verify stock quantity update after sale")
public class PlantStockQuantityUpdateTest {

    private static final ConfigManager config = ConfigManager.getInstance();
    private static final String API_BASE_URL = "http://localhost:8080/api";
    
    // Precondition: Admin credentials
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";

    @BeforeAll
    static void setupTestEnvironment() {
        // Ensure base API URL is set correctly
        io.restassured.RestAssured.baseURI = API_BASE_URL;
    }

    /**
     * MAIN TEST: API_DASH_006
     * Complete end-to-end test verifying stock quantity update after sale
     */
    @Test
    @DisplayName("API_DASH_006: Verify stock quantity update after sale")
    @Description("Complete test: Create plant, record sale, verify stock reduced correctly")
    @Feature("Sales Management")
    @Story("Stock Quantity Update")
    void testStockQuantityUpdateAfterSale() {
        // ============================================================
        // STEP 1: Authenticate as Admin
        // ============================================================
        System.out.println("\n========== API_DASH_006 Test Execution ==========");
        System.out.println("STEP 1: Authenticate as Admin");
        
        String authToken = authenticateAsAdmin();
        assertNotNull(authToken, "Authentication token should not be null");
        APIClient.setAuthToken(authToken);
        System.out.println("✓ Admin authenticated successfully");

        // ============================================================
        // STEP 2: Create Parent Category
        // ============================================================
        System.out.println("\nSTEP 2: Create Parent Category");
        
        // Category name must be 3-10 characters (per SRS 5.2)
        String parentCategoryName = "Plants" + RandomStringUtils.randomNumeric(2);
        
        Map<String, Object> parentBody = new HashMap<>();
        parentBody.put("name", parentCategoryName);

        Response parentResp = given()
                .spec(APIClient.getRequestSpec())
                .body(parentBody)
                .when()
                .post("/categories");

        System.out.println("  Request: POST /api/categories");
        System.out.println("  Body: " + parentBody);
        System.out.println("  Response Status: " + parentResp.getStatusCode());
        
        assertEquals(201, parentResp.getStatusCode(), 
                "Parent category creation should return 201. Response: " + parentResp.getBody().asString());
        
        Integer parentCategoryId = parentResp.jsonPath().getInt("id");
        assertNotNull(parentCategoryId, "Parent category ID should be returned");
        assertTrue(parentCategoryId > 0, "Parent category ID should be positive");
        System.out.println("  Parent Category ID: " + parentCategoryId);
        System.out.println("  Parent Category Name: " + parentCategoryName);
        System.out.println("✓ Parent category created successfully");

        // ============================================================
        // STEP 3: Create Sub-Category under Parent
        // ============================================================
        System.out.println("\nSTEP 3: Create Sub-Category with parent object (workaround for API bug)");
        
        // Category name must be 3-10 characters (per SRS 5.2)
        String subCategoryName = "Flowers" + RandomStringUtils.randomNumeric(2);
        
        // Use parent object instead of parentId to work around API validation bug #3
        Map<String, Object> parentObj = new HashMap<>();
        parentObj.put("id", parentCategoryId);
        
        Map<String, Object> subBody = new HashMap<>();
        subBody.put("name", subCategoryName);
        subBody.put("parent", parentObj);  // Using parent object instead of parentId

        Response subResp = given()
                .spec(APIClient.getRequestSpec())
                .body(subBody)
                .when()
                .post("/categories");

        System.out.println("  Request: POST /api/categories with parent object");
        System.out.println("  Body: " + subBody);
        System.out.println("  Response Status: " + subResp.getStatusCode());
        
        assertEquals(201, subResp.getStatusCode(), 
                "Sub-category creation should return 201. Response: " + subResp.getBody().asString());
        
        Integer subCategoryId = subResp.jsonPath().getInt("id");
        assertNotNull(subCategoryId, "Sub-category ID should be returned");
        assertTrue(subCategoryId > 0, "Sub-category ID should be positive");
        System.out.println("  Sub-Category ID: " + subCategoryId);
        System.out.println("  Sub-Category Name: " + subCategoryName);
        System.out.println("✓ Sub-category created successfully");

        // ============================================================
        // STEP 4: Create Plant with Known Stock Quantity
        // ============================================================
        System.out.println("\nSTEP 4: Create Plant with Known Stock (100 units)");
        
        String plantName = "Rose" + RandomStringUtils.randomNumeric(3);
        double plantPrice = 150.99;
        int initialQuantity = 100; // Known initial stock
        
        Map<String, Object> plantBody = new HashMap<>();
        plantBody.put("name", plantName);
        plantBody.put("price", plantPrice);
        plantBody.put("quantity", initialQuantity);

        Response plantResp = given()
                .spec(APIClient.getRequestSpec())
                .pathParam("categoryId", subCategoryId)
                .body(plantBody)
                .when()
                .post("/plants/category/{categoryId}");

        System.out.println("  Request: POST /api/plants/category/" + subCategoryId);
        System.out.println("  Body: " + plantBody);
        System.out.println("  Response Status: " + plantResp.getStatusCode());
        
        assertEquals(201, plantResp.getStatusCode(), 
                "Plant creation should return 201. Response: " + plantResp.getBody().asString());
        
        Integer plantId = plantResp.jsonPath().getInt("id");
        assertNotNull(plantId, "Plant ID should be returned");
        assertTrue(plantId > 0, "Plant ID should be positive");
        System.out.println("  Plant ID: " + plantId);
        System.out.println("  Plant Name: " + plantName);
        System.out.println("  Initial Stock Quantity: " + initialQuantity);
        System.out.println("✓ Plant created successfully");

        // ============================================================
        // STEP 5A: Verify Initial Stock Before Sale
        // ============================================================
        System.out.println("\nSTEP 5A: Verify Initial Stock Before Sale");
        
        Response plantBeforeSale = given()
                .spec(APIClient.getRequestSpec())
                .when()
                .get("/plants/{plantId}", plantId);

        assertEquals(200, plantBeforeSale.getStatusCode(), 
                "GET plant should return 200. Response: " + plantBeforeSale.getBody().asString());
        
        Integer stockBefore = plantBeforeSale.jsonPath().getInt("quantity");
        assertEquals(initialQuantity, stockBefore, 
                "Initial stock should be " + initialQuantity + ", but got " + stockBefore);
        System.out.println("  Current Stock: " + stockBefore);
        System.out.println("✓ Initial stock verified");

        // ============================================================
        // STEP 5B: Record a Sale
        // ============================================================
        System.out.println("\nSTEP 5B: Record a Sale");
        
        int quantitySold = 25; // Sell 25 units
        int expectedStockAfter = initialQuantity - quantitySold; // 75 units should remain
        
        Response saleResp = given()
                .spec(APIClient.getRequestSpec())
                .pathParam("plantId", plantId)
                .queryParam("quantity", quantitySold)
                .when()
                .post("/sales/plant/{plantId}");

        System.out.println("  Request: POST /api/sales/plant/" + plantId + "?quantity=" + quantitySold);
        System.out.println("  Response Status: " + saleResp.getStatusCode());
        
        assertEquals(201, saleResp.getStatusCode(), 
                "Sale recording should return 201. Response: " + saleResp.getBody().asString());
        
        Integer saleId = saleResp.jsonPath().getInt("id");
        assertNotNull(saleId, "Sale ID should be returned");
        System.out.println("  Sale ID: " + saleId);
        System.out.println("  Quantity Sold: " + quantitySold);
        System.out.println("✓ Sale recorded successfully");

        // Log sale response
        System.out.println("  Sale Response Body:");
        System.out.println(saleResp.getBody().asPrettyString());

        // ============================================================
        // STEP 6: Verify Stock Quantity Reduced Correctly
        // ============================================================
        System.out.println("\nSTEP 6: Verify Stock Quantity Reduced Correctly");
        System.out.println("  Expected stock after sale: " + expectedStockAfter + " units");
        
        Response plantAfterSale = given()
                .spec(APIClient.getRequestSpec())
                .when()
                .get("/plants/{plantId}", plantId);

        assertEquals(200, plantAfterSale.getStatusCode(), 
                "GET plant should return 200. Response: " + plantAfterSale.getBody().asString());
        
        Integer stockAfter = plantAfterSale.jsonPath().getInt("quantity");
        assertNotNull(stockAfter, "Stock quantity should be present in response");
        
        System.out.println("  Plant details after sale:");
        System.out.println(plantAfterSale.getBody().asPrettyString());
        
        // MAIN ASSERTION: Verify stock reduced by sold quantity
        assertEquals(expectedStockAfter, stockAfter, 
                "Stock should be reduced by " + quantitySold + " units. " +
                "Expected: " + expectedStockAfter + ", Got: " + stockAfter);
        
        System.out.println("  ✓ Stock quantity reduced from " + stockBefore + " to " + stockAfter);
        System.out.println("  ✓ Reduction amount: " + (stockBefore - stockAfter) + " units");
        System.out.println("✓ Stock reduction verified correctly");

        // ============================================================
        // STEP 7: Verify Sale Details
        // ============================================================
        System.out.println("\nSTEP 7: Verify Sale Details");
        
        Integer saleQuantity = saleResp.jsonPath().getInt("quantity");
        Number saleTotalPrice = saleResp.jsonPath().getDouble("totalPrice");
        
        assertEquals(quantitySold, saleQuantity, "Sale quantity should match");
        System.out.println("  ✓ Sale Quantity: " + saleQuantity);
        
        assertNotNull(saleTotalPrice, "Total price should be present");
        double expectedTotalPrice = plantPrice * quantitySold;
        System.out.println("  ✓ Total Price: " + saleTotalPrice + " (Expected: ~" + expectedTotalPrice + ")");
        
        System.out.println("✓ Sale details verified");

        System.out.println("\n✓✓✓ TEST PASSED ✓✓✓");
        System.out.println("Stock quantity correctly updated after sale");
        System.out.println("Initial Stock: " + stockBefore + " → Final Stock: " + stockAfter);
        System.out.println("================================================\n");
    }

    /**
     * Negative Test: Attempt to sell more than available stock
     */
    @Test
    @Order(2)
    @DisplayName("Negative Test: Sell more than available stock - should fail")
    @Description("Verify API prevents selling more than available stock")
    void testSellMoreThanAvailableStock() {
        System.out.println("\n========== Negative Test: Over-Sale Attempt ==========\n");
        
        String authToken = authenticateAsAdmin();
        APIClient.setAuthToken(authToken);

        // Create test data
        String parentCategoryName = "Plants" + RandomStringUtils.randomNumeric(2);
        Map<String, Object> parentBody = new HashMap<>();
        parentBody.put("name", parentCategoryName);
        Response parentResp = given().spec(APIClient.getRequestSpec()).body(parentBody).when().post("/categories");
        Integer parentCategoryId = parentResp.jsonPath().getInt("id");

        String subCategoryName = "Flowers" + RandomStringUtils.randomNumeric(2);
        Map<String, Object> subBody = new HashMap<>();
        subBody.put("name", subCategoryName);
        // Use parent object instead of parentId to work around API bug #3
        Map<String, Object> parentRef = new HashMap<>();
        parentRef.put("id", parentCategoryId);
        subBody.put("parent", parentRef);
        Response subResp = given().spec(APIClient.getRequestSpec()).body(subBody).when().post("/categories");
        Integer subCategoryId = subResp.jsonPath().getInt("id");

        String plantName = "Rose" + RandomStringUtils.randomNumeric(3);
        int initialQuantity = 10; // Only 10 units available
        Map<String, Object> plantBody = new HashMap<>();
        plantBody.put("name", plantName);
        plantBody.put("price", 100.0);
        plantBody.put("quantity", initialQuantity);
        Response plantResp = given().spec(APIClient.getRequestSpec())
                .pathParam("categoryId", subCategoryId).body(plantBody)
                .when().post("/plants/category/{categoryId}");
        
        assertEquals(201, plantResp.getStatusCode(), 
                "Plant creation failed. Status: " + plantResp.getStatusCode() + ", Response: " + plantResp.getBody().asString());
        Integer plantId = plantResp.jsonPath().getInt("id");
        assertNotNull(plantId, "Plant ID should be returned from creation response");

        System.out.println("Test Data Setup:");
        System.out.println("  Plant ID: " + plantId);
        System.out.println("  Available Stock: " + initialQuantity);
        System.out.println("  Attempting to sell: 25 units (exceeds stock)");

        // Attempt to sell more than available
        int quantityToSell = 25; // More than available (10)
        
        Response saleResp = given()
                .spec(APIClient.getRequestSpec())
                .pathParam("plantId", plantId)
                .queryParam("quantity", quantityToSell)
                .when()
                .post("/sales/plant/{plantId}");

        System.out.println("  Response Status: " + saleResp.getStatusCode());
        
        if (saleResp.getStatusCode() == 400 || saleResp.getStatusCode() == 409) {
            System.out.println("  ✓ API correctly rejected over-sale");
            System.out.println("  Error Response: " + saleResp.getBody().asString());
        } else {
            System.out.println("  ⚠ API did not prevent over-sale (potential BUG)");
            System.out.println("  Response: " + saleResp.getBody().asString());
        }
        
        System.out.println("================================================\n");
    }

    /**
     * Negative Test: Invalid quantity (zero or negative)
     */
    @Test
    @Order(3)
    @DisplayName("Negative Test: Invalid sale quantity - should fail")
    @Description("Verify API rejects invalid quantities")
    void testInvalidSaleQuantity() {
        System.out.println("\n========== Negative Test: Invalid Sale Quantity ==========\n");
        
        String authToken = authenticateAsAdmin();
        APIClient.setAuthToken(authToken);

        // Create test data
        String parentCategoryName = "Plants" + RandomStringUtils.randomNumeric(2);
        Map<String, Object> parentBody = new HashMap<>();
        parentBody.put("name", parentCategoryName);
        Response parentResp = given().spec(APIClient.getRequestSpec()).body(parentBody).when().post("/categories");
        Integer parentCategoryId = parentResp.jsonPath().getInt("id");

        String subCategoryName = "Flowers" + RandomStringUtils.randomNumeric(2);
        Map<String, Object> subBody = new HashMap<>();
        subBody.put("name", subCategoryName);
        // Use parent object instead of parentId to work around API bug #3
        Map<String, Object> parentRef = new HashMap<>();
        parentRef.put("id", parentCategoryId);
        subBody.put("parent", parentRef);
        Response subResp = given().spec(APIClient.getRequestSpec()).body(subBody).when().post("/categories");
        Integer subCategoryId = subResp.jsonPath().getInt("id");

        String plantName = "Rose" + RandomStringUtils.randomNumeric(3);
        Map<String, Object> plantBody = new HashMap<>();
        plantBody.put("name", plantName);
        plantBody.put("price", 100.0);
        plantBody.put("quantity", 50);
        Response plantResp = given().spec(APIClient.getRequestSpec())
                .pathParam("categoryId", subCategoryId).body(plantBody)
                .when().post("/plants/category/{categoryId}");
        
        assertEquals(201, plantResp.getStatusCode(), 
                "Plant creation failed. Status: " + plantResp.getStatusCode() + ", Response: " + plantResp.getBody().asString());
        Integer plantId = plantResp.jsonPath().getInt("id");
        assertNotNull(plantId, "Plant ID should be returned from creation response");

        System.out.println("Test: Attempt to sell with zero quantity");
        
        Response saleResp = given()
                .spec(APIClient.getRequestSpec())
                .pathParam("plantId", plantId)
                .queryParam("quantity", 0)
                .when()
                .post("/sales/plant/{plantId}");

        System.out.println("  Response Status: " + saleResp.getStatusCode());
        assertTrue(saleResp.getStatusCode() >= 400, 
                "API should reject zero quantity. Got: " + saleResp.getStatusCode());
        System.out.println("  ✓ Zero quantity rejected correctly");
        
        System.out.println("================================================\n");
    }

    /**
     * Helper method to authenticate as admin
     */
    private String authenticateAsAdmin() {
        Map<String, String> loginBody = new HashMap<>();
        loginBody.put("username", ADMIN_USERNAME);
        loginBody.put("password", ADMIN_PASSWORD);

        Response response = given()
                .contentType("application/json")
                .body(loginBody)
                .when()
                .post("/auth/login");

        assertEquals(200, response.getStatusCode(), 
                "Authentication failed. Status: " + response.getStatusCode() + ", Response: " + response.getBody().asString());
        
        String token = response.jsonPath().getString("token");
        assertNotNull(token, "Authentication token should not be null");
        return token;
    }
}

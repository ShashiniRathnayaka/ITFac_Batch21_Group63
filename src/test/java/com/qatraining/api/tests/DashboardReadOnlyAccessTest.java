package com.qatraining.api.tests;

import com.qatraining.api.APIClient;
import com.qatraining.config.ConfigManager;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("API_DASH_007: Dashboard Read-Only Access Test")
public class DashboardReadOnlyAccessTest {

    private String userToken;
    private static final ConfigManager config = ConfigManager.getInstance();
    private static final String BASE_URL = "http://localhost:8080/api";

    @BeforeEach
    public void setUp() {
        authenticateAsUser();
    }

    private void authenticateAsUser() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "user");
        credentials.put("password", "user@123");

        Response loginResponse = given()
                .spec(APIClient.getRequestSpec())
                .body(credentials)
                .when().post("/auth/login");

        assertEquals(200, loginResponse.getStatusCode(), "User authentication should succeed");
        userToken = loginResponse.jsonPath().getString("token");
        assertNotNull(userToken, "User token should not be null");
        APIClient.setAuthToken(userToken);
    }

    @Test
    @DisplayName("API_DASH_007: User can retrieve dashboard data in read-only mode - all endpoints return 200 OK")
    public void testUserRetrievesDashboardDataReadOnly() {
        // GET /api/categories as standard user
        Response categoriesResponse = given()
                .spec(APIClient.getRequestSpec())
                .when().get("/categories");

        assertEquals(200, categoriesResponse.getStatusCode(), "User should be able to GET /categories");
        assertTrue(categoriesResponse.getContentType().contains("application/json"), 
                "Categories response should be JSON");
        List<Map<String, Object>> categories = categoriesResponse.jsonPath().getList("$");
        assertNotNull(categories, "Categories should return a list");
        assertTrue(categories.size() > 0, "Categories list should not be empty");
        
        // Verify category structure
        for (Map<String, Object> category : categories) {
            assertNotNull(category.get("id"), "Category should have an id");
            assertNotNull(category.get("name"), "Category should have a name");
        }

        // GET /api/plants as standard user
        Response plantsResponse = given()
                .spec(APIClient.getRequestSpec())
                .when().get("/plants");

        assertEquals(200, plantsResponse.getStatusCode(), "User should be able to GET /plants");
        assertTrue(plantsResponse.getContentType().contains("application/json"), 
                "Plants response should be JSON");
        List<Map<String, Object>> plants = plantsResponse.jsonPath().getList("$");
        assertNotNull(plants, "Plants should return a list");
        assertTrue(plants.size() > 0, "Plants list should not be empty");
        
        // Verify plant structure
        for (Map<String, Object> plant : plants) {
            assertNotNull(plant.get("id"), "Plant should have an id");
            assertNotNull(plant.get("name"), "Plant should have a name");
        }

        // GET /api/sales as standard user
        Response salesResponse = given()
                .spec(APIClient.getRequestSpec())
                .when().get("/sales");

        assertEquals(200, salesResponse.getStatusCode(), "User should be able to GET /sales");
        assertTrue(salesResponse.getContentType().contains("application/json"), 
                "Sales response should be JSON");
        List<Map<String, Object>> sales = salesResponse.jsonPath().getList("$");
        assertNotNull(sales, "Sales should return a list");
        
        // Sales list may be empty if no sales exist
        for (Map<String, Object> sale : sales) {
            assertNotNull(sale.get("id"), "Sale should have an id");
        }
    }

    @Test
    @DisplayName("API_DASH_007: User READ-ONLY - User cannot create category")
    public void testUserCannotCreateCategory() {
        Map<String, Object> categoryData = new HashMap<>();
        categoryData.put("name", "UnauthorizedCategory");

        Response createResponse = given()
                .spec(APIClient.getRequestSpec())
                .body(categoryData)
                .when().post("/categories");

        // User should not have permission to create - expect 403 Forbidden
        assertTrue(createResponse.getStatusCode() >= 400, 
                "User should not be able to create categories (expected 403 or similar)");
    }

    @Test
    @DisplayName("API_DASH_007: User READ-ONLY - User cannot create plant")
    public void testUserCannotCreatePlant() {
        Map<String, Object> plantData = new HashMap<>();
        plantData.put("name", "UnauthorizedPlant");
        plantData.put("categoryId", 1);
        plantData.put("initialStock", 10);

        Response createResponse = given()
                .spec(APIClient.getRequestSpec())
                .body(plantData)
                .when().post("/plants");

        // User should not have permission to create - expect 403 Forbidden
        assertTrue(createResponse.getStatusCode() >= 400, 
                "User should not be able to create plants (expected 403 or similar)");
    }

    @Test
    @DisplayName("API_DASH_007: User READ-ONLY - User cannot create sale")
    public void testUserCannotCreateSale() {
        Map<String, Object> saleData = new HashMap<>();
        saleData.put("plantId", 1);
        saleData.put("quantitySold", 5);

        Response createResponse = given()
                .spec(APIClient.getRequestSpec())
                .body(saleData)
                .when().post("/sales");

        // User should not have permission to create - expect 403 Forbidden
        assertTrue(createResponse.getStatusCode() >= 400, 
                "User should not be able to create sales (expected 403 or similar)");
    }
}

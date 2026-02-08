Feature: Plants API - Retrieve Plant List

  @adminapi
  Scenario: Verify admin can retrieve plant list
    When Admin sends GET request to retrieve plants list
    Then API should return 200 OK for plants list retrieval
    And Response should contain plant records
    And Plant records should have valid structure with id, name, price, quantity, category
    And No data records should be modified

  @nonadminapi
  Scenario: Verify user can retrieve plant list
    When User sends GET request to retrieve plants list
    Then API should return 200 OK for plants list retrieval
    And Response should contain plant records
    And Plant records should have valid structure with id, name, price, quantity, category
    And No data records should be modified

  @nonadminapi
  Scenario: Verify user can retrieve plant by ID
    Given A plant exists for user to fetch by ID
    When User sends GET request to retrieve plant by ID
    Then API should return 200 OK for plant get by ID
    And Response should contain correct plant details for requested ID

  @nonadminapi
  @retrieve-page-with-pagination-user
  Scenario: API_PLANT_USER_009 - Retrieve paginated plant list with default parameters
    When User sends GET request to retrieve paginated plants with default parameters
    Then API should return 200 OK for paginated plants list
    And Response should contain paginated plant data with correct structure
    And Response should include pagination metadata with totalElements, totalPages, and pageInfo
    And Exactly 10 or fewer plants should be returned
    And Response indicates this is the first page

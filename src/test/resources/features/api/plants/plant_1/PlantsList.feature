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
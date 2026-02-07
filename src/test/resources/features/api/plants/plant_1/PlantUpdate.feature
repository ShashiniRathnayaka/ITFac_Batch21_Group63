Feature: Plants API - Admin Update Plant

  @adminapi
  Scenario: Verify admin can update an existing plant
    When Admin sends PUT request to update plant details
    Then API should return 200 OK for plant update
    And Updated plant details should be reflected in response

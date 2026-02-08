Feature: Plants API - Update Plant

  @adminapi
  Scenario: Verify admin can update an existing plant
    When Admin sends PUT request to update plant details
    Then API should return 200 OK for plant update
    And Updated plant details should be reflected in response

  @nonadminapi
  Scenario: Verify normal user cannot update plant details
    Given A plant exists for user to attempt update
    When User sends PUT request to update plant details
    Then API should return 403 Forbidden for plant update
    And Plant data should not be modified after failed update attempt

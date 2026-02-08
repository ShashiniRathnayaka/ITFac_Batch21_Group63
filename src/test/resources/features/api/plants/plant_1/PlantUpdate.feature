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

  @adminapi @categoryupdate
  Scenario: API_PLANT_Admin_CANNOT_UPDATE_CATEGORY_NAME004 - Verify admin can update category name of an existing plant
    Given An admin is authenticated and a valid plant exists
    When Admin sends PUT request to update plant category name with existing sub category name
    Then API should return 200 OK for plant category update
    And Updated plant category name should be reflected in response

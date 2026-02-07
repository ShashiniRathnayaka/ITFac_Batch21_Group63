Feature: Plants API - Delete Plant

  @adminapi
  Scenario: Verify admin can delete an existing plant
    Given An existing plant is available for deletion
    When Admin sends DELETE request to delete the plant
    Then API should return 200 or 204 for plant deletion
    And Plant should not be found in the system

  @nonadminapi
  Scenario: Verify normal user cannot delete a plant
    Given A plant exists for user to attempt delete
    When User sends DELETE request to delete the plant
    Then API should return 403 or 401 for delete attempt
    And Plant should still exist after failed delete attempt

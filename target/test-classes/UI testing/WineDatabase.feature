@Regression
Feature: Testing the correct navigation all over the wineDatabase

  Scenario Outline: Navigation through Regions and Wines menu. Data validation.
    Given user opens the browser and navigate into WineStore
    When user navigates to a "<Region>"
    Then user validates the amount of "<Number>" wines available in current Region
    And user validates the wine names in current Region

    Examples:
      | Region    | Number |
      | Bordeaux  | 25     |
      | Bourgogne | 8      |
      | Champagne | 7      |
      | Languedoc | 0      |
      | Loire     | 11     |
      | Normandie | 1      |

  Scenario Outline: Verify "Like" button feature for all wines
    Given user opens the browser and navigate into WineStore
    When user navigates to a "<Region>"
    Then user clicks on Like button for all the wines in the current Region

    Examples:
      | Region    |
      | Bordeaux  |
      | Bourgogne |
      | Champagne |
      | Languedoc |
      | Loire     |
      | Normandie |

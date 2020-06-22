Feature: Category REST service

  Scenario: Retrieve ALL Categories
    Given there are Category entries
    When an API client requests all Category entries
    Then 4 category entries are returned
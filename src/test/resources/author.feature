Feature: Find all books written by Jack London

  Scenario: find books
    Given method to search books
    When author name is Jack London
#    When all books are saved to array
    Then check if Jack London is an author for all
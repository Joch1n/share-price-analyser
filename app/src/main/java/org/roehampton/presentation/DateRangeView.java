package org.roehampton.presentation;

import org.roehampton.controller.IController;

import org.roehampton.domain.DateRange;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class DateRangeView implements IDateRangeView {

    private final IController controller;
    private final Scanner scanner;
    private final DateTimeFormatter dateFormatter;

    public DateRangeView(IController controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
        this.dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    @Override
    public void displayDateForm() {
        System.out.println("\n--- DATE RANGE SELECTION ---");
        System.out.println("Enter dates in format: YYYY-MM-DD");
        System.out.println("Maximum range: 2 years (730 days)");
        System.out.println("Example: 2024-01-01");
    }

    @Override
    public DateRange getDateRange() {
        try {
            // Get start date from user
            LocalDate startDate = promptForDate("Start Date");
            if (startDate == null) {
                return null;
            }

            // Get end date from user
            LocalDate endDate = promptForDate("End Date");
            if (endDate == null) {
                return null;
            }

            // Create DateRange - this will validate internally
            // (checks that end > start and range <= 730 days)
            DateRange dateRange = new DateRange(startDate, endDate);

            // Validate with controller for additional business rules
            if (!controller.validateDateRange(dateRange)) {
                System.out.println("✗ Date range validation failed");
                return null;
            }

            // Display confirmation
            System.out.printf("✓ Date range: %s (%d days)%n",
                    dateRange, dateRange.getDaysBetween());

            return dateRange;

        } catch (IllegalArgumentException e) {
            System.out.println("✗ Invalid date range: " + e.getMessage());
            return null;
        }
    }


    private LocalDate promptForDate(String prompt) {
        while (true) {
            System.out.print(prompt + " (YYYY-MM-DD) or 'q' to cancel: ");
            String input = scanner.nextLine().trim();

            // Allow user to cancel
            if (input.equalsIgnoreCase("q")) {
                return null;
            }

            try {
                // Parse the date using YYYY-MM-DD format
                LocalDate date = LocalDate.parse(input, dateFormatter);

                // Don't allow future dates
                if (date.isAfter(LocalDate.now())) {
                    System.out.println("✗ Date cannot be in the future");
                    continue;
                }

                return date;

            } catch (DateTimeParseException e) {
                System.out.println("✗ Invalid date format. Use YYYY-MM-DD (e.g., 2024-01-01)");
            }
        }
    }

    @Override
    public boolean validateDateRange(DateRange dateRange) {
        if (dateRange == null) {
            return false;
        }

        try {
            // Send validation request to controller
            return controller.validateDateRange(dateRange);
        } catch (Exception e) {
            System.out.println("✗ Validation error: " + e.getMessage());
            return false;
        }
    }
}

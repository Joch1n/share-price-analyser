package org.roehampton.presentation;

import org.roehampton.controller.IController;
import java.time.LocalDate;

public class DateRangeView implements IDateRangeView {

    private final IController controller;

    public DateRangeView(IController controller) {
        this.controller = controller;
    }

    @Override
    public void displayDateForm() {
        System.out.println("[DateRangeView] Date range form ready (web interface not implemented)");
    }

    @Override
    public boolean validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            System.out.println("[DateRangeView] Dates cannot be null");
            return false;
        }

        try {
            // Validate with controller's business rules
            controller.setDateRange(startDate, endDate);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("[DateRangeView] Validation failed: " + e.getMessage());
            return false;
        }
    }

    public boolean processDateSelection(LocalDate startDate, LocalDate endDate) {
        if (validateDateRange(startDate, endDate)) {
            System.out.println("[DateRangeView] Valid date range: " + startDate + " to " + endDate);
            return true;
        }
        return false;
    }
}
package org.roehampton.presentation;

import org.roehampton.controller.IController;
import org.roehampton.domain.DateRange;

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
    public DateRange getDateRange() {
        throw new UnsupportedOperationException(
                "getDateRange() would be implemented in Sprint 3 web layer"
        );
    }

    @Override
    public boolean validateDateRange(DateRange dateRange) {
        if (dateRange == null) {
            return false;
        }

        try {
            // Validate with controller's business rules
            controller.setDateRange(dateRange.getStartDate(), dateRange.getEndDate());
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("[DateRangeView] Validation failed: " + e.getMessage());
            return false;
        }
    }

    public DateRange createDateRange(java.time.LocalDate startDate, java.time.LocalDate endDate) {
        try {
            DateRange dateRange = new DateRange(startDate, endDate);

            if (validateDateRange(dateRange)) {
                System.out.println("[DateRangeView] Valid date range: " + dateRange);
                return dateRange;
            }

            return null;

        } catch (IllegalArgumentException e) {
            System.out.println("[DateRangeView] Invalid date range: " + e.getMessage());
            return null;
        }
    }
}
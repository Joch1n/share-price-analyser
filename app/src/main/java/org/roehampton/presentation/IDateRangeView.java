package org.roehampton.presentation;

import java.time.LocalDate;

public interface IDateRangeView {

    void displayDateForm();
    boolean validateDateRange(LocalDate startDate, LocalDate endDate);
}
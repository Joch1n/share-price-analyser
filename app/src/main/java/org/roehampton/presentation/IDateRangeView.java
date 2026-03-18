package org.roehampton.presentation;

import org.roehampton.domain.DateRange;

public interface IDateRangeView {

    void displayDateForm();
    DateRange getDateRange();
    boolean validateDateRange(DateRange dateRange);
}

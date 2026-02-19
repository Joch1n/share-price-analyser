package org.roehampton.controller;

import org.roehampton.businesslogic.IDataService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class SharePriceController implements IController {

    private final IDataService dataService;
    private final IChartData chart;

    public SharePriceController(IDataService dataService, IChartData chart) {
        this.dataService = dataService;
        this.chart = chart;
    }

    @Override
    public void loadSingleShare(String symbol, LocalDate start, LocalDate end) {

    }

    @Override
    public void compareShares(String symbol1, String symbol2, LocalDate start, LocalDate end) {

    }

    private void validateDates(LocalDate start, LocalDate end) {
        if (start == null || end == null)
            throw new IllegalArgumentException("Dates cannot be null");

        if (start.isAfter(end))
            throw new IllegalArgumentException("Start date must be before end date");

        long years = ChronoUnit.YEARS.between(start, end);
        if (years > 2)
            throw new IllegalArgumentException("Date range cannot exceed two years");
    }
}
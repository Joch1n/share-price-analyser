package org.roehampton.controller;

import org.roehampton.domain.IDataService;
import org.roehampton.domain.IChartData;
import org.roehampton.domain.SharePrice;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class SharePriceController implements IController {

    private final IDataService dataService;
    private final IChartData chart;

    public SharePriceController(IDataService dataService, IChartData chart) {
        this.dataService = dataService;
        this.chart = chart;
    }

    @Override
    public void loadSingleShare(String symbol, LocalDate start, LocalDate end) {
        validateDates(start, end);
        List<SharePrice> prices = dataService.getSharePrices(symbol, start, end);
        chart.displayChart(prices);
    }

    @Override
    public void compareShares(String symbol1, String symbol2, LocalDate start, LocalDate end) {
        validateDates(start, end);
        List<SharePrice> prices1 = dataService.getSharePrices(symbol1, start, end);
        List<SharePrice> prices2 = dataService.getSharePrices(symbol2, start, end);
        chart.compareCharts(prices1, prices2);
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
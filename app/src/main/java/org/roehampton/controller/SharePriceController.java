package org.roehampton.controller;

import org.roehampton.businesslogic.IDataService;
import org.roehampton.domain.PriceSeries;
import org.roehampton.presentation.IGraphView;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class SharePriceController implements IController {

    private final IDataService dataService;
    private final IGraphView graphView;

    public SharePriceController(IDataService dataService, IGraphView graphView) {
        this.dataService = dataService;
        this.graphView = graphView;
    }

    @Override
    public void loadSingleShare(String symbol, LocalDate start, LocalDate end) {

        validateDates(start, end);
        validateSymbol(symbol);

        PriceSeries series = dataService.getSharePrices(symbol, start, end);
        graphView.displaySingleSeries(series);
    }

    @Override
    public void compareShares(String symbol1, String symbol2,
                              LocalDate start, LocalDate end) {

        validateDates(start, end);
        validateSymbol(symbol1);
        validateSymbol(symbol2);

        PriceSeries series1 = dataService.getSharePrices(symbol1, start, end);
        PriceSeries series2 = dataService.getSharePrices(symbol2, start, end);

        graphView.displayComparison(series1, series2);
    }

    @Override
    public void addToWatchlist(String symbol) {
        dataService.addWatchlistItem(symbol);
    }

    @Override
    public List<String> getWatchlist() {
        return dataService.retrieveWatchlist();
    }

    @Override
    public void viewWatchlistItem(String symbol) {
        System.out.println("[Controller] View watchlist item: " + symbol);
    }

    @Override
    public void handleDataPointClick(int index, double value) {
        System.out.println("[Controller] Data point clicked: index=" + index + ", value=" + value);
    }

    @Override
    public void setDateRange(LocalDate startDate, LocalDate endDate) {

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

    private void validateSymbol(String symbol) {
        if (symbol == null || symbol.isBlank()) {
            throw new IllegalArgumentException("Symbol cannot be empty");
        }
    }
}
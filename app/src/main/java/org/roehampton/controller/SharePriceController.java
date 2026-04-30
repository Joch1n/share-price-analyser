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
    //It poses as the main controller inbetween the data service and the graphview
    public SharePriceController(IDataService dataService, IGraphView graphView) {
        this.dataService = dataService;
        this.graphView = graphView;
    }
    //It stack price data to a single company and returns it as html
    @Override
    public String loadSingleShare(String symbol, LocalDate start, LocalDate end) {
        validateDates(start, end);
        validateSymbol(symbol);
        PriceSeries series = dataService.getSharePrices(symbol, start, end);
        return graphView.displaySingleSeries(series);
    }

    @Override
    public String compareShares(String symbol1, String symbol2, LocalDate start, LocalDate end) {
        validateDates(start, end);
        validateSymbol(symbol1);
        validateSymbol(symbol2);
        PriceSeries series1 = dataService.getSharePrices(symbol1, start, end);
        PriceSeries series2 = dataService.getSharePrices(symbol2, start, end);
        return graphView.displayComparison(series1, series2);
    }
    //It implements th stock symbol to the watchlist
    @Override
    public void addToWatchlist(String symbol) {
        validateSymbol(symbol);
        dataService.addWatchlistItem(symbol);
    }

    @Override
    public List<String> getWatchlist() {
        return dataService.retrieveWatchlist();
    }
    //It stacks a watchlist item with using a 6 month default range and returns it  a html
    @Override
    public String viewWatchlistItem(String symbol) {
        validateSymbol(symbol);
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusMonths(6);
        PriceSeries series = dataService.getSharePrices(symbol, start, end);
        return graphView.displaySingleSeries(series);
    }

    @Override
    public void handleDataPointClick(int index, double value) {
        System.out.println("[Controller] Data point clicked: index=" + index + ", value=" + value);
    }

    @Override
    public void setDateRange(LocalDate startDate, LocalDate endDate) {
    }
     //It displays the validity of the date and it imposes the 2 year limit
    private void validateDates(LocalDate start, LocalDate end) {
        if (start == null || end == null)
            throw new IllegalArgumentException("Dates cannot be null");

        if (start.isAfter(end))
            throw new IllegalArgumentException("Start date must be before end date");

        long years = ChronoUnit.YEARS.between(start, end);
        if (years > 2)
            throw new IllegalArgumentException("Date range cannot transcend two years");
    }
    //It demonstrates through validation that the stock symbol is not empty
    private void validateSymbol(String symbol) {
        if (symbol == null || symbol.isBlank()) {
            throw new IllegalArgumentException("Symbol cannot be void");
        }
    }
}
package org.roehampton.controller;

import org.roehampton.businesslogic.IDataService;
import org.roehampton.businesslogic.IGraphService;
import org.roehampton.businesslogic.IWatchlistService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

public class SharePriceController implements IController {

    private final IDataService dataService;
    private final IGraphService graphService;
    private final IWatchlistService watchlistService;

    private List<String> selectedCompanies;
    private LocalDate startDate;
    private LocalDate endDate;

    public SharePriceController(IDataService dataService,
                                IGraphService graphService,
                                IWatchlistService watchlistService) {
        this.dataService = Objects.requireNonNull(dataService);
        this.graphService = Objects.requireNonNull(graphService);
        this.watchlistService = Objects.requireNonNull(watchlistService);
    }

    @Override
    public void loadSingleShare(String symbol, LocalDate start, LocalDate end) {
        validateDates(start, end);
        graphService.getSingleGraphData(symbol, start, end);
    }

    @Override
    public void compareShares(String symbol1, String symbol2, LocalDate start, LocalDate end) {
        validateDates(start, end);

        if (symbol1 == null || symbol1.trim().isEmpty()
                || symbol2 == null || symbol2.trim().isEmpty()) {
            throw new IllegalArgumentException("Both symbols must be provided.");
        }

        if (symbol1.equalsIgnoreCase(symbol2)) {
            throw new IllegalArgumentException("Cannot compare the same company.");
        }

        graphService.getComparisonGraphData(symbol1, symbol2, start, end);
    }

    @Override
    public void setCompanies(List<String> companies) {
        this.selectedCompanies = companies;
    }

    @Override
    public void setDateRange(LocalDate start, LocalDate end) {
        validateDates(start, end);
        this.startDate = start;
        this.endDate = end;
    }

    private void validateDates(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Dates cannot be null");
        }

        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }

        long years = ChronoUnit.YEARS.between(start, end);
        if (years > 2) {
            throw new IllegalArgumentException("Date range cannot exceed two years");
        }
    }

    @Override
    public void addToWatchlist(String symbol) {
        watchlistService.addWatchlistItem(symbol);
    }

    @Override
    public List<String> getWatchlist() {
        return watchlistService.retrieveWatchlist();
    }

    @Override
    public void viewWatchlistItem(String symbol) {
        if (startDate != null && endDate != null) {
            graphService.getSingleGraphData(symbol, startDate, endDate);
        }
    }

    @Override
    public void handleDataPointClick(int index, double value){
    }
}
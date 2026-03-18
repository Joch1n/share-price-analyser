package org.roehampton.controller;

import org.roehampton.businesslogic.IDataService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class SharePriceController implements IController {

    private final IDataService dataService;


    public SharePriceController(IDataService dataService) {
        this.dataService = dataService;

    }

    @Override
    public void loadSingleShare(String symbol, LocalDate start, LocalDate end) {

    }

    @Override
    public void compareShares(String symbol1, String symbol2, LocalDate start, LocalDate end) {

    }

    @Override
    public void setCompanies(List<String> companies) {
        // TODO: implement later
    }

    @Override
    public void setDateRange(LocalDate start, LocalDate end) {
        validateDates(start, end);
        // TODO: implement later
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
   //will stay empty since we arent implementing ui behaviour
    }
}

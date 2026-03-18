package org.roehampton.controller;

import java.time.LocalDate;
import java.util.List;

public interface IController {

    /**
     * Load and display share prices for a single symbol within a date range.
     *
     * @param symbol the stock symbol
     * @param start  the start date
     * @param end    the end date
     */
    void loadSingleShare(String symbol, LocalDate start, LocalDate end);
    void setCompanies(List<String> companies);
    void setDateRange(LocalDate start, LocalDate end);
    /**
     * Load and compare share prices for two symbols within a date range.
     *
     * @param symbol1 the first stock symbol
     * @param symbol2 the second stock symbol
     * @param start   the start date
     * @param end     the end date
     */
    void compareShares(String symbol1, String symbol2, LocalDate start, LocalDate end);

    void addToWatchlist(String symbol);
    List<String> getWatchlist();
    void viewWatchlistItem(String symbol);
}

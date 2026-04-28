package org.roehampton.controller;

import java.time.LocalDate;
import java.util.List;

public interface IController {

    void loadSingleShare(String symbol, LocalDate start, LocalDate end);

    void compareShares(String symbol1, String symbol2, LocalDate start, LocalDate end);

    void addToWatchlist(String symbol);

    List<String> getWatchlist();

    void viewWatchlistItem(String symbol);

    void handleDataPointClick(int index, double value);

    void setDateRange(LocalDate startDate, LocalDate endDate);
}
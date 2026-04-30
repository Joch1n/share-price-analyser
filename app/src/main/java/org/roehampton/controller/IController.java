package org.roehampton.controller;

import java.time.LocalDate;
import java.util.List;

public interface IController {

    String loadSingleShare(String symbol, LocalDate start, LocalDate end);

    String compareShares(String symbol1, String symbol2, LocalDate start, LocalDate end);

    void addToWatchlist(String symbol);

    List<String> getWatchlist();

    String viewWatchlistItem(String symbol);

    void handleDataPointClick(int index, double value);

    void setDateRange(LocalDate startDate, LocalDate endDate);
}
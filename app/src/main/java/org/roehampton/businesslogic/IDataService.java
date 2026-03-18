package org.roehampton.businesslogic;

import org.roehampton.domain.PriceSeries;

import java.time.LocalDate;

import java.util.List;

public interface IDataService {
    PriceSeries getSharePrices(String symbol, LocalDate to, LocalDate from);

    void addWatchlistItem(String symbol);
    List<String> retrieveWatchlist();

}
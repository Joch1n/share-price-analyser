package org.roehampton.businesslogic;

import org.roehampton.domain.PriceSeries;
import org.roehampton.domain.Watchlist;
import org.roehampton.domain.WatchlistItem;

import java.time.LocalDate;

public interface IDataService {

    PriceSeries getSharePrices(String symbol, LocalDate from, LocalDate to);

    void addWatchlistItem(WatchlistItem item);

    Watchlist retrieveWatchlist();
}
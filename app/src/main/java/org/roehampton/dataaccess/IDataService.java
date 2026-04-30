package org.roehampton.dataaccess;

import org.roehampton.domain.PriceSeries;
import org.roehampton.domain.Watchlist;
import org.roehampton.domain.WatchlistItem;

import java.time.LocalDate;

// Interface exposed by the compound component SharePriceData
public interface IDataService {

    PriceSeries getSharePrices(String symbol, LocalDate from, LocalDate to);

    void saveWatchlistItem(WatchlistItem item);

    Watchlist getWatchlist();
}
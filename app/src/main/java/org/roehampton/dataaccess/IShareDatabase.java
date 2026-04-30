package org.roehampton.dataaccess;

import org.roehampton.domain.PriceSeries;
import org.roehampton.domain.Watchlist;
import org.roehampton.domain.WatchlistItem;

import java.time.LocalDate;

public interface IShareDatabase {

    // Check if full data is stored, result determines if fetching from API or database
    enum DataFound {

        FOUND,
        NOT_FOUND,
        PARTIAL;
    }

    DataFound dbCheck(String symbol, LocalDate from, LocalDate to);

    void storeData(PriceSeries priceSeries);

    PriceSeries getStoredData(String symbol, LocalDate from, LocalDate to);

    void saveWatchlistItem(WatchlistItem item);

    Watchlist getWatchlist();
}

package org.roehampton.dataaccess;

import org.roehampton.domain.PriceSeries;
import org.roehampton.domain.Watchlist;
import org.roehampton.domain.WatchlistItem;

import java.time.LocalDate;

// Compound data access component
// Exposes one external interface and delegates internally to APIClient and ShareDatabase
public class SharePriceData implements IDataService {

    private final IAPIClient apiClient;
    private final IShareDatabase shareDatabase;

    public SharePriceData(IAPIClient apiClient, IShareDatabase shareDatabase) {
        
        if (apiClient == null) {
            throw new IllegalArgumentException("APIClient cannot be null.");
        }

        if (shareDatabase == null) {
            throw new IllegalArgumentException("ShareDatabase cannot be null.");
        }

        this.apiClient = apiClient;
        this.shareDatabase = shareDatabase;
    }

    @Override
    public PriceSeries getSharePrices(String symbol, LocalDate from, LocalDate to) {

        IShareDatabase.DataFound status = shareDatabase.dbCheck(symbol, from, to);

        if (status == IShareDatabase.DataFound.FOUND) {
            return shareDatabase.getStoredData(symbol, from, to);
        }

        PriceSeries apiData = apiClient.getSharePrices(symbol, from, to);
        shareDatabase.storeData(apiData);

        return shareDatabase.getStoredData(symbol, from, to);
    }

    @Override
    public void saveWatchlistItem(WatchlistItem item) {
        shareDatabase.saveWatchlistItem(item);
    }

    @Override
    public Watchlist getWatchlist() {
        return shareDatabase.getWatchlist();
    }
}
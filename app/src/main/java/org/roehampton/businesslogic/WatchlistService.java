package org.roehampton.businesslogic;

import org.roehampton.dataaccess.IShareDatabase;

import java.util.List;
import java.util.Objects;

public class WatchlistService implements IWatchlistService {

    private final IShareDatabase db;

    public WatchlistService(IShareDatabase db) {
        this.db = Objects.requireNonNull(db);
    }

    @Override
    public void addWatchlistItem(String symbol) {
        if (symbol == null || symbol.trim().isEmpty()) {
            throw new IllegalArgumentException("Symbol must be provided.");
        }

        db.saveWatchlistItem(symbol.trim().toUpperCase());
    }

    @Override
    public List<String> retrieveWatchlist() {
        return db.getWatchlist();
    }
}
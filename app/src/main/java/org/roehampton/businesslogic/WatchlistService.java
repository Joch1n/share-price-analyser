package org.roehampton.businesslogic;

import org.roehampton.dataaccess.IShareDatabase;
import org.roehampton.domain.Watchlist;
import org.roehampton.domain.WatchlistItem;

import java.util.Objects;

public class WatchlistService implements IWatchlistService {

    private final IShareDatabase db;

    public WatchlistService(IShareDatabase db) {
        this.db = Objects.requireNonNull(db);
    }

    @Override
    public void addWatchlistItem(WatchlistItem item) {

        db.saveWatchlistItem(item);
    }

    @Override
    public Watchlist retrieveWatchlist() {
        return db.getWatchlist();
    }
}
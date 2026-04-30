package org.roehampton.businesslogic;

import org.roehampton.domain.Watchlist;
import org.roehampton.domain.WatchlistItem;

public interface IWatchlistService {
    void addWatchlistItem(WatchlistItem item);
    Watchlist retrieveWatchlist();
}
package org.roehampton.businesslogic;

import java.util.List;

public interface IWatchlistService {
    void addWatchlistItem(String symbol);
    List<String> retrieveWatchlist();
}
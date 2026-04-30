package org.roehampton.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Represents a collection of WatchlistItems
public class Watchlist {

    private final List<WatchlistItem> items;

    // Creates empty Watchlist for manual addition of items
    public Watchlist() {
        this.items = new ArrayList<>();
    }

    // Creates a Watchlist object from Watchlist items
    public Watchlist(List<WatchlistItem> items) {

        if (items == null) {
            throw new IllegalArgumentException("Watchlist items cannot be null.");
        }

        this.items = new ArrayList<>(items);
    }

    public void addItem(WatchlistItem item) {

        if (item == null) {
            throw new IllegalArgumentException("Watchlist item cannot be null.");
        }

        items.add(item);
    }

    public List<WatchlistItem> getItems() {
        return Collections.unmodifiableList(items);
    }

}

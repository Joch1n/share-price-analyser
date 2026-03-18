package org.roehampton.presentation;

import java.util.List;

public interface IWatchlistView {

    void displayWatchlistMenu();

    String getSymbolToAdd();

    boolean validateSymbol(String symbol);

    void showAddSuccess(String symbol);

    void showAddFailure(String message);

    void displayWatchlist(List<String> watchlist);

    String getSelectedWatchlistItem();

    void showMessage(String message);
}
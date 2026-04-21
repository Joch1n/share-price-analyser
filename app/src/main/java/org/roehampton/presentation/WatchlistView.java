package org.roehampton.presentation;

import org.roehampton.controller.IController;

import java.util.List;
import java.util.Scanner;

public class WatchlistView implements IWatchlistView {

    private final IController controller;
    private final Scanner scanner;

    public WatchlistView(IController controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void displayWatchlistMenu() {
        System.out.println("\n--- WATCHLIST ---");
        System.out.println("1. Add company to watchlist");
        System.out.println("2. View watchlist");
        System.out.println("3. View watchlist item");
        System.out.println("4. Back");
    }

    @Override
    public String getSymbolToAdd() {
        System.out.print("Enter stock symbol to add (or 'q' to cancel): ");
        String symbol = scanner.nextLine().trim().toUpperCase();

        if (symbol.equalsIgnoreCase("q")) {
            return null;
        }

        if (!validateSymbol(symbol)) {
            System.out.println("✗ Invalid symbol format");
            return null;
        }

        return symbol;
    }

    @Override
    public boolean validateSymbol(String symbol) {
        if (symbol == null || symbol.isEmpty()) {
            return false;
        }

        return symbol.matches("^[A-Z]{1,5}$");
    }

    @Override
    public void showAddSuccess(String symbol) {
        System.out.println("✓ " + symbol + " added to watchlist");
    }

    @Override
    public void showAddFailure(String message) {
        System.out.println("✗ Could not add to watchlist: " + message);
    }

    @Override
    public void displayWatchlist(List<String> watchlist) {
        System.out.println("\n--- SAVED WATCHLIST ---");

        if (watchlist == null || watchlist.isEmpty()) {
            System.out.println("Watchlist is empty");
            return;
        }

        for (int i = 0; i < watchlist.size(); i++) {
            System.out.println((i + 1) + ". " + watchlist.get(i));
        }
    }

    @Override
    public String getSelectedWatchlistItem() {
        System.out.print("Enter watchlist symbol to view (or 'q' to cancel): ");
        String symbol = scanner.nextLine().trim().toUpperCase();

        if (symbol.equalsIgnoreCase("q")) {
            return null;
        }

        if (!validateSymbol(symbol)) {
            System.out.println("✗ Invalid symbol format");
            return null;
        }

        return symbol;
    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void processAddToWatchlist(String symbol) {
        if (!validateSymbol(symbol)) {
            showAddFailure("Invalid symbol format");
            return;
        }

        try {
            controller.addToWatchlist(symbol);
            showAddSuccess(symbol);
        } catch (Exception e) {
            showAddFailure(e.getMessage());
        }
    }

    @Override
    public void processViewWatchlist() {
        displayWatchlist(controller.getWatchlist());
    }

    @Override
    public void processViewWatchlistItem(String symbol) {
        if (!validateSymbol(symbol)) {
            showMessage("✗ Invalid symbol format");
            return;
        }

        controller.viewWatchlistItem(symbol);
        showMessage("Viewing watchlist item: " + symbol);
    }
}
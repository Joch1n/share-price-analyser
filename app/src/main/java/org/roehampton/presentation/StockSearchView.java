package org.roehampton.presentation;

import org.roehampton.controller.IController;
import java.time.LocalDate;

public class StockSearchView implements IStockSearchView {

    private final IController controller;

    public StockSearchView(IController controller) {
        this.controller = controller;
    }

    @Override
    public void displaySearchForm() {
        System.out.println("[StockSearchView] Search form ready");
    }

    @Override
    public String getSymbol() {
        throw new UnsupportedOperationException("Symbol provided via web request parameters");
    }

    @Override
    public String[] getTwoSymbols() {
        throw new UnsupportedOperationException("Symbols provided via web request parameters");
    }

    @Override
    public boolean validateSymbol(String symbol) {
        if (symbol == null || symbol.isEmpty()) return false;
        return symbol.matches("^[A-Z]{1,5}$");
    }

    public String processSearchRequest(String symbol, LocalDate startDate, LocalDate endDate) {
        if (!validateSymbol(symbol)) {
            System.out.println("[StockSearchView] Invalid symbol: " + symbol);
            return "Invalid symbol: " + symbol;
        }
        System.out.println("[StockSearchView] Sending search to controller: " + symbol);
        return controller.loadSingleShare(symbol, startDate, endDate);
    }

    public String processComparisonRequest(String symbol1, String symbol2,
                                           LocalDate startDate, LocalDate endDate) {
        if (!validateSymbol(symbol1) || !validateSymbol(symbol2)) {
            System.out.println("[StockSearchView] Invalid symbols");
            return "Invalid symbols provided.";
        }
        if (symbol1.equals(symbol2)) {
            System.out.println("[StockSearchView] Cannot compare a stock with itself");
            return "Cannot compare a stock with itself.";
        }
        System.out.println("[StockSearchView] Sending comparison to controller: "
                + symbol1 + " vs " + symbol2);
        return controller.compareShares(symbol1, symbol2, startDate, endDate);
    }
}
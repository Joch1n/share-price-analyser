package org.roehampton.presentation;

import org.roehampton.controller.IController;

public class CompanySearchView implements ICompanySearchView {

    private final IController controller;

    public CompanySearchView(IController controller) {
        this.controller = controller;
    }

    @Override
    public void displaySearchForm() {
        System.out.println("[CompanySearchView] Search form ready (web interface not implemented)");
    }

    @Override
    public String getSymbol() {
        throw new UnsupportedOperationException(
                "getSymbol() would be implemented in Sprint 3 web layer"
        );
    }

    @Override
    public String[] getTwoSymbols() {
        throw new UnsupportedOperationException(
                "getTwoSymbols() would be implemented in Sprint 3 web layer"
        );
    }

    @Override
    public boolean validateSymbol(String symbol) {

        if (symbol == null || symbol.isEmpty()) {
            return false;
        }

        // Symbol must be 1-5 uppercase letters
        return symbol.matches("^[A-Z]{1,5}$");
    }

    /**
     * Process a search request (would be called by web controller)
     *
     * @param symbol Stock symbol from web form
     * @param start Start date from web form
     * @param end End date from web form
     */
    public void processSearchRequest(String symbol, java.time.LocalDate start, java.time.LocalDate end) {
        if (!validateSymbol(symbol)) {
            System.out.println("[CompanySearchView] Invalid symbol: " + symbol);
            return;
        }

        System.out.println("[CompanySearchView] Sending search request to controller: " + symbol);
        controller.loadSingleShare(symbol, start, end);
    }

    public void processComparisonRequest(String symbol1, String symbol2,
                                         java.time.LocalDate start, java.time.LocalDate end) {
        if (!validateSymbol(symbol1) || !validateSymbol(symbol2)) {
            System.out.println("[CompanySearchView] Invalid symbols");
            return;
        }

        if (symbol1.equals(symbol2)) {
            System.out.println("[CompanySearchView] Cannot compare a stock with itself");
            return;
        }

        System.out.println("[CompanySearchView] Sending comparison request to controller: "
                + symbol1 + " vs " + symbol2);
        controller.compareShares(symbol1, symbol2, start, end);
    }
}
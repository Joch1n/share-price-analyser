package org.roehampton.compound;

import org.roehampton.controller.IController;
import org.roehampton.presentation.CompanySearchView;
import org.roehampton.presentation.DateRangeView;
import org.roehampton.soa.IStockService;

import java.time.LocalDate;

public class StockSearchComponent {

    // Sub-components composed into this compound component
    private final CompanySearchView companySearchView;
    private final DateRangeView dateRangeView;
    private final IController controller;
    private final IStockService stockService;

    public StockSearchComponent(IController controller, IStockService stockService) {
        this.controller = controller;
        this.stockService = stockService;
        this.companySearchView = new CompanySearchView(controller);
        this.dateRangeView = new DateRangeView(controller);
    }

    public String handleSingleStockRequest(String symbol, LocalDate startDate, LocalDate endDate) {
        // Step 1: validate symbol using CompanySearchView logic
        if (!companySearchView.validateSymbol(symbol)) {
            return "Invalid symbol '" + symbol + "'. Must be 1–5 uppercase letters.";
        }

        // Step 2: validate date range using DateRangeView logic
        if (!dateRangeView.validateDateRange(startDate, endDate)) {
            return "Invalid date range. Ensure start is before end and within the last 2 years.";
        }

        // Step 3: if both valid, process the search
        companySearchView.processSearchRequest(symbol, startDate, endDate);
        return null; // null = success
    }

    /**
     * Handles a user request to compare two stocks.
     * Validates both symbols and the shared date range.
     *
     * @param symbol1   First ticker symbol
     * @param symbol2   Second ticker symbol
     * @param startDate Start of the date range
     * @param endDate   End of the date range
     * @return          Error message if invalid, or null on success
     */
    public String handleComparisonRequest(String symbol1, String symbol2,
                                          LocalDate startDate, LocalDate endDate) {
        if (!companySearchView.validateSymbol(symbol1))
            return "Invalid first symbol: " + symbol1;

        if (!companySearchView.validateSymbol(symbol2))
            return "Invalid second symbol: " + symbol2;

        if (symbol1.equalsIgnoreCase(symbol2))
            return "Cannot compare a stock with itself.";

        if (!dateRangeView.validateDateRange(startDate, endDate))
            return "Invalid date range.";

        companySearchView.processComparisonRequest(symbol1, symbol2, startDate, endDate);
        return null;
    }

    // Expose sub-component interfaces for targeted UI interactions
    public CompanySearchView getCompanySearchView() { return companySearchView; }
    public DateRangeView getDateRangeView() { return dateRangeView; }
}

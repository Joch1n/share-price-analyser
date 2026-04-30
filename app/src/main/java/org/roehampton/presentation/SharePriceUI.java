package org.roehampton.presentation;

import org.roehampton.controller.IController;
import org.roehampton.presentation.*;

import java.time.LocalDate;

public class SharePriceUI {

    // ── Sub-components (diagram: inner components of SharePriceUI) ─────────
    private final UISkeleton uiSkeleton;
    private final StockSearchView stockSearchView;
    private final DateRangeView dateRangeView;
    private final GraphView graphView;
    private final WatchlistView watchlistView;

    public SharePriceUI(IController controller) {
        // Instantiate sub-components (diagram: inner <<component>> boxes)
        this.stockSearchView = new StockSearchView(controller);
        this.dateRangeView   = new DateRangeView(controller);
        this.graphView       = new GraphView(controller);
        this.watchlistView   = new WatchlistView(controller);

        // UISkeleton composes the other views via their interfaces
        // (diagram: <<delegate>> arrows from UISkeleton)
        this.uiSkeleton = new UISkeleton(
                stockSearchView,   // via IStockSearchView
                watchlistView,     // via IWatchlistView
                graphView,         // via IGraphView
                dateRangeView      // via IDateRangeView
        );
    }

    // ── Provided interface: IUserInterface ────────────────────────────────
    public String renderHomePage() {
        return uiSkeleton.renderHomePage();
    }

    // ── Provided interface: IStockSearchView ──────────────────────────────

    public String handleSingleStockRequest(String symbol,
                                           LocalDate startDate, LocalDate endDate) {
        // Validate symbol (StockSearchView responsibility)
        if (!stockSearchView.validateSymbol(symbol))
            return "Invalid symbol '" + symbol + "'. Must be 1-5 uppercase letters.";

        // Validate date range (DateRangeView responsibility — diagram: delegate arrow)
        if (!dateRangeView.validateDateRange(startDate, endDate))
            return "Invalid date range. Must be within the last 2 years.";

        // Both valid — delegate to controller via StockSearchView
        return stockSearchView.processSearchRequest(symbol, startDate, endDate);
    }

    // ── Provided interface: IStockSearchView (comparison) ─────────────────
    public String handleComparisonRequest(String symbol1, String symbol2,
                                          LocalDate startDate, LocalDate endDate) {
        if (!stockSearchView.validateSymbol(symbol1))
            return "Invalid first symbol: " + symbol1;

        if (!stockSearchView.validateSymbol(symbol2))
            return "Invalid second symbol: " + symbol2;

        if (symbol1.equalsIgnoreCase(symbol2))
            return "Cannot compare a stock with itself.";

        if (!dateRangeView.validateDateRange(startDate, endDate))
            return "Invalid date range.";

        return stockSearchView.processComparisonRequest(symbol1, symbol2, startDate, endDate);
    }

    // ── Provided interface: IWatchlistView ────────────────────────────────
    public String addToWatchlist(String symbol) {
        if (!watchlistView.validateSymbol(symbol))
            return "Invalid symbol: " + symbol;
        watchlistView.processAddToWatchlist(symbol);
        return null;
    }

    // ── Expose sub-components for Spring wiring and direct access ──────────
    public UISkeleton       getUiSkeleton()       { return uiSkeleton; }
    public StockSearchView  getStockSearchView()   { return stockSearchView; }
    public DateRangeView    getDateRangeView()     { return dateRangeView; }
    public GraphView        getGraphView()         { return graphView; }
    public WatchlistView    getWatchlistView()     { return watchlistView; }
}

package org.roehampton.presentation;

import org.roehampton.controller.IController;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Component
@RestController
public class SharePriceUI {

    private final UISkeleton uiSkeleton;
    private final StockSearchView stockSearchView;
    private final DateRangeView dateRangeView;
    private final GraphView graphView;
    private final WatchlistView watchlistView;

    public SharePriceUI(IController controller) {
        this.stockSearchView = new StockSearchView(controller);
        this.dateRangeView   = new DateRangeView(controller);
        this.graphView       = new GraphView(controller);
        this.watchlistView   = new WatchlistView(controller);
        this.uiSkeleton = new UISkeleton(
                stockSearchView,
                dateRangeView,
                watchlistView,
                graphView
        );
    }

    @GetMapping("/")
    public String renderHomePage() {
        return uiSkeleton.renderHomePage();
    }

    public String handleSingleStockRequest(String symbol,
                                           LocalDate startDate, LocalDate endDate) {
        if (!stockSearchView.validateSymbol(symbol))
            return "Invalid symbol '" + symbol + "'. Must be 1-5 uppercase letters.";
        if (!dateRangeView.validateDateRange(startDate, endDate))
            return "Invalid date range. Must be within the last 2 years.";
        return stockSearchView.processSearchRequest(symbol, startDate, endDate);
    }

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

    public String addToWatchlist(String symbol) {
        if (!watchlistView.validateSymbol(symbol))
            return "Invalid symbol: " + symbol;
        watchlistView.processAddToWatchlist(symbol);
        return null;
    }

    public UISkeleton      getUiSkeleton()     { return uiSkeleton; }
    public StockSearchView getStockSearchView() { return stockSearchView; }
    public DateRangeView   getDateRangeView()   { return dateRangeView; }
    public GraphView       getGraphView()       { return graphView; }
    public WatchlistView   getWatchlistView()   { return watchlistView; }
}
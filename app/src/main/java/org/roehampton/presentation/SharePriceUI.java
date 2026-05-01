package org.roehampton.presentation;

import org.roehampton.controller.IController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SharePriceUI {

    private final UISkeleton uiSkeleton;
    private final StockSearchView stockSearchView;
    private final DateRangeView dateRangeView;
    private final GraphView graphView;
    private final WatchlistView watchlistView;

    private final List<WatchlistItem> watchlist = new ArrayList<>();

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

    public String renderHomePage() {
        return uiSkeleton.renderHomePage();
    }

    public String handleSingleStockRequest(String symbol,
                                           String from,
                                           String to) {
        LocalDate startDate = LocalDate.parse(from);
        LocalDate endDate = LocalDate.parse(to);

        symbol = symbol.toUpperCase();

        if (!stockSearchView.validateSymbol(symbol)) {
            return "<p>Invalid symbol '" + symbol + "'. Must be 1-5 uppercase letters.</p>";
        }

        if (!dateRangeView.validateDateRange(startDate, endDate)) {
            return "<p>Invalid date range. Must be within the last 2 years.</p>";
        }

        graphView.configureSingleGraph(symbol, startDate, endDate);
        return stockSearchView.processSearchRequest(symbol, startDate, endDate);
    }

    public String handleComparisonRequest(String symbol1,
                                          String symbol2,
                                          String from,
                                          String to) {
        LocalDate startDate = LocalDate.parse(from);
        LocalDate endDate = LocalDate.parse(to);

        symbol1 = symbol1.toUpperCase();
        symbol2 = symbol2.toUpperCase();

        if (!stockSearchView.validateSymbol(symbol1)) {
            return "<p>Invalid first symbol: " + symbol1 + "</p>";
        }

        if (!stockSearchView.validateSymbol(symbol2)) {
            return "<p>Invalid second symbol: " + symbol2 + "</p>";
        }

        if (symbol1.equalsIgnoreCase(symbol2)) {
            return "<p>Cannot compare a stock with itself.</p>";
        }

        if (!dateRangeView.validateDateRange(startDate, endDate)) {
            return "<p>Invalid date range.</p>";
        }

        graphView.configureComparisonGraph(symbol1, symbol2, startDate, endDate);
        return stockSearchView.processComparisonRequest(symbol1, symbol2, startDate, endDate);
    }

    public List<WatchlistItem> getWatchlist() {
        return watchlist;
    }

    public WatchlistResponse addToWatchlist(String symbol,
                                            String from,
                                            String to) {
        LocalDate startDate = LocalDate.parse(from);
        LocalDate endDate = LocalDate.parse(to);

        symbol = symbol.toUpperCase();

        if (!watchlistView.validateSymbol(symbol)) {
            return new WatchlistResponse(false, "Invalid symbol: " + symbol);
        }

        if (!dateRangeView.validateDateRange(startDate, endDate)) {
            return new WatchlistResponse(false, "Invalid date range.");
        }

        final String watchSymbol = symbol;
        final String watchFrom = from;
        final String watchTo = to;

        boolean alreadyExists = watchlist.stream().anyMatch(item ->
                item.symbol().equals(watchSymbol)
                        && item.from().equals(watchFrom)
                        && item.to().equals(watchTo)
        );

        if (!alreadyExists) {
            watchlist.add(new WatchlistItem(watchSymbol, watchFrom, watchTo));
        }

        return new WatchlistResponse(
                true,
                watchSymbol + " added to watchlist for " + watchFrom + " to " + watchTo + "."
        );
    }

    public record WatchlistItem(String symbol, String from, String to) {
    }

    public record WatchlistResponse(boolean success, String message) {
    }
}
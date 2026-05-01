package org.roehampton.controller;

import org.roehampton.businesslogic.IGraphService;
import org.roehampton.businesslogic.IWatchlistService;
import org.roehampton.domain.PriceSeries;
import org.roehampton.domain.WatchlistItem;
import org.roehampton.presentation.IGraphView;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class SharePriceController implements IController {

    private final IGraphService graphService;
    private final IWatchlistService watchlistService;
    private final IGraphView graphView;

    public SharePriceController(IGraphService graphService,
                                IWatchlistService watchlistService,
                                IGraphView graphView) {
        this.graphService = graphService;
        this.watchlistService = watchlistService;
        this.graphView = graphView;
    }

    @Override
    public String loadSingleShare(String symbol, LocalDate start, LocalDate end) {
        PriceSeries series = graphService.getSingleGraphData(symbol, start, end);
        graphView.configureSingleGraph(symbol, start, end);
        return graphView.displaySingleSeries(series);
    }

    @Override
    public String compareShares(String symbol1, String symbol2, LocalDate start, LocalDate end) {
        List<PriceSeries> series = graphService.getComparisonGraphData(symbol1, symbol2, start, end);

        graphView.configureComparisonGraph(symbol1, symbol2, start, end);
        return graphView.displayComparison(series.get(0), series.get(1));
    }

    @Override
    public void addToWatchlist(String symbol) {
        validateSymbol(symbol);

        LocalDate today = LocalDate.now();

        WatchlistItem item = new WatchlistItem(
                symbol.trim().toUpperCase(),
                today.minusYears(1),
                today
        );

        watchlistService.addWatchlistItem(item);
    }

    @Override
    public List<String> getWatchlist() {
        return watchlistService.retrieveWatchlist()
                .getItems()
                .stream()
                .map(WatchlistItem::getSymbol)
                .collect(Collectors.toList());
    }

    @Override
    public String viewWatchlistItem(String symbol) {
        validateSymbol(symbol);

        LocalDate end = LocalDate.now();
        LocalDate start = end.minusMonths(6);

        PriceSeries series = graphService.getSingleGraphData(symbol, start, end);

        graphView.configureSingleGraph(symbol, start, end);
        return graphView.displaySingleSeries(series);
    }

    @Override
    public void handleDataPointClick(int index, double value) {
    }

    @Override
    public void setDateRange(LocalDate startDate, LocalDate endDate) {
        validateDates(startDate, endDate);
    }

    private void validateDates(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Dates cannot be null");
        }

        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }

        if (start.plusYears(2).isBefore(end)) {
            throw new IllegalArgumentException("Date range cannot exceed two years");
        }
    }

    private void validateSymbol(String symbol) {
        if (symbol == null || symbol.isBlank()) {
            throw new IllegalArgumentException("Symbol cannot be empty");
        }
    }
}
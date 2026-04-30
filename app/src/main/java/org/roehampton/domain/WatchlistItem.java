package org.roehampton.domain;

import java.time.LocalDate;

// Represents individual 'watchlisted' PriceSeries items
public class WatchlistItem {

    private final String symbol;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public WatchlistItem(String symbol, LocalDate startDate, LocalDate endDate) {

        this.symbol = symbol;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getSymbol() {
        return symbol;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public WatchlistItem createWatchlistItem(PriceSeries series) {

        if (series == null) {
            throw new IllegalArgumentException("Price series cannot be null.");
        }

        if (series.getSymbol() == null || series.getSymbol().isBlank()) {
            throw new IllegalArgumentException("Symbol cannot be empty.");
        }

        if (series.getPoints() == null || series.getPoints().isEmpty()) {
            throw new IllegalArgumentException("Price series must contain price points.");
        }

        LocalDate startDate = series.getPoints().get(0).getDate();
        LocalDate endDate = series.getPoints().get(0).getDate();

        for (PricePoint point : series.getPoints()) {

            if (point == null || point.getDate() == null) {
                continue;

            }

            LocalDate date = point.getDate();

            if (date.isBefore(startDate)) {
                startDate = date;
            }

            if (date.isAfter(endDate)) {
                endDate = date;
            }
        }

        return new WatchlistItem(
                series.getSymbol(),
                startDate,
                endDate
        );

    }
}
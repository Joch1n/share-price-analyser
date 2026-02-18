package org.roehampton.domain;

import java.time.LocalDate;

// Represents a single point in the graph for a company
public class PricePoint {

    private final LocalDate date;
    private final double closePrice;

    public PricePoint(LocalDate date, double closePrice) {

        this.date = date;
        this.closePrice = closePrice;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getClosePrice() {
        return closePrice;
    }
}


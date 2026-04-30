package org.roehampton.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Represents all the points on the graph for a stock
// Collection of PricePoint objects
public class PriceSeries {

    private final String symbol;
    private final List<PricePoint> points;

    public PriceSeries(String symbol, List<PricePoint> points) {

        this.symbol = symbol;
        this.points = new ArrayList<>(points);
    }

    public String getSymbol() {
        return symbol;
    }

    public List<PricePoint> getPoints() {
        return Collections.unmodifiableList(points);
    }
}

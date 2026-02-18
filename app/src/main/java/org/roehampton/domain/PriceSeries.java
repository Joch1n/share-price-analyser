package org.roehampton.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Represents all the points on the graph for a company
// Collection of PricePoint objects
public class PriceSeries {

    private final String symbol;
    private final String companyName; // can be null if unavailable
    private final String currency;    // can be null if unavailable
    private final List<PricePoint> points;

    public PriceSeries(String symbol, String companyName, String currency, List<PricePoint> points) {

        this.symbol = symbol;
        this.companyName = companyName;
        this.currency = currency;
        this.points = new ArrayList<>(points);
    }

    public String getSymbol() {
        return symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCurrency() {
        return currency;
    }

    public List<PricePoint> getPoints() {
        return Collections.unmodifiableList(points);
    }
}

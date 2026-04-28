package org.roehampton.presentation;

import org.roehampton.domain.PriceSeries;

import java.time.LocalDate;

public interface IGraphView {

    void showMessage(String message);

    void configureSingleGraph(String symbol, LocalDate startDate, LocalDate endDate);

    void configureComparisonGraph(String symbol1, String symbol2,
                                  LocalDate startDate, LocalDate endDate);

    void displaySingleSeries(PriceSeries series);

    void displayComparison(PriceSeries series1, PriceSeries series2);

    void clickDataPoint(int index, double value);
}
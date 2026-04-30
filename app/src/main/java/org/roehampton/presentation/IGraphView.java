package org.roehampton.presentation;

import org.roehampton.domain.PriceSeries;

import java.time.LocalDate;

public interface IGraphView {


    void configureSingleGraph(String symbol, LocalDate startDate, LocalDate endDate);

    void configureComparisonGraph(String symbol1, String symbol2,
                                  LocalDate startDate, LocalDate endDate);

    String displaySingleSeries(PriceSeries series);

    String displayComparison(PriceSeries series1, PriceSeries series2);

    void clickDataPoint(int index, double value);
}
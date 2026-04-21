package org.roehampton.presentation;

import java.time.LocalDate;

public interface IGraphView {

    void showMessage(String message);

    void configureSingleGraph(String symbol, LocalDate startDate, LocalDate endDate);

    void configureComparisonGraph(String symbol1, String symbol2, LocalDate startDate, LocalDate endDate);

    void clickDataPoint(int index, double value);
}
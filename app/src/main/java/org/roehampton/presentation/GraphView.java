package org.roehampton.presentation;

import org.roehampton.controller.IController;

import java.time.LocalDate;

public class GraphView implements IGraphView {

    private final IController controller;

    private boolean comparisonMode;
    private String primarySymbol;
    private String secondarySymbol;
    private LocalDate startDate;
    private LocalDate endDate;

    public GraphView(IController controller) {
        this.controller = controller;
    }

    @Override
    public void showMessage(String message) {
        System.out.println("[GraphView] " + message);
    }

    @Override
    public void configureSingleGraph(String symbol, LocalDate startDate, LocalDate endDate) {
        this.comparisonMode = false;
        this.primarySymbol = symbol;
        this.secondarySymbol = null;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public void configureComparisonGraph(String symbol1, String symbol2, LocalDate startDate, LocalDate endDate) {
        this.comparisonMode = true;
        this.primarySymbol = symbol1;
        this.secondarySymbol = symbol2;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public void clickDataPoint(int index, double value) {
        System.out.println("[GraphView] Clicked point: index=" + index + ", value=" + value);

        if (primarySymbol == null || startDate == null || endDate == null) {
            showMessage("Graph is not configured yet.");
            return;
        }

        if (comparisonMode) {
            controller.compareShares(primarySymbol, secondarySymbol, startDate, endDate);
        } else {
            controller.loadSingleShare(primarySymbol, startDate, endDate);
        }
    }
}
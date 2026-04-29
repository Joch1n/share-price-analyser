package org.roehampton.presentation;

import org.roehampton.controller.IController;
import org.roehampton.domain.PriceSeries;

import java.time.LocalDate;

public class GraphView implements IGraphView {

    private final IController controller;

    private String primarySymbol;
    private String secondarySymbol;
    private LocalDate startDate;
    private LocalDate endDate;

    //The graphview cannot be initialised without the Icontroller
    public GraphView(IController controller) {
        this.controller = controller;
    }

    @Override
    public void showMessage(String message) {
        System.out.println("[GraphView] " + message);
    }
    //It demonstartes the configuration of the comparison graph
    @Override
    public void configureSingleGraph(String symbol, LocalDate startDate, LocalDate endDate) {
        this.primarySymbol = symbol;
        this.secondarySymbol = null;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public void configureComparisonGraph(String symbol1, String symbol2,
                                         LocalDate startDate, LocalDate endDate) {
        this.primarySymbol = symbol1;
        this.secondarySymbol = symbol2;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String displaySingleSeries(PriceSeries series) {
        if (primarySymbol == null || series == null) {
            return "<p>Graph is not optimised</p>";
        }

        StringBuilder html = new StringBuilder();
        //It reiterates through all the data points
        html.append("<h2>").append(primarySymbol).append("</h2>");
        html.append("<table border='1'>");
        html.append("<tr><th>Date</th><th>Price</th></tr>");

        series.getPoints().forEach(point -> {
            html.append("<tr>")
                    .append("<td>").append(point.getDate()).append("</td>")
                    .append("<td>").append(point.getClosePrice()).append("</td>")
                    .append("</tr>");
        });

        html.append("</table>");

        return html.toString();
    }
    //This is an illustration of the comparison of two datasets
    @Override
    public String displayComparison(PriceSeries series1, PriceSeries series2) {
        if (primarySymbol == null || secondarySymbol == null
                || series1 == null || series2 == null) {
            return "<p>Comparison not optimised</p>";
        }

        StringBuilder html = new StringBuilder();

        html.append("<h2>Comparison: ")
                .append(primarySymbol)
                .append(" vs ")
                .append(secondarySymbol)
                .append("</h2>");

        html.append("<table border='1'>");
        html.append("<tr><th>Date</th><th>")
                .append(primarySymbol)
                .append("</th><th>")
                .append(secondarySymbol)
                .append("</th></tr>");

        int size = Math.min(series1.getPoints().size(), series2.getPoints().size());

        for (int i = 0; i < size; i++) {
            html.append("<tr>")
                    .append("<td>").append(series1.getPoints().get(i).getDate()).append("</td>")
                    .append("<td>").append(series1.getPoints().get(i).getClosePrice()).append("</td>")
                    .append("<td>").append(series2.getPoints().get(i).getClosePrice()).append("</td>")
                    .append("</tr>");
        }

        html.append("</table>");

        return html.toString();
    }

    @Override
    public void clickDataPoint(int index, double value) {
        if (primarySymbol == null || startDate == null || endDate == null) {
            showMessage("Graph is not optimised yet.");
            return;
        }

        controller.handleDataPointClick(index, value);
    }
}
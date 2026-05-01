package org.roehampton.presentation;

import org.roehampton.controller.IController;
import org.roehampton.domain.PriceSeries;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class GraphView implements IGraphView {

    private final IController controller;

    private String primarySymbol;
    private String secondarySymbol;
    private LocalDate startDate;
    private LocalDate endDate;

    public GraphView(@Lazy IController controller) {
        this.controller = controller;
    }
    @Override
    public void configureSingleGraph(String symbol, LocalDate startDate, LocalDate endDate) {
        this.primarySymbol = symbol;
        this.secondarySymbol = null;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    //It arranges
    @Override
    public void configureComparisonGraph(String symbol1, String symbol2,
                                         LocalDate startDate, LocalDate endDate) {
        this.primarySymbol = symbol1;
        this.secondarySymbol = symbol2;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    //It demonstrates the single stocks data
    @Override
    public String displaySingleSeries(PriceSeries series) {
        if (primarySymbol == null || series == null) {
            return "<p style='background-color:yellow;'>No data available</p>";
        }

        return buildSVG(series, null);
    }


    // It illustates the two stock comparision
    @Override
    public String displayComparison(PriceSeries s1, PriceSeries s2) {
        if (s1 == null || s2 == null || primarySymbol == null || secondarySymbol == null) {
            return "<p style='background-color:yellow;'>No data comparison </p>";
        }

        return buildSVG(s1, s2);
    }

    private String buildSVG(PriceSeries s1, PriceSeries s2) {

        int width = 800;
        int height = 400;
        int padding = 40;

        StringBuilder svg = new StringBuilder();

        svg.append("<svg width='").append(width)
                .append("' height='").append(height)
                .append("' style='border:1px solid black'>");

        int size = s1.getPoints().size();
        if (s2 != null) {
            size = Math.min(size, s2.getPoints().size());
        }
        if (s1.getPoints().isEmpty()||(s2 != null && s2.getPoints().isEmpty()))
            return "<p style='background-color:yellow; '>No data points </p>";

        if (size < 2) {
            return "<p style='background-color:yellow; '>Not enough data </p>";
        }
        double maxPrice = 0;

        for (int i = 0; i < size; i++) {
            maxPrice = Math.max(maxPrice, s1.getPoints().get(i).getClosePrice());
            if (s2 != null) {
                maxPrice = Math.max(maxPrice, s2.getPoints().get(i).getClosePrice());
            }
        }

        if (maxPrice == 0) maxPrice = 1;
        //gives a better overview and labelling of the x and y axis to the user
        svg.append("<line x1='").append(padding)
                .append("' y1='").append(height - padding)
                .append("' x2='").append(width - padding)
                .append("' y2='").append(height - padding)
                .append("' stroke='black'/>");

        svg.append("<line x1='").append(padding)
                .append("' y1='").append(padding)
                .append("' x2='").append(padding)
                .append("' y2='").append(height - padding)
                .append("' stroke='black'/>");

        //Grid line implementation for a polished/cleaner look
        for (int i = 0; i <= 5; i++) {
            int y = padding + i * (height - 2 * padding) / 5;

            double priceLabel = maxPrice - (i * maxPrice / 5);

            svg.append("<line x1='").append(padding)
                    .append("' y1='").append(y)
                    .append("' x2='").append(width - padding)
                    .append("' y2='").append(y)
                    .append("' stroke='lightgray' stroke-dasharray='4'/>");

            svg.append("<text x='5' y='").append(y + 4)
                    .append("' font-size='10'>")
                    .append(String.format("%.2f", priceLabel))
                    .append("</text>");
        }

        //Displays the title of the graph
        svg.append("<text x='200' y='25' font-size='15' font-weight='bold'>")
                .append("SharePriceAnalyser Stock Comparison")
                .append("</text>");

        //It implements the x and y axis
        svg.append("<text x='5' y='50'>Price</text>");
        svg.append("<text x='360' y='380'>Time</text>");

        //Demonstrates the color of the lines
        svg.append("<text x='50' y='50' fill='blue'>")
                .append("● ").append(primarySymbol)
                .append("</text>");

        if (s2 != null) {
            svg.append("<text x='180' y='50' fill='orange'>")
                    .append("● ").append(secondarySymbol)
                    .append("</text>");
        }

        int prevX1 = 0, prevY1 = 0;
        int prevX2 = 0, prevY2 = 0;

        for (int i = 0; i < size; i++) {

            int x = padding + (i * (width - 2 * padding)) / Math.max(1, size - 1);

            int y1 = height - padding - (int)((s1.getPoints().get(i).getClosePrice() / maxPrice) * (height - 2 * padding));

            if (i > 0) {
                svg.append("<line x1='").append(prevX1)
                        .append("' y1='").append(prevY1)
                        .append("' x2='").append(x)
                        .append("' y2='").append(y1)
                        .append("' stroke='blue' stroke-width='2'/>");
            }

            prevX1 = x;
            prevY1 = y1;

            if (s2 != null) {

                int y2 = height - padding - (int)((s2.getPoints().get(i).getClosePrice() / maxPrice) * (height - 2 * padding));

                if (i > 0) {
                    svg.append("<line x1='").append(prevX2)
                            .append("' y1='").append(prevY2)
                            .append("' x2='").append(x)
                            .append("' y2='").append(y2)
                            .append("' stroke='orange' stroke-width='2'/>");
                }

                prevX2 = x;
                prevY2 = y2;
            }
        }

        svg.append("</svg>");

        return svg.toString();
    }

    @Override
    public void clickDataPoint(int index, double value) {
        if (primarySymbol == null || startDate == null || endDate == null) {
            return;
        }

        controller.handleDataPointClick(index, value);
    }
}
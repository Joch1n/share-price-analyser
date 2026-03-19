package org.roehampton.presentation;

import org.roehampton.controller.IController;
import java.time.LocalDate;

public class GraphView {

    private final IController controller;

    public GraphView(IController controller) {
        this.controller = controller;
    }

    public void showMessage(String message) {
        System.out.println("GraphView: " + message);
    }

    public void clickDataPoint(int index, double value) {
        System.out.println("Clicked point: index=" + index + ", value=" + value);

        if (index % 2 == 0) {
            controller.loadSingleShare("AAPL",
                    LocalDate.of(2026, 3, 1),
                    LocalDate.of(2026, 3, 10));
        } else {
            controller.compareShares("AAPL", "GOOGL",
                    LocalDate.of(2026, 3, 1),
                    LocalDate.of(2026, 3, 10));
        }
    }
}
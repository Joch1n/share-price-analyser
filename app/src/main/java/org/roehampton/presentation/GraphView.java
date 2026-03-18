package main.java.org.roehampton.presentation;

import org.roehampton.presentation.IController;

public class GraphView {

    private final IController controller;

    public GraphView(IController controller) {
        this.controller = controller;
    }

    public void showMessage(String message) {
        System.out.println("GraphView: " + message);
    }

    public void clickDataPoint(int index, double value) {
        controller.handleDataPointClick(index, value);
    }
}
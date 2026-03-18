package main.java.org.roehampton.presentation;

// Minimal IController interface
interface IController {
    void handleDataPointClick(int index, double value);
}

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
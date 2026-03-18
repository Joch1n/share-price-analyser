package org.roehampton.presentation;

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

    public static void main(String[] args) {

        IController controller = new IController() {
            @Override
            public void handleDataPointClick(int index, double value) {
                System.out.println("Controller received click: index=" + index + ", value=" + value);
            }
        };

        GraphView view = new GraphView(controller);

        view.showMessage("Hello, Graph!");
        view.clickDataPoint(1, 42.0);
    }
}
ppackage org.roehampton.presentation;

import org.roehampton.controller.IController;
import org.roehampton.domain.DateRange;
import java.util.List;

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

    public void displayDateRange(DateRange range) {
        System.out.println("Displaying date range: " + range);
    }

    public void displayData(List<Double> data) {
        System.out.println("Graph data: " + data);
    }

    public static void main(String[] args) {
        // Dummy controller implementation
        IController controller = new IController() {
            @Override
            public void handleDataPointClick(int index, double value) {
                System.out.println("Controller clicked: index=" + index + ", value=" + value);
            }
        };

        GraphView view = new GraphView(controller);

        view.showMessage("Hello, Graph!");
        view.clickDataPoint(1, 42.0);
        view.displayDateRange(new DateRange(java.time.LocalDate.of(2026, 3, 18),
                java.time.LocalDate.of(2026, 3, 25)));
        view.displayData(List.of(10.0, 20.5, 30.2));
    }
}
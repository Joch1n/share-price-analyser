package org.roehampton.presentation;

import org.roehampton.controller.IController;

public class GraphView {

    private final IController controller;

    public GraphView(IController controller) {
        this.controller = controller;
    }

    // Example method to make it do something
    public void showMessage(String message) {
        System.out.println("GraphView: " + message);
    }
}
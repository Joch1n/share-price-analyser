package org.roehampton.presentation;

import org.roehampton.controller.IController;

public class GraphView implements IGraphView {

    private final IController controller;

    public GraphView(IController controller) {

        this.controller = controller;
    }
}

package org.roehampton.presentation;

import org.roehampton.controller.IController;

public class DateRangeView implements IDateRangeView {

    private final IController controller;

    public DateRangeView(IController controller) {

        this.controller = controller;
    }
}

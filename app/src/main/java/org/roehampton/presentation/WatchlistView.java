package org.roehampton.presentation;

import org.roehampton.controller.IController;

public class WatchlistView implements IWatchlistView {

    private final IController controller;

    public WatchlistView(IController controller) {

        this.controller = controller;
    }
}

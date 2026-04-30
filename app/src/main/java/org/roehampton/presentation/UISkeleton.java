package org.roehampton.presentation;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class UISkeleton implements IUserInterface {

    private final StockSearchView stockSearchView;
    private final DateRangeView dateRangeView;
    private final WatchlistView watchlistView;
    private final GraphView graphView;

    public UISkeleton(StockSearchView stockSearchView, DateRangeView dateRangeView, WatchlistView watchlistView, GraphView graphView) {

        this.stockSearchView = stockSearchView;
        this.dateRangeView = dateRangeView;
        this.watchlistView = watchlistView;
        this.graphView = graphView;

    }


    @Override
    public String renderHomePage() {

        String homePageHtml = loadHtml("ui/index.html");

        return homePageHtml;

    }

    @Override
    public String renderWatchlistPage() {

        return "";

    }

    // Open HTML files and return contents if found
    private String loadHtml(String path) {

        try (InputStream input = getClass().getClassLoader().getResourceAsStream(path)) {

            if (input == null) {
                throw new IllegalStateException("HTML file not found: " + path);
            }

            return new String(input.readAllBytes(), StandardCharsets.UTF_8);

        } catch (IOException e) {
            throw new RuntimeException("Could not load HTML file: " + path, e);
        }
    }

}
package org.roehampton.presentation;

public class UISkeleton implements IUserInterface {

    private final ICompanySearchView companySearchView;
    private final IWatchlistView watchlistView;
    private final IGraphView graphView;

    public UISkeleton(ICompanySearchView companySearchView, IWatchlistView watchlistView, IGraphView graphView) {

        this.companySearchView = companySearchView;
        this.watchlistView = watchlistView;
        this.graphView = graphView;

    }

    @Override
    public void start() {

        // Start server, handle routing

    }
}
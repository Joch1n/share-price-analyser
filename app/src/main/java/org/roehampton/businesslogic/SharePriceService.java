package org.roehampton.businesslogic;

import org.roehampton.dataaccess.IAPIClient;
import org.roehampton.dataaccess.IShareDatabase;
import org.roehampton.domain.PriceSeries;
import org.roehampton.domain.Watchlist;
import org.roehampton.domain.WatchlistItem;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class SharePriceService implements IGraphService, IWatchlistService {

    private final GraphService graphService;
    private final WatchlistService watchlistService;

    public SharePriceService(IShareDatabase db, IAPIClient api) {
        Objects.requireNonNull(db);
        Objects.requireNonNull(api);

        this.graphService = new GraphService(db, api);
        this.watchlistService = new WatchlistService(db);
    }

    @Override
    public PriceSeries getSingleGraphData(String symbol, LocalDate start, LocalDate end) {
        return graphService.getSingleGraphData(symbol, start, end);
    }

    @Override
    public List<PriceSeries> getComparisonGraphData(String symbol1,
                                                    String symbol2,
                                                    LocalDate start,
                                                    LocalDate end) {
        return graphService.getComparisonGraphData(symbol1, symbol2, start, end);
    }

    @Override
    public void addWatchlistItem(WatchlistItem item) {
        watchlistService.addWatchlistItem(item);
    }

    @Override
    public Watchlist retrieveWatchlist() {
        return watchlistService.retrieveWatchlist();
    }
}
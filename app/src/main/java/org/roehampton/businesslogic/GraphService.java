package org.roehampton.businesslogic;

import org.roehampton.domain.PriceSeries;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class GraphService implements IGraphService {

    private final IDataService dataService;

    public GraphService(IDataService dataService) {
        this.dataService = Objects.requireNonNull(dataService);
    }

    @Override
    public PriceSeries getSingleGraphData(String symbol, LocalDate start, LocalDate end) {
        return dataService.getSharePrices(symbol, start, end);
    }

    @Override
    public List<PriceSeries> getComparisonGraphData(String symbol1, String symbol2,
                                                    LocalDate start, LocalDate end) {
        return List.of(
                dataService.getSharePrices(symbol1, start, end),
                dataService.getSharePrices(symbol2, start, end)
        );
    }
}
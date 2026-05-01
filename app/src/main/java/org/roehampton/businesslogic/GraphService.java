package org.roehampton.businesslogic;

import org.roehampton.domain.PricePoint;
import org.roehampton.domain.PriceSeries;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
public class GraphService implements IGraphService {

    private final IDataService dataService;

    public GraphService(IDataService dataService) {
        this.dataService = Objects.requireNonNull(dataService);
    }

    @Override
    public PriceSeries getSingleGraphData(String symbol, LocalDate start, LocalDate end) {

        // It gets raw data
        PriceSeries raw = dataService.getSharePrices(symbol, start, end);

        // It validates data as a safe fallback
        PriceSeries validated = validateData(raw);

        // It filters irrelevant and invalid points
        return filterInvalidPoints(validated);
    }

    @Override
    public List<PriceSeries> getComparisonGraphData(String symbol1, String symbol2,
                                                    LocalDate start, LocalDate end) {

        validate(symbol1, start, end);
        validate(symbol2, start, end);

        PriceSeries s1 = getSingleGraphData(symbol1, start, end);
        PriceSeries s2 = getSingleGraphData(symbol2, start, end);

        return List.of(s1, s2);
    }

    // Displays validation
    private void validate(String symbol, LocalDate start, LocalDate end) {
        if (symbol == null || symbol.isBlank()) return;
        if (start == null || end == null || start.isAfter(end)) return;
    }

    // It ensures the data usability
    private PriceSeries validateData(PriceSeries series) {
        if (series == null || series.getPoints() == null || series.getPoints().isEmpty()) {
            return new PriceSeries("", List.of()); // safe fallback
        }
        return series;
    }

    // It safely removes invalid points
    private PriceSeries filterInvalidPoints(PriceSeries series) {

        List<PricePoint> cleanedPoints = new ArrayList<>();

        for (PricePoint point : series.getPoints()) {
            if (point.getClosePrice() > 0) {
                cleanedPoints.add(point);
            }
        }

        return new PriceSeries("", cleanedPoints);
    }
}
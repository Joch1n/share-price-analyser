package org.roehampton.businesslogic;

import org.roehampton.dataaccess.IAPIClient;
import org.roehampton.dataaccess.IShareDatabase;
import org.roehampton.domain.PricePoint;
import org.roehampton.domain.PriceSeries;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GraphService implements IGraphService {

    private final IShareDatabase db;
    private final IAPIClient api;
    private final Clock clock = Clock.systemDefaultZone();

    public GraphService(IShareDatabase db, IAPIClient api) {
        this.db = Objects.requireNonNull(db);
        this.api = Objects.requireNonNull(api);
    }

    @Override
    public PriceSeries getSingleGraphData(String symbol, LocalDate start, LocalDate end) {
        PriceSeries raw = getSharePrices(symbol, start, end);
        PriceSeries validated = validateData(raw);
        return filterInvalidPoints(validated);
    }

    @Override
    public List<PriceSeries> getComparisonGraphData(String symbol1,
                                                    String symbol2,
                                                    LocalDate start,
                                                    LocalDate end) {
        PriceSeries s1 = getSingleGraphData(symbol1, start, end);
        PriceSeries s2 = getSingleGraphData(symbol2, start, end);

        return List.of(s1, s2);
    }

    private PriceSeries getSharePrices(String symbol, LocalDate from, LocalDate to) {
        validate(symbol, from, to);

        String normalisedSymbol = symbol.trim().toUpperCase();
        IShareDatabase.DataFound found = db.dbCheck(normalisedSymbol, from, to);

        switch (found) {
            case FOUND:
                return db.getStoredData(normalisedSymbol, from, to);

            case NOT_FOUND:
                return fetchAndStore(normalisedSymbol, from, to);

            case PARTIAL:
                fetchAndStore(normalisedSymbol, from, to);
                return db.getStoredData(normalisedSymbol, from, to);

            default:
                throw new IllegalStateException("Unexpected dbCheck result: " + found);
        }
    }

    private PriceSeries fetchAndStore(String symbol, LocalDate from, LocalDate to) {
        PriceSeries fetched = api.getSharePrices(symbol, from, to);
        db.storeData(fetched);
        return fetched;
    }

    private void validate(String symbol, LocalDate from, LocalDate to) {
        if (symbol == null || symbol.trim().isEmpty()) {
            throw new IllegalArgumentException("Symbol must be provided.");
        }

        if (from == null || to == null) {
            throw new IllegalArgumentException("From/to dates must be provided.");
        }

        if (!from.isBefore(to)) {
            throw new IllegalArgumentException("From date must be before to date.");
        }

        LocalDate today = LocalDate.now(clock);

        if (to.isAfter(today)) {
            throw new IllegalArgumentException("To date cannot be in the future.");
        }

        if (from.isBefore(today.minusYears(2))) {
            throw new IllegalArgumentException("Date range cannot exceed two years.");
        }
    }

    private PriceSeries validateData(PriceSeries series) {
        if (series == null || series.getPoints() == null || series.getPoints().isEmpty()) {
            return new PriceSeries("", List.of());
        }

        return series;
    }

    private PriceSeries filterInvalidPoints(PriceSeries series) {
        List<PricePoint> cleanedPoints = new ArrayList<>();

        for (PricePoint point : series.getPoints()) {
            if (point.getClosePrice() > 0) {
                cleanedPoints.add(point);
            }
        }

        return new PriceSeries(series.getSymbol(), cleanedPoints);
    }
}
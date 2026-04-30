package org.roehampton.businesslogic;

import org.roehampton.dataaccess.IAPIClient;
import org.roehampton.dataaccess.IShareDatabase;
import org.roehampton.domain.PriceSeries;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Objects;

public class DataService implements IDataService {

    private final IShareDatabase db;
    private final IAPIClient api;
    private final Clock clock;

    // Creates the service with the database, API client, and clock it needs
    public DataService(IShareDatabase db, IAPIClient api, Clock clock) {

        this.db = Objects.requireNonNull(db);
        this.api = Objects.requireNonNull(api);
        this.clock = Objects.requireNonNull(clock);
    }

    // Gets share prices for a symbol and date range
    // It checks local data first, then uses the API if needed
    @Override
    public PriceSeries getSharePrices(String symbol, LocalDate from, LocalDate to) {
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

    // Fetches missing share data from the API and saves it in the database
    private PriceSeries fetchAndStore(String symbol, LocalDate from, LocalDate to) {
        PriceSeries fetched = api.getSharePrices(symbol, from, to);
        db.storeData(fetched);
        return fetched;
    }

    // Checks that the symbol and dates are valid before loading data
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
}
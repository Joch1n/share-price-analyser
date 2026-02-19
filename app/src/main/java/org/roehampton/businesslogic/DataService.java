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

    public DataService(IShareDatabase db, IAPIClient api, Clock clock) {
        this.db = Objects.requireNonNull(db);
        this.api = Objects.requireNonNull(api);
        this.clock = Objects.requireNonNull(clock);
    }

    @Override
    public PriceSeries getSharePrices(String symbol, LocalDate from, LocalDate to) {
        validate(symbol, from, to);

        IShareDatabase.DataFound found = db.dbCheck(symbol, from, to);

        switch (found) {
            case FOUND:
                return db.getStoredData(symbol, from, to);

            case NOT_FOUND: {
                PriceSeries fetched = api.getSharePrices(symbol, from, to);
                db.storeData(fetched);
                return fetched;
            }

            case PARTIAL: {
                // Simple approach: fetch full range then store (DB should upsert by date)
                PriceSeries fetched = api.getSharePrices(symbol, from, to);
                db.storeData(fetched);

                // return merged (DB becomes the source of truth)
                return db.getStoredData(symbol, from, to);
            }

            default:
                throw new IllegalStateException("Unexpected dbCheck result: " + found);
        }
    }

    private void validate(String symbol, LocalDate from, LocalDate to) {
        if (symbol == null || symbol.trim().isEmpty())
            throw new IllegalArgumentException("Symbol must be provided.");

        if (from == null || to == null)
            throw new IllegalArgumentException("From/to dates must be provided.");

        if (!from.isBefore(to))
            throw new IllegalArgumentException("From date must be before to date.");

        LocalDate today = LocalDate.now(clock);
        if (to.isAfter(today))
            throw new IllegalArgumentException("To date cannot be in the future.");

        if (from.isBefore(today.minusYears(2)))
            throw new IllegalArgumentException("Date range cannot exceed two years.");
    }
}
package org.roehampton.dataaccess;

import org.roehampton.domain.PriceSeries;
import java.util.List;

import java.time.LocalDate;

public interface IShareDatabase {

    enum DataFound {

        FOUND,
        NOT_FOUND,
        PARTIAL;
    }

    DataFound dbCheck(String symbol, LocalDate from, LocalDate to);

    void storeData(PriceSeries priceSeries);

    PriceSeries getStoredData(String symbol, LocalDate from, LocalDate to);

    void saveWatchlistItem(String symbol);
    List<String> getWatchlist();
}

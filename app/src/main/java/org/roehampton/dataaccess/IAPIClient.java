package org.roehampton.dataaccess;

import org.roehampton.domain.PriceSeries;

import java.time.LocalDate;

public interface IAPIClient {
    PriceSeries getStockPrices(String symbol, LocalDate from, LocalDate to);

}

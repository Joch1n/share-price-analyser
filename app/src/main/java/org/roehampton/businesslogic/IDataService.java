package org.roehampton.businesslogic;

import org.roehampton.domain.PriceSeries;

import java.time.LocalDate;

public interface IDataService {

    PriceSeries getSharePrices(String symbol, LocalDate from, LocalDate to);
}
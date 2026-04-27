package org.roehampton.businesslogic;

import org.roehampton.domain.PriceSeries;

import java.time.LocalDate;
import java.util.List;

public interface IGraphService {
    PriceSeries getSingleGraphData(String symbol, LocalDate start, LocalDate end);
    List<PriceSeries> getComparisonGraphData(String symbol1, String symbol2, LocalDate start, LocalDate end);
}
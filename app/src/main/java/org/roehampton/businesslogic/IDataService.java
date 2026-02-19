package org.roehampton.businesslogic;

import java.time.LocalDate;
import java.util.List;

public interface IDataService {

    /**
     * Returns chart-ready data for selected companies and date range.
     */
    ChartDataDTO getChartData(List<String> symbols,
                              LocalDate startDate,
                              LocalDate endDate);
}
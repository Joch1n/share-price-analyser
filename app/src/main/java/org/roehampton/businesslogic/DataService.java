package org.roehampton.businesslogic;

import java.time.Clock;
import java.time.LocalDate;
import java.util.*;

public class DataService implements IDataService {

    private static final int MAX_COMPANIES = 2;

    private final ISharePriceRepository repository;
    private final IApiClient apiClient;
    private final Clock clock;

    public DataService(ISharePriceRepository repository,
                       IApiClient apiClient,
                       Clock clock) {
        this.repository = repository;
        this.apiClient = apiClient;
        this.clock = clock;
    }

    @Override
    public ChartDataDTO getChartData(List<String> symbols,
                                     LocalDate startDate,
                                     LocalDate endDate) {

        validate(symbols, startDate, endDate);

        List<SeriesDTO> seriesList = new ArrayList<>();

        for (String symbol : symbols) {

            // 1. Check which dates are missing locally
            Set<LocalDate> missing =
                    repository.findMissingTradingDates(symbol, startDate, endDate);

            // 2. Fetch missing data from API
            if (!missing.isEmpty()) {
                List<SharePrice> fetched =
                        apiClient.fetchDailyPrices(symbol, startDate, endDate);

                repository.saveAll(fetched);
            }

            // 3. Load complete data from DB (offline friendly)
            List<SharePrice> prices =
                    repository.findPrices(symbol, startDate, endDate);

            prices.sort(Comparator.comparing(SharePrice::getDate));

            // 4. Map to chart series
            List<PointDTO> points = new ArrayList<>();
            for (SharePrice p : prices) {
                points.add(new PointDTO(p.getDate(), p.getClose()));
            }

            String companyName =
                    repository.findCompanyName(symbol).orElse(symbol);

            seriesList.add(new SeriesDTO(symbol, companyName, points));
        }

        return new ChartDataDTO(seriesList);
    }

    // -------------------------
    // Validation
    // -------------------------
    private void validate(List<String> symbols,
                          LocalDate start,
                          LocalDate end) {

        if (symbols == null || symbols.isEmpty())
            throw new RuntimeException("At least one company required.");

        if (symbols.size() > MAX_COMPANIES)
            throw new RuntimeException("Maximum two companies allowed.");

        if (start == null || end == null)
            throw new RuntimeException("Dates must be selected.");

        if (!start.isBefore(end))
            throw new RuntimeException("Start date must be before end date.");

        LocalDate today = LocalDate.now(clock);

        if (end.isAfter(today))
            throw new RuntimeException("End date cannot be in future.");

        if (start.isBefore(today.minusYears(2)))
            throw new RuntimeException("Maximum range is two years.");
    }
}
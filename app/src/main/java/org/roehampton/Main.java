package org.roehampton;

import org.roehampton.dataaccess.APIClient;
import org.roehampton.domain.PriceSeries;

import java.net.URI;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        // Test API connection
        APIClient a = new APIClient(URI.create("https://financialmodelingprep.com"));

        PriceSeries priceSeries = a.getSharePrices("AAPL", LocalDate.of(2024, 2, 2), LocalDate.of(2026, 2, 5));

        /* Outputs some data gathered from the API in readable format */
        priceSeries.getPoints().stream()
                .limit(5)
                .forEach(p -> System.out.println(priceSeries.getSymbol() + "-> " + p.getDate() + " : " + p.getClosePrice()));
    }
    
}
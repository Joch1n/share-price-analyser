package org.roehampton.dataaccess;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.roehampton.domain.PricePoint;
import org.roehampton.domain.PriceSeries;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Component that directly communicates with API
public class APIClient implements IAPIClient {

    private final HttpClient http;
    private final URI baseUri;
    private final String apiKey;

    public APIClient(URI baseUri) {

        if (baseUri == null) throw new IllegalArgumentException("A baseUri must be entered.");

        this.apiKey = System.getenv("FMP_API_KEY");

        if (this.apiKey == null || this.apiKey.isBlank()) {
            throw new IllegalStateException("Missing environment variable FMP_API_KEY.");
        }

        this.baseUri = baseUri;
        this.http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(15)).build();
    }


    @Override
    public PriceSeries getSharePrices(String symbol, LocalDate from, LocalDate to) {

        if (symbol == null || symbol.trim().isEmpty())
            throw new APIClientException("A symbol must be entered.");

        if (from == null || to == null)
            throw new APIClientException("A start and end date must be entered.");

        try {
            URI uri = buildFMPUri(symbol.trim(), from, to);

            HttpRequest req = HttpRequest.newBuilder(uri)
                    .timeout(Duration.ofSeconds(15))
                    .GET()
                    .build();

            HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());

            if (res.statusCode() != 200) {

                throw new APIClientException("API returned HTTP " + res.statusCode());
            }

            return jsonToPriceSeries(symbol.trim(), res.body());

        } catch (IOException e) {
            throw new APIClientException("I/O error calling API", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new APIClientException("Request interrupted", e);
        } catch (RuntimeException e) {
            throw new APIClientException("Failed to parse API response: " + e.getMessage(), e);
        }
    }

    // Builds URI to request from FMP API
    private URI buildFMPUri(String symbol, LocalDate from, LocalDate to) {

        String base = baseUri.toString();

        if (base.endsWith("/")) base = base.substring(0, base.length() - 1);

        String q = "/stable/historical-price-eod/light?"
                + "symbol=" + enc(symbol)
                + "&from=" + enc(from.toString())
                + "&to=" + enc(to.toString())
                + "&apikey=" + enc(apiKey);

        return URI.create(base + q);
    }

    // Ensures URL parts are in the correct format
    private String enc(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }

    // Converts API response to acceptable parameters
    // Creates PriceSeries object
    // Specific to expected FMP API response
    private PriceSeries jsonToPriceSeries(String symbol, String json) throws JsonProcessingException {

        List<PricePoint> points = new ArrayList<>();

        if (json == null || json.isBlank()) {
            return new PriceSeries(symbol, points);
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);

            if (!root.isArray()) {
                throw new APIClientException("Expected JSON array response but got: " + root.getNodeType());
            }

            for (JsonNode item : root) {
                if (!item.isObject()) continue;

                JsonNode dateNode = item.get("date");
                JsonNode priceNode = item.get("price");

                if (dateNode == null || priceNode == null) continue;

                LocalDate date = LocalDate.parse(dateNode.asText());
                double price = priceNode.asDouble();

                points.add(new PricePoint(date, price));
            }

            return new PriceSeries(symbol, points);

        } catch (Exception e) {
            throw new APIClientException("Failed to parse JSON response", e);
        }
    }
}

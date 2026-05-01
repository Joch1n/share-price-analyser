package org.roehampton.presentation;

import org.roehampton.businesslogic.IWatchlistService;
import org.roehampton.domain.WatchlistItem;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class WebController {

    private final IWatchlistService watchlistService;
    private final SharePriceUI sharePriceUI;

    public WebController(IWatchlistService watchlistService,
                         SharePriceUI sharePriceUI) {
        this.watchlistService = watchlistService;
        this.sharePriceUI = sharePriceUI;
    }

    // Called when user clicks "View Chart" on Single Stock tab
    @GetMapping("/stock")
    @ResponseBody
    public String getSingleStock(@RequestParam String symbol,
                                 @RequestParam String from,
                                 @RequestParam String to) {
        try {
            return sharePriceUI.handleSingleStockRequest(
                    symbol.trim().toUpperCase(),
                    LocalDate.parse(from),
                    LocalDate.parse(to));
        } catch (Exception e) {
            return "<p style='color:red;'>" + e.getMessage() + "</p>";
        }
    }

    // Called when user clicks "Compare" on Compare Stocks tab
    @GetMapping("/compare")
    @ResponseBody
    public String compareStocks(@RequestParam String symbol1,
                                @RequestParam String symbol2,
                                @RequestParam String from,
                                @RequestParam String to) {
        try {
            return sharePriceUI.handleComparisonRequest(
                    symbol1.trim().toUpperCase(),
                    symbol2.trim().toUpperCase(),
                    LocalDate.parse(from),
                    LocalDate.parse(to));
        } catch (Exception e) {
            return "<p style='color:red;'>" + e.getMessage() + "</p>";
        }
    }

    // Called when Watchlist tab loads
    @GetMapping("/watchlist")
    public List<String> getWatchlist() {
        return watchlistService.retrieveWatchlist()
                .getItems()
                .stream()
                .map(WatchlistItem::getSymbol)
                .collect(Collectors.toList());
    }

    // Called when user clicks "Add to Watchlist"
    @PostMapping("/watchlist")
    public Map<String, String> addToWatchlist(@RequestParam String symbol) {
        try {
            LocalDate today = LocalDate.now();
            WatchlistItem item = new WatchlistItem(
                    symbol.trim().toUpperCase(),
                    today.minusYears(1),
                    today);
            watchlistService.addWatchlistItem(item);
            Map<String, String> r = new HashMap<>();
            r.put("message", symbol.toUpperCase() + " added to watchlist.");
            return r;
        } catch (Exception e) {
            Map<String, String> r = new HashMap<>();
            r.put("message", "Error: " + e.getMessage());
            return r;
        }
    }
}
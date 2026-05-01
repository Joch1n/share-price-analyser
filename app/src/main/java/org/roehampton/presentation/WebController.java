package org.roehampton.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WebController {

    private final SharePriceUI sharePriceUI;

    public WebController(SharePriceUI sharePriceUI) {
        this.sharePriceUI = sharePriceUI;
    }

    @GetMapping("/")
    @ResponseBody
    public String home() {
        return sharePriceUI.renderHomePage();
    }

    // Called when user clicks "View Chart" on Single Stock tab
    @GetMapping("/api/stock")
    @ResponseBody
    public String getSingleStock(@RequestParam String symbol,
                                 @RequestParam String from,
                                 @RequestParam String to) {
        try {
            return sharePriceUI.handleSingleStockRequest(
                    symbol.trim().toUpperCase(),
                    from,
                    to
            );
        } catch (Exception e) {
            return "<p style='color:red;'>" + e.getMessage() + "</p>";
        }
    }

    // Called when user clicks "Compare" on Compare Stocks tab
    @GetMapping("/api/compare")
    @ResponseBody
    public String compareStocks(@RequestParam String symbol1,
                                @RequestParam String symbol2,
                                @RequestParam String from,
                                @RequestParam String to) {
        try {
            return sharePriceUI.handleComparisonRequest(
                    symbol1.trim().toUpperCase(),
                    symbol2.trim().toUpperCase(),
                    from,
                    to
            );
        } catch (Exception e) {
            return "<p style='color:red;'>" + e.getMessage() + "</p>";
        }
    }

    // Called when Watchlist tab loads
    @GetMapping("/api/watchlist")
    @ResponseBody
    public List<SharePriceUI.WatchlistItem> getWatchlist() {
        return sharePriceUI.getWatchlist();
    }

    // Called when user clicks "Add to Watchlist"
    @PostMapping("/api/watchlist")
    @ResponseBody
    public SharePriceUI.WatchlistResponse addToWatchlist(@RequestParam String symbol,
                                                         @RequestParam String from,
                                                         @RequestParam String to) {
        return sharePriceUI.addToWatchlist(symbol, from, to);
    }
}
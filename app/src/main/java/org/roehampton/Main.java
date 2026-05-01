package org.roehampton;

import org.roehampton.businesslogic.IGraphService;
import org.roehampton.businesslogic.IWatchlistService;
import org.roehampton.controller.IController;
import org.roehampton.controller.SharePriceController;
import org.roehampton.presentation.IGraphView;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.roehampton.presentation.SharePriceUI;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public IController controller(IGraphService graphService,
                                  IWatchlistService watchlistService,
                                  IGraphView graphView) {
        return new SharePriceController(graphService, watchlistService, graphView);
    }

    @Bean
    public SharePriceUI sharePriceUI(IController controller) {
        return new SharePriceUI(controller);
    }
}

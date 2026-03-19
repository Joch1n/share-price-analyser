package org.roehampton.presentation;

import java.time.LocalDate;

public interface IController {
    void loadSingleShare(String symbol, LocalDate start, LocalDate end);

    void compareShares(String symbol1, String symbol2, LocalDate start, LocalDate end);
}
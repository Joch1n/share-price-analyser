package org.roehampton.businesslogic;

<<<<<<< HEAD
=======
import org.roehampton.domain.PriceSeries;

>>>>>>> 175db492dfc75795142f6feb59e70809610f8833
import java.time.LocalDate;

public interface IDataService {
    PriceSeries getSharePrices(String symbol, LocalDate from, LocalDate to);
}